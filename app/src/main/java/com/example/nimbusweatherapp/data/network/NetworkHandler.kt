package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHoursList
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import retrofit2.Response
import javax.inject.Inject

class NetworkHandler @Inject constructor (
    private val networkApi: NetworkApi
): RemoteDataSource
{
    override suspend fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double
    ): Response<WeatherEveryThreeHoursList> {
        return networkApi.getWeatherEveryThreeHours(latitude, longitude)
    }

    override suspend fun getWeatherForLocation(
        latitude: Double,
        longitude: Double
    ): Response<WeatherForLocation> {
        return networkApi.getWeatherForLocation(latitude, longitude)
    }

}