package com.example.nimbusweatherapp.data.database

import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.database.dao.AlertsDao
import com.example.nimbusweatherapp.data.database.dao.FavouriteLocationDao
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationDatabaseHandler @Inject constructor(
    private val locationDatabase: LocationDatabase
) : LocalDataSource{
    override fun getAllLocations(): Flow<List<FavouriteLocation>> {
        return locationDatabase.favouriteLocationDao().getAllFavouriteLocations()
    }

    override suspend fun insertLocation(favouriteLocation: FavouriteLocation) {
        locationDatabase.favouriteLocationDao().insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun deleteLocation(favouriteLocation: FavouriteLocation) {
        locationDatabase.favouriteLocationDao().deleteFavouriteLocation(favouriteLocation)
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        return locationDatabase.alertsDao().getAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        locationDatabase.alertsDao().insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        locationDatabase.alertsDao().deleteAlert(alert)
    }

    override fun getWeatherForLocation(): Flow<List<WeatherForLocation>> {
        return locationDatabase.weatherDao().getWeatherForLocation()
    }

    override suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation) {
        locationDatabase.weatherDao().insertWeatherForLocation(weatherForLocation)
    }

    override suspend fun deleteWeatherForLocation() {
        locationDatabase.weatherDao().deleteWeatherForLocation()
    }

    override fun getWeatherItemEveryThreeHours(): Flow<List<WeatherItemEveryThreeHours>> {
        return locationDatabase.weatherDao().getWeatherItemEveryThreeHours()
    }


    override suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>) {
        locationDatabase.weatherDao().insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList)
    }


    override suspend fun deleteAllWeatherItemEveryThreeHours() {
        locationDatabase.weatherDao().deleteAllWeatherItemEveryThreeHours()
    }
}