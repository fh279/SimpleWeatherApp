package com.example.weatherapp.presentation.model

object MainScreenTags {
    val root = "MainScreen"
    val dropdownMenu = "$root.dropdownMenu"

    fun dropdownMenuItem(city: String) = "$root.dropdownMenuItem.$city"
}