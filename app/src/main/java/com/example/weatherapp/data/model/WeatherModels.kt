package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

// Обязательно обсудить с Русланом!
data class DescriptionWeather(
    @SerializedName("id")
    var id: Int,

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String,
)

data class CoordinateResponse(
    @SerializedName("lon")
    val lon: Float,

    @SerializedName("lat")
    val lat: Float
)

data class MainResponse(
    @SerializedName("temp")
    val temp: Float,

    @SerializedName("feels_like")
    val feelsLike: Float,

    @SerializedName("temp_min")
    val tempMin: Float,

    @SerializedName("temp_max")
    val tempMax: Float,

    @SerializedName("pressure")
    val pressure: Int,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("sea_level")
    val seaLevel: Int,

    @SerializedName("grnd_level")
    val grndLevel: Int
)

data class WindResponse(
    @SerializedName("speed")
    val speed: Float,

    @SerializedName("deg")
    val deg: Int,

    @SerializedName("gust")
    val gust: Float
)

data class RainResponse(val oneHour: Float)

data class CloudsResponse(val all: Int)

data class SysResponse(
    @SerializedName("type")
    val type: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("country")
    val country: String,

    @SerializedName("sunrise")
    val sunrise: Double,

    @SerializedName("sunset")
    val sunset: Double
)
// обрати внимание что часть параметров являются необязательными (дока!). Пока просто к сведению.

data class WeatherResponse(
    @SerializedName("coord")
    val coordinates: CoordinateResponse,

    @SerializedName("weather")
    val weather: List<DescriptionWeather>,

    @SerializedName("base")
    val base: String,

    @SerializedName("main")
    val main: MainResponse,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("wind")
    val wind: WindResponse,

    @SerializedName("rain")
    val rain: RainResponse,

    @SerializedName("clouds")
    val clouds: CloudsResponse,

    @SerializedName("dt")
    val dt: Double,

    @SerializedName("sys")
    val sys: SysResponse,

    @SerializedName("timezone")
    val timezone: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("cod")
    val cod: Int
)

