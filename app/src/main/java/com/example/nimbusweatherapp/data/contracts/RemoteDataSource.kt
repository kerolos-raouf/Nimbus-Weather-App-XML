package com.example.nimbusweatherapp.data.contracts

import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHoursList
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import retrofit2.Response

interface RemoteDataSource
{
    suspend fun getWeatherEveryThreeHours(latitude: Double, longitude: Double): Response<WeatherEveryThreeHoursList>

    suspend fun getWeatherForLocation(latitude: Double, longitude: Double): Response<WeatherForLocation>
}