package com.example.nimbusweatherapp.data.contracts

import androidx.lifecycle.LiveData
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllLocations() : Flow<List<FavouriteLocation>>

    suspend fun insertLocation(favouriteLocation: FavouriteLocation)

    suspend fun deleteLocation(favouriteLocation: FavouriteLocation)

    //alert
    fun getAllAlerts() : Flow<List<Alert>>

    suspend fun insertAlert(alert: Alert)

    suspend fun deleteAlert(alert: Alert)

    //weather for location
    fun getWeatherForLocation() : Flow<List<WeatherForLocation>>

    suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation)

    suspend fun deleteWeatherForLocation()

    suspend fun refreshWeatherForLocation(weatherForLocation: WeatherForLocation)

    suspend fun getWeatherForLocationCount() : Int

    //weather item every three hours
    fun getWeatherItemEveryThreeHours() : Flow<List<WeatherItemEveryThreeHours>>

    suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>)

    suspend fun deleteAllWeatherItemEveryThreeHours()

    suspend fun refreshWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>)

}