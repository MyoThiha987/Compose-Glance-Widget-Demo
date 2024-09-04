package com.myothiha.composeglancewidgetdemo.data

import com.myothiha.composeglancewidgetdemo.domain.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */

@Serializable
data class WeatherDto(
    @SerialName(value = "location")
    val location: LocationDto,
    @SerialName(value = "current")
    val current: CurrentDto

) {

    @Serializable
    data class LocationDto(
        @SerialName(value = "name")
        val name: String
    )

    @Serializable
    data class CurrentDto(
        @SerialName(value = "temp_c")
        val temp_c: Double,
        @SerialName(value = "condition")
        val condition: ConditionDto,
        @SerialName(value = "wind_mph")
        val windSpeed: Double,
        @SerialName("humidity")
        val humidity: Int,
        @SerialName("precip_mm")
        val precipitation: Double
    ) {
        @Serializable
        data class ConditionDto(
            @SerialName("text")
            val text: String,
            @SerialName("code")
            val code: Int
        )
    }

    fun mapToModel() = Weather(
        code = current.condition.code,
        locationName = location.name,
        temperature = current.temp_c,
        weatherCondition = current.condition.text,
        windSpeed = current.windSpeed,
        humidity = current.humidity,
        preciptation = current.precipitation
    )
}
