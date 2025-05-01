package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Чем отличается const val от val?
const val API_KEY = "421131f71c7500a8d35943680edd5ae1"

interface WeatherApi {
    // Какая структура у url? Вспоминай. Домен и все такое.
    // На какие составные части она делится?
    @GET("data/2.5/weather/")
    fun getWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("appid")
        appid: String = API_KEY,

    ): WeatherResponse

}

/*
Parameters
lat	required	Latitude. If you need the geocoder to automatic convert city names and zip-codes to geo coordinates and the other way around, please use our Geocoding API
lon	required	Longitude. If you need the geocoder to automatic convert city names and zip-codes to geo coordinates and the other way around, please use our Geocoding API
appid	required	Your unique API key (you can always find it on your account page under the "API key" tab)
mode	optional	Response format. Possible values are xml and html. If you don't use the mode parameter format is JSON by default. Learn more
units	optional	Units of measurement. standard, metric and imperial units are available. If you do not use the units parameter, standard units will be applied by default.
Learn more
lang	optional	You can use this parameter to get the output in your language. Learn more
* */

/*
https://api.openweathermap.org
/data
/2.5
/weather? lat={lat} & lon={lon} & appid={API key}
*/