package com.myothiha.composeglancewidgetdemo.domain

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */
interface WeatherRepository {
    suspend fun fetchCurrentCityWeather(city : String): Weather
}