package com.example.weatherapp.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
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
    var connectivityManager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



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
            lat = city.lat ?: 100500.0,
            lon = city.lon ?: 100500.0,
            units = units
        )
    }

    fun isThereInternetConnection(): Boolean {
        /*Проблема. теряюсь в мелочах. надо ставить конкретные атомарные цели и достигать их. Пока так.
        Кажется был вопрос о том что есть подтипы и супертипы, а так же наследники и дочерние классы. Какая в них разница?
        мне что то надо было почитать важное. что?
        Правильно ли понимаю что в Элвиса можно воткнуть return false потому что return возвращает Nothing, а Noghing это подтип всех типов.
        уточнить определение Сервис в Андроиде
        Уточнить понятие Manager (например, ConnectivityManager) в Android.
        */
        val network: Network = connectivityManager?.activeNetwork ?: return false
        val activeNetwork = connectivityManager?.getNetworkCapabilities(network) ?: return false
        // pattern matching - when реализует эту штуку. В Kotlin этого нет, но when это костыльная реализация pattern matching'а.
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
