package com.example.nimbusweatherapp.data.database

import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class FakeDatabaseHandler : LocalDataSource {

    val favouriteLocations = MutableStateFlow(emptyList<FavouriteLocation>())

    override fun getAllLocations(): Flow<List<FavouriteLocation>> {
        return favouriteLocations
    }

    override suspend fun insertLocation(favouriteLocation: FavouriteLocation) {
        val currentList = favouriteLocations.value.toMutableList()
        currentList.add(favouriteLocation)
        favouriteLocations.value = currentList
    }

    override suspend fun deleteLocation(favouriteLocation: FavouriteLocation) {
        val currentList = favouriteLocations.value.toMutableList()
        currentList.remove(favouriteLocation)
        favouriteLocations.value = currentList
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: Alert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alert) {
        TODO("Not yet implemented")
    }

    override fun getWeatherForLocation(): Flow<List<WeatherForLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherForLocation() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshWeatherForLocation(weatherForLocation: WeatherForLocation) {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherForLocationCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getWeatherItemEveryThreeHours(): Flow<List<WeatherItemEveryThreeHours>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWeatherItemEveryThreeHours() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>) {

    }
}