package com.example.weatherapp.presentation

import androidx.compose.runtime.MutableState
import com.example.weatherapp.R

enum class Cities(
    val cityName: Int,
    val lat: Double?,
    val lon: Double?
) {
    MOSCOW(
        cityName = R.string.Moscow_name,
        lat = 55.7522,
        lon = 37.6156
    ),
    SPB(
        cityName = R.string.SPb_name,
        lat = 59.938233124605226,
        lon = 30.358811825486548,
    ),
    NSK(
        cityName = R.string.Nsk_name,
        lat = 44.6333,
        lon = 41.9444
    ),
    CURRENT_LOCATION(
        cityName = R.string.Current_location_name,
        lat = null,
        lon = null
    )

}