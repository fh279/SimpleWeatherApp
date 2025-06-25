package com.example.weatherapp.presentation

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.presentation.theme.DarkColorScheme
import com.example.weatherapp.presentation.theme.LightColorScheme
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import kotlinx.coroutines.launch

sealed class State {
    // состояние по умолчанию
    data object Loading : State()
    data class Success(
        val weather: WeatherResponse
    ) : State()

    data object Error : State()
    data object Nothing : State()

}

class MainActivity : ComponentActivity() {
    // Nothing поставил под кейс когда результат получается по нажатию на кнопку. Это надо сделать. Потом выпадающий список.
    private val state: MutableState<State> = mutableStateOf(State.Nothing)
    val items = Cities.entries
    var currentCity: Cities = Cities.SPB
    // а можно сделать отложенную инициализацию типа вот так? val selectedItem: Cities by lazy { тут не понятно что писать, чем инициализировать }
    // или так - var isLoading by remember { mutableStateOf(false) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NETWORK INFO", RetrofitProvider(this@MainActivity).isThereInternetConnection().toString())
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
                State.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                    )

                State.Error -> println("i chto?..")
                State.Nothing -> {
                    DropDownList()
                    FetchButton()
                }

                is State.Success -> {
                    val weather = (state.value as State.Success).weather
                    Text(
                        text = "Погода в $currentCity: ${weather.main.temp}"
                    )
                    /*Text(
                        // "${result.value?.main?.temp.toString()} ℃", // вот это хорошо, но надо дописать проверку на null что бы не получилось так что бы у нас высветилось "null градусов цельсия".
                        value = */
                    /*вот тут нужна проверка на null*//* String.format(
                            format = this@MainActivity.getString(R.string.smth),
                            args = arrayOf(weather.main.temp.toString())
                        ),
                        onValueChange = {},
                        label = { Text(stringResource(id = R.string.main_temperature_Label)) },
                        placeholder = { Text(stringResource(id = R.string.main_temperature_Label)) },
                        // как уменьшить расстояние между верхним divider'ом и OutlinedTextField'ом?
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(vertical = 0.dp),
                        textStyle = TextStyle(fontSize = 25.sp)
                    )*/
                    // FetchButton()
                    ReturnToMainButton()
                }
            }


            /*Button(
                onClick = {
                    lifecycleScope.launch {
                        try {
                            state.value = RetrofitProvider.fetchWeather(
                                city = Cities.SPB,
                                units = Units.METRIC.value
                            )
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
                // вынести в ресурсы
                Text("Запросить погоду")
            }*/

            /*Text(
                text = if (isDarkTheme) "Включено" else "Выключено",
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isChecked -> isDarkTheme = isChecked }
            )*/
        }
    }

    @Composable
    fun FetchButton() {
        Button(
            onClick = {
                state.value = State.Loading
                lifecycleScope.launch {
                    try {
                        RetrofitProvider(this@MainActivity).fetchWeather(
                            city =  currentCity /*Cities.SPB*/,
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
            // вынести в ресурсы
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
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
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
        Button(onClick = { state.value = State.Nothing }) {
            Text(text = this@MainActivity.getString(R.string.return_to_main_screen))
        }
    }
}
// как задать кастомный цвет Dovoder'у?