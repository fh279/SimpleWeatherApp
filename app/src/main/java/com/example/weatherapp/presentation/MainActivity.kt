package com.example.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.presentation.theme.DarkColorScheme
import com.example.weatherapp.presentation.theme.LightColorScheme
import com.example.weatherapp.presentation.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    private val result: MutableState<WeatherResponse?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                LaunchedEffect(Unit) {
                    try {
                        result.value = RetrofitProvider.fetchWeather(
                            city = Cities.SPB,
                            units = Units.METRIC.value
                        )
                    } catch (e: Exception) {
                        Log.e(
                            "Alarma!!!",
                            this@MainActivity.getString(R.string.request_error))
                        e.printStackTrace()
                    }
                }
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
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                // "${result.value?.main?.temp.toString()} ℃", // вот это хорошо, но надо дописать проверку на null что бы не получилось так что бы у нас высветилось "null градусов цельсия".
                value = /*вот тут нужна проверка на null*/ String.format(
                    format = this@MainActivity.getString(R.string.smth),
                    args = arrayOf(result.value?.main?.temp.toString())

                ),
                onValueChange = {},
                label = {
                    Text(
                        stringResource(
                            id = R.string.main_temperature_Label
                        )
                    )
                },
                placeholder = {
                    Text(
                        stringResource(
                            id = R.string.main_temperature_Label
                        )
                    )
                },
                modifier = Modifier.padding(innerPadding),
                textStyle = TextStyle(fontSize = 25.sp)
            )
            Text(
                text = if (isDarkTheme) "Включено" else "Выключено",
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center
            )


            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isChecked -> isDarkTheme = isChecked }
            )
        }
    }
}
