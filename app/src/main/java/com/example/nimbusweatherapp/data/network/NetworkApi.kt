package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.BuildConfig
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHoursList
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkApi
{

    @GET("forecast")
    suspend fun getWeatherEveryThreeHours(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "standard",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<WeatherEveryThreeHoursList>

    @GET("weather")
    suspend fun getWeatherForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "standard",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<WeatherForLocation>
}