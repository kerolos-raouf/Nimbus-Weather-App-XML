package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.BuildConfig
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi
{

    @GET("data/2.5/forecast")
    suspend fun getWeatherEveryThreeHours(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "standard",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<WeatherEveryThreeHours>

    @GET("data/2.5/weather")
    suspend fun getWeatherForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "standard",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<WeatherForLocation>

    @GET("data/2.5/weather")
    suspend fun getWeatherByCountryName(
        @Query("q") countryName : String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<WeatherForLocation>

    @GET("geo/1.0/reverse")
    suspend fun getLocationInfoUsingCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<LocationNameResponse>

    @GET("geo/1.0/reverse")
    suspend fun getLocationInfoUsingName(
        @Query("q") locationName: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ) : Response<LocationNameResponse>


}