package com.example.weatherapp.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.weatherapp.data.model.Units
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitProvider(context: Context) {
    // 1.1 Создание Ретрофита
    // что делать с модификатором видимости? Если нужно private, то объект надо класть внутрь класса.
    // Задача по проверке наличия интернета сводится к тому что непонятно как в RetrofitProvider прокидывать контекст :

    // зачем делается явное приведение к ConnectivityManager ? Да, я понимаю что без приведения переменная имеет тип Any. А почему именно ConnectivityManager ?
    var cm: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



    private val BASE_URL = "https://api.openweathermap.org/"
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

    fun isThereInternetConnection(): Boolean {
        val network = cm?.activeNetwork ?: return false
        val activeNetwork = cm?.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
