package com.example.weatherapp.presentation

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/"

class MainActivity : ComponentActivity() {
    private val context = this@MainActivity
    private val result: MutableState<WeatherResponse?> = mutableStateOf(null)

    // 1.1 Создание Ретрофита
    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherAppTheme {
                LaunchedEffect(Unit) {
                    try {
                        result.value = fetchWeather(
                            lat = 59.938233124605226,
                            lon = 30.358811825486548,
                            apiKey = "421131f71c7500a8d35943680edd5ae1",
                            units = Units.METRIC.value
                        )
                    } catch (e: Exception) {
                        Log.e("ОШИБКО!11", "Achtung! Alarma! Ниже будет стектрейс...")
                        e.printStackTrace()
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = "${result.value?.main?.temp.toString()} ℃",
                                onValueChange = {},
                                label = { Text(context.getString(R.string.main_temperature_Label)) },
                                placeholder = { Text(context.getString(R.string.main_temperature_Label)) },
                                modifier = Modifier.padding(innerPadding),
                                textStyle = TextStyle(fontSize = 25.sp)
                            )
                        }
                }
                }
            }
        }

    private suspend fun fetchWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String = Units.METRIC.value
    ): WeatherResponse {
        return service.getCurrentWeather(lat, lon, apiKey, units)
    }
}
