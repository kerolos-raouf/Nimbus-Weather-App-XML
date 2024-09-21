package com.example.nimbusweatherapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.timeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val settingsHandler: SettingsHandler
): Repository
{
    //remote data source
    @OptIn(FlowPreview::class)
    override fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Flow<State<WeatherEveryThreeHours>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherEveryThreeHours(latitude, longitude,language,units)
            if (response.isSuccessful && response.body() != null) {
                emit(State.Success(response.body()!!))
            } else {
                emit(State.Error(response.message().orEmpty()))
            }
        } catch (e: Exception) {
            Log.d("Kerolos", "getWeatherEveryThreeHours: Catch ${e.message}")
            emit(State.Error(e.message.orEmpty()))
        }
    }.timeout(10.seconds).catch {
        emit(State.Error("Time out"))
    }

    @OptIn(FlowPreview::class)
    override fun getWeatherForLocation(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Flow<State<WeatherForLocation>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherForLocation(latitude, longitude,language,units)
            if (response.isSuccessful && response.body() != null) {
                emit(State.Success(response.body()!!))
            } else {
                emit(State.Error(response.message().orEmpty()))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.orEmpty()))
        }
    }.timeout(10.seconds).catch {
        emit(State.Error("Time out"))
    }

    @OptIn(FlowPreview::class)
    override fun getWeatherByCountryName(
        countryName: String,
        language: String,
        units: String
    ): Flow<State<WeatherForLocation>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherByCountryName(countryName,language,units)
            if (response.isSuccessful && response.body() != null) {
                Log.d("Kerolos", "getWeatherByCountryName: Success" )
                emit(State.Success(response.body()!!))
            } else {
                Log.d("Kerolos", "getWeatherByCountryName: fail" )
                emit(State.Error(response.message().orEmpty()))
            }
        } catch (e: Exception) {
            Log.d("Kerolos", "getWeatherByCountryName: catch ${e.message}" )
            emit(State.Error(e.message.orEmpty()))
        }
    }.timeout(10.seconds).catch {
        emit(State.Error("Time out"))
    }

    ////local data source
    override fun getAllLocations(): Flow<List<FavouriteLocation>> = localDataSource.getAllLocations()

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.insertLocation(favouriteLocation)
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.deleteLocation(favouriteLocation)
    }

    //shared pref
    override fun setSharedPreferencesString(stringKey: String, stringValue: String) {
        settingsHandler.setSharedPreferencesString(stringKey, stringValue)
    }

    override fun getSharedPreferencesString(stringKey: String): String {
        return settingsHandler.getSharedPreferencesString(stringKey)
    }

    override fun setSharedPreferencesBoolean(booleanKey: String, isTrue: Boolean) {
        settingsHandler.setSharedPreferencesBoolean(booleanKey, isTrue)
    }

    override fun getSharedPreferencesBoolean(booleanKey: String): Boolean {
        return settingsHandler.getSharedPreferencesBoolean(booleanKey)
    }

}