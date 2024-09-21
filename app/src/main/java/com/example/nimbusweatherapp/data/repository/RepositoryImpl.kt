package com.example.nimbusweatherapp.data.repository

import android.util.Log
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    //private val localDataSource: LocalDataSource
    private val settingsHandler: SettingsHandler
): Repository
{
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
    }

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
    }

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