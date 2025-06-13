package com.example.weatherapp.presentation

import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitProvider {
    // 1.1 Создание Ретрофита
    // что делать с модификатором видимости? Если нужно private, то объект надо класть внутрь класса.
    // Задача по проверке наличия интернета сводится к тому что непонятно как в RetrofitProvider прокидывать контекст :
        // var cm: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE)

    private const val BASE_URL = "https://api.openweathermap.org/"
    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    internal suspend fun fetchWeather(
        city: Cities,
        units: String = Units.METRIC.value
    ): WeatherResponse {
        return service.getCurrentWeather(
            lat = city.lat,
            lon = city.lon,
            units = units
        )
    }
}