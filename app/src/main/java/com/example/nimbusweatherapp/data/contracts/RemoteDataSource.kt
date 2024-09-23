package com.example.nimbusweatherapp.data.contracts

import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import retrofit2.Response

interface RemoteDataSource
{
    suspend fun getWeatherEveryThreeHours(latitude: Double,
                                          longitude: Double,
                                          language: String,
                                          units: String): Response<WeatherEveryThreeHours>

    suspend fun getWeatherForLocation(latitude: Double,
                                      longitude: Double,
                                      language: String,
                                      units: String): Response<WeatherForLocation>

    suspend fun getLocationInfoUsingCoordinates(
        latitude: Double,
        longitude: Double,
    ) : Response<LocationNameResponse>

    suspend fun getLocationInfoUsingName(
        locationName : String
    ) : Response<LocationNameResponse>

    suspend fun getWeatherByCountryName(
       countryName : String,
       language: String,
       units: String ,
    ) : Response<WeatherForLocation>
}