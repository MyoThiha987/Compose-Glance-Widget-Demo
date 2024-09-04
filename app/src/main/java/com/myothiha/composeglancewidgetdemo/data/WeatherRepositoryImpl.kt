package com.myothiha.composeglancewidgetdemo.data

import com.myothiha.composeglancewidgetdemo.domain.Weather
import com.myothiha.composeglancewidgetdemo.domain.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */
class WeatherRepositoryImpl @Inject constructor(private val client: HttpClient) :
    WeatherRepository {
    override suspend fun fetchCurrentCityWeather(city: String): Weather {
        val raw = client.get {
            url("https://api.weatherapi.com/v1/current.json")
            parameter("key", "8474ad46e7914dadb44112144240209")
            parameter("q", city)
        }.body<WeatherDto>()

        return raw.mapToModel()
    }
}