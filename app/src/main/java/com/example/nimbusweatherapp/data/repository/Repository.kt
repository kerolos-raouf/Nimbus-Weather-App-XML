package com.example.nimbusweatherapp.data.repository


import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
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


    fun setSharedPreferencesString(stringKey : String,stringValue : String)
    fun getSharedPreferencesString(stringKey : String) : String

    fun setSharedPreferencesBoolean(booleanKey : String,isTrue : Boolean)
    fun getSharedPreferencesBoolean(booleanKey : String) : Boolean

}