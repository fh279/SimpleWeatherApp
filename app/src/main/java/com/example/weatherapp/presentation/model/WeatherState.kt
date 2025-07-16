package com.example.weatherapp.presentation.model

import androidx.compose.material3.surfaceColorAtElevation
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.presentation.Cities

sealed class WeatherState {
    data object Start : WeatherState()
    data object Loading : WeatherState()
    data class Success(val weather: WeatherResponse) : WeatherState()
    data object Error : WeatherState()
}

// Это мы тут тренировались с data class'ами.
data class DataWeatherState(
    val isStart: Boolean,
    val isLoading: Boolean,
    val isError: Boolean,
    val weatherResponse: WeatherResponse?,
    val selectedCity: Cities
) {
    companion object {
        fun initialized(): DataWeatherState {
            return DataWeatherState(
                isStart = true,
                isLoading = false,
                isError = false,
                weatherResponse = null,
                selectedCity = Cities.SPB
            )
        }
    }
}

class WeatherState2(
    private val isStart: Boolean = true,
    private val isLoading: Boolean = false,
    private val isError: Boolean = false,
    private val weatherResponse: WeatherResponse? = null,
    private val selectedCity: Cities = Cities.SPB
) {
    fun copy(
        isStart: Boolean = this.isStart,
        isLoading: Boolean = this.isLoading,
        isError: Boolean = this.isError,
        weatherResponse: WeatherResponse? = this.weatherResponse,
        selectedCity: Cities = this.selectedCity
    ): WeatherState2 {
        return WeatherState2(
            isStart = isStart,
            isLoading = isLoading,
            isError = isError,
            weatherResponse = weatherResponse,
            selectedCity = selectedCity
            )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherState2

        if (isStart != other.isStart) return false
        if (isLoading != other.isLoading) return false
        if (isError != other.isError) return false
        if (weatherResponse != other.weatherResponse) return false
        if (selectedCity != other.selectedCity) return false

        return true
    }

    // Это вообще что?.....
    override fun hashCode(): Int {
        var result = isStart.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isError.hashCode()
        result = 31 * result + (weatherResponse?.hashCode() ?: 0)
        result = 31 * result + selectedCity.hashCode()
        return result
    }
}

val state: DataWeatherState = DataWeatherState.initialized()

fun main() {
    val newState = state.copy()
    val newState2 = WeatherState2().copy(isLoading = true)
}
