package com.example.nimbusweatherapp.data.repository


import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import kotlinx.coroutines.flow.Flow


interface Repository
{

    fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double
    ) : Flow<State<WeatherEveryThreeHours>>

    fun getWeatherForLocation(
        latitude: Double,
        longitude: Double
    ) : Flow<State<WeatherForLocation>>


}