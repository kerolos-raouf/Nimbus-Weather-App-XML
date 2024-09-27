package com.example.nimbusweatherapp.data.repository


import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.utils.State
import kotlinx.coroutines.flow.Flow


interface Repository
{

    //remote data source
    fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ) : Flow<State<WeatherEveryThreeHours>>

    fun getWeatherForLocation(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ) : Flow<State<WeatherForLocation>>

    fun getWeatherByCountryName(
        countryName : String,
        language: String,
        units: String
    ) : Flow<State<WeatherForLocation>>


    fun getLocationInfoUsingCoordinates(
        latitude: Double,
        longitude: Double,
    ) : Flow<State<LocationNameResponse>>

    fun getLocationInfoUsingName(
        locationName : String
    ) : Flow<State<LocationNameResponse>>

    ///local data source
    fun getAllLocations() : Flow<List<FavouriteLocation>>

    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)

    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)

    //alert
    fun getAllAlerts() : Flow<List<Alert>>

    suspend fun insertAlert(alert: Alert)

    suspend fun deleteAlert(alert: Alert)

    //weather for location
    fun getWeatherForLocationFromLocal() : Flow<List<WeatherForLocation>>
    suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation)
    suspend fun deleteWeatherForLocation()
    suspend fun refreshWeatherForLocation(weatherForLocation: WeatherForLocation)
    suspend fun getWeatherForLocationCount() : Int

    //weather item every three hours
    fun getWeatherItemEveryThreeHoursFromLocal() : Flow<List<WeatherItemEveryThreeHours>>
    suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>)
    suspend fun deleteAllWeatherItemEveryThreeHours()
    suspend fun refreshWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>)


    //shared preferences
    fun setSharedPreferencesString(stringKey : String,stringValue : String)
    fun getSharedPreferencesString(stringKey : String) : String

    fun setSharedPreferencesBoolean(booleanKey : String,isTrue : Boolean)
    fun getSharedPreferencesBoolean(booleanKey : String) : Boolean

}