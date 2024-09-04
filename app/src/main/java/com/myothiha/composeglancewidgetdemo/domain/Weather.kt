package com.myothiha.composeglancewidgetdemo.domain

import kotlinx.serialization.Serializable

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */

@Serializable
data class Weather(
    val code : Int,
    val locationName: String,
    val temperature: Double,
    val weatherCondition: String,
    val humidity: Int,
    val windSpeed: Double,
    val preciptation: Double
)

@Serializable
sealed interface WeatherUIState {
    @Serializable
    object Loading : WeatherUIState

    @Serializable
    data class Success(val data: Weather) : WeatherUIState

    @Serializable
    data class Error(val message: String) : WeatherUIState
}