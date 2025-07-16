package com.example.weatherapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.presentation.model.Coordinates
import com.example.weatherapp.presentation.model.MainScreenTags
import com.example.weatherapp.presentation.model.WeatherState
import com.example.weatherapp.presentation.model.WeatherState.Error
import com.example.weatherapp.presentation.model.WeatherState.Loading
import com.example.weatherapp.presentation.model.WeatherState.Start
import com.example.weatherapp.presentation.model.WeatherState.Success
import com.example.weatherapp.presentation.theme.DarkColorScheme
import com.example.weatherapp.presentation.theme.LightColorScheme
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val retrofitProvider: RetrofitProvider by lazy { RetrofitProvider(applicationContext) }
    private val state: MutableState<WeatherState> = mutableStateOf(Start)
    private val items = Cities.entries
    private var currentCity: Cities = Cities.SPB
    private var currentLocation: Coordinates = Coordinates(0.0, 0.0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(innerPadding)
                }
                }
            }
        }

    @Composable
    fun WeatherScreen(innerPadding: PaddingValues) {
        var isDarkTheme by remember { mutableStateOf(false) } // rememberSaveable - вот про эту штуку почитай.
        val color = if (isDarkTheme) {
            DarkColorScheme.primary
        } else {
            LightColorScheme.primary
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            when (state.value) {
                Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                    )

                Error -> println("i chto?..")
                Start -> {
                    DropDownList()
                    FetchWeatherButton()
                    FetchLocationButton()
                }

                is Success -> {
                    val weather = (state.value as Success).weather
                    Text(
                        text = "Погода в $currentCity: ${weather.main.temp}"
                    )
                    ShowCoordinatesText()

                    ReturnToMainButton()
                }
            }
        }
    }

    @Composable
    fun FetchWeatherButton() {
        Button(
            onClick = {
                state.value = Loading
                lifecycleScope.launch {
                    try {
                        retrofitProvider.fetchWeather(
                            city =  currentCity,
                            units = Units.METRIC.value
                        ).let {
                            state.value = Success(it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
        ) {
            Text(this@MainActivity.getString(R.string.weather_request_button_text))
        }
    }

    @Composable
    fun DropDownList() {
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(currentCity)  }

        Column {
            Text(text = this@MainActivity.getString(R.string.city_select))
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedTextField(
                value =  this@MainActivity.getString(selectedItem.cityName),
                onValueChange = {},
                modifier = Modifier.width(300.dp),
                readOnly = true,
                // В дальнейшем хочется сделать что бы дропдаун раскрывался по клику
                // не trailingIcon, а всего текстового поля.
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            )
            DropdownMenu(
                modifier = Modifier.testTag(MainScreenTags.dropdownMenu),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier.testTag(MainScreenTags.dropdownMenuItem(item.name)),
                        onClick = {
                            selectedItem = item
                            currentCity = selectedItem
                            expanded = false
                    },
                        text = { Text(text = this@MainActivity.getString(item.cityName)) }
                    )
                }
            }
        }
    }

    @Composable
    fun ReturnToMainButton() {
        Button(onClick = { state.value = Start }) {
            Text(text = this@MainActivity.getString(R.string.return_to_main_screen))
        }
    }

    @Composable
    private fun FetchLocationButton() {
        Button(onClick = {
            // Add permissions request here
            lifecycleScope.launch {
                currentLocation = getCurrentLocation()
                Log.i("REQUEST", "Запрос пошел")
            }
        }
        ) {
            Text(text = this@MainActivity.getString(R.string.request_location_title))
        }
    }

    @Composable
    private fun ShowCoordinatesText() {
        Column {
            Text(text = "latitude = ${currentLocation.latitude}")
            Text(text = "longitude = ${currentLocation.longitude}")
        }
    }

    private suspend fun getCurrentLocation(): Coordinates {
        val isGranted = checkSelfPermissions(
            context = this,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        return suspendCoroutine { continuation: Continuation<Coordinates> ->
            if (isGranted) {
                Log.i("REQUEST INFO", "Пермиссии даны")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location ->
                        continuation.resume(Coordinates(location.latitude, location.longitude))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(
                            IllegalStateException(exception)
                        )
                    }
            } else {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, permissions,0)
            }
        }
    }


    private fun checkSelfPermissions(
        context: Context,
        vararg permissions: String
    ): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
