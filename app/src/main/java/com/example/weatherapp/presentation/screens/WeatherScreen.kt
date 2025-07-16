// TRYING TO PUT COMPOSE LAYOUT IN SEPARATE FILE

/*
package com.example.weatherapp.presentation.screens

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.presentation.Cities
import com.example.weatherapp.presentation.MainActivity
import com.example.weatherapp.presentation.RetrofitProvider
import com.example.weatherapp.presentation.model.Coordinates
import com.example.weatherapp.presentation.model.WeatherState
import com.example.weatherapp.presentation.model.WeatherState.Start
import com.example.weatherapp.presentation.model.WeatherState.Loading
import com.example.weatherapp.presentation.model.WeatherState.Error
import com.example.weatherapp.presentation.model.WeatherState.Success
import com.example.weatherapp.presentation.theme.DarkColorScheme
import com.example.weatherapp.presentation.theme.LightColorScheme
import kotlinx.coroutines.launch

private val retrofitProvider: RetrofitProvider by lazy { RetrofitProvider() }
private val state: MutableState<WeatherState> = mutableStateOf(Start)
private val items = Cities.entries
private var currentCity: Cities = Cities.SPB
private var currentLocation: Coordinates = Coordinates(0.0, 0.0)

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
                fetchLocationButton()
            }

            is Success -> {
                val weather = (state.value as Success).weather
                Text(
                    text = "Погода в $currentCity: ${weather.main.temp}"
                )

                Row {
                    Text(
                        text = currentLocation.latitude.toString()
                    )
                    Spacer(modifier = Modifier.size(100.dp))
                    Text(
                        text = (currentLocation.longitude.toString()
                                )
                    )
                }

                ReturnToMainButton()
            }
        }
    }
}

@Composable
fun FetchWeatherButton() {
    Button(
        onClick = {
            state.value = State.Loading
            lifecycleScope.launch {
                try {
                    retrofitProvider.fetchWeather(
                        city =  currentCity,
                        units = Units.METRIC.value
                    ).let {
                        state.value = State.Success(it)
                    }
                } catch (e: Exception) {
                    Log.e(
                        "Alarma!!!",
                        this@MainActivity.getString(R.string.request_error)
                    )
                    e.printStackTrace()
                }
            }
        },
    ) {
        Text(this@MainActivity.getString(R.string.weather_request_button_text))
    }
}

@Composable
fun fetchLocationButton() {
    Button(onClick = {
        lifecycleScope.launch {
            currentLocation = getCurrentLocation()
        }
    }
    ) {
        Text(text = "Запросить местоположение")
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
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        DropdownMenu(
            modifier = Modifier.testTag(MainActivity.MainScreenTags.dropdownMenu),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(MainActivity.MainScreenTags.dropdownMenuItem(item.name)),
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
    Button(onClick = { state.value = State.Start }) {
        Text(text = this@MainActivity.getString(R.string.return_to_main_screen))
    }
}
*/
