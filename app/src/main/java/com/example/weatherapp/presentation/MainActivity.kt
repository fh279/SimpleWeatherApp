package com.example.weatherapp.presentation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

private const val BASE_URL = "https://api.openweathermap.org/"

class MainActivity : ComponentActivity() {

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var textViewState: MutableState<String> = mutableStateOf("")
    // 1.1 Создание Ретрофита
    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*fetchWeather(
            lat = 59.938233124605226,
            lon = 30.358811825486548,
            apiKey = "421131f71c7500a8d35943680edd5ae1",
            units = Units.METRIC.value
        )*/
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = textViewState,
                        modifier = Modifier.padding(innerPadding)
                    )
                    Text(
                        text = LaunchedEffect(true) {
                            /*val result = scope.*/async {
                            fetchWeather(
                                lat = 59.938233124605226,
                                lon = 30.358811825486548,
                                apiKey = "421131f71c7500a8d35943680edd5ae1",
                                units = Units.METRIC.value
                            )
                        }.await().main.temp.toString()
                    },
                        modifier = Modifier
                    )
                }

                }
            }
        }

    suspend private fun fetchWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String = Units.METRIC.value
    ): WeatherResponse {
        return service.getCurrentWeather(lat, lon, apiKey, units)
    }
}




    @Composable
    fun Greeting(name: MutableState<String>, modifier: Modifier = Modifier) {
        Text(
            text = name.value,
            modifier = modifier
        )
    }
