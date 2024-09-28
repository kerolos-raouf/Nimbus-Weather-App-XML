package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.model.CitiesForSearch
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import retrofit2.Response
import javax.inject.Inject

class NetworkHandler @Inject constructor (
    private val networkApi: NetworkApi,
    private val countrySearchNetworkApi: CountrySearchNetworkApi
): RemoteDataSource
{
    override suspend fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Response<WeatherEveryThreeHours> {
        return networkApi.getWeatherEveryThreeHours(latitude, longitude,language,units)
    }

    override suspend fun getWeatherForLocation(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Response<WeatherForLocation> {
        return networkApi.getWeatherForLocation(latitude, longitude,language,units)
    }

    override suspend fun getLocationInfoUsingCoordinates(
        latitude: Double,
        longitude: Double,
    ): Response<LocationNameResponse> {
        return networkApi.getLocationInfoUsingCoordinates(latitude,longitude)
    }

    override suspend fun getLocationInfoUsingName(locationName: String): Response<LocationNameResponse> {
        return networkApi.getLocationInfoUsingName(locationName)
    }

    override suspend fun getWeatherByCountryName(
        countryName: String,
        language: String,
        units: String
    ): Response<WeatherForLocation> {
        return networkApi.getWeatherByCountryName(countryName)
    }

    override suspend fun getCitiesListForSearch(name: String): Response<CitiesForSearch> {
        return countrySearchNetworkApi.getCountryListForSearch(name)
    }

}