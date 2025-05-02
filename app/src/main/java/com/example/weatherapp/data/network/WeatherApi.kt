package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Чем отличается const val от val?
const val API_KEY = "421131f71c7500a8d35943680edd5ae1"

interface WeatherApi {
    // Какая структура у url? Вспоминай. Домен и все такое.
    // На какие составные части она делится?
    // Java dynamics proxy
    // URL connect session
    // Retrofit сам сгенерирует реализацию метода - это прикол библиотеки.
    @GET("data/2.5/weather/")
    suspend fun getCurrentWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("appid")
        appid: String = API_KEY,
        @Query("units")
        units: String = "standard"

    ): WeatherResponse    // это что за Call? - это про многопоточку, нам это пока что не надо.

}
