package com.example.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.network.WeatherApi
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/"

class MainActivity : ComponentActivity() {
    private var textViewState: MutableState<String> = mutableStateOf("")
    // 1.1 Создание Ретрофита
    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchWeather(
            lat = 59.938233124605226,
            lon = 30.358811825486548,
            apiKey = "421131f71c7500a8d35943680edd5ae1",
            units = Units.METRIC.value
        )
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = textViewState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun fetchWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String = Units.METRIC.value
    ) {
        try {
            MainScope().launch {
                // Результат вызова ручки getCurrentWeather.
                val result = service.getCurrentWeather(lat, lon, apiKey, units)
                // изменение состояния значения ответа на запрос().
                textViewState.value = result.toString()
                println(textViewState)
            }
        } catch (_:Exception) {
            println("Не удалось получить ответ")
        }
    }
}

@Composable
fun Greeting(name: MutableState<String>, modifier: Modifier = Modifier) {
    Text(
        text = name.value,
        modifier = modifier
    )
}
