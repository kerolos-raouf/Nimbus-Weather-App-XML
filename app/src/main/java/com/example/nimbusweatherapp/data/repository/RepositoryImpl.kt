package com.example.nimbusweatherapp.data.repository

import android.util.Log
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHoursList
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    //private val localDataSource: LocalDataSource
): Repository
{
    override fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double
    ): Flow<State<WeatherEveryThreeHoursList>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherEveryThreeHours(latitude, longitude)
            if (response.isSuccessful && response.body() != null ) {
                emit(State.Success(response.body()!!))
            } else {
                emit(State.Error(response.message().orEmpty()))
            }
        } catch (e: Exception) {
            Log.d("Kerolos", "getWeatherEveryThreeHours: Catch")
            emit(State.Error(e.message.orEmpty()))
        }
    }

    override fun getWeatherForLocation(
        latitude: Double,
        longitude: Double
    ): Flow<State<WeatherForLocation>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherForLocation(latitude, longitude)
            if (response.isSuccessful && response.body() != null) {
                emit(State.Success(response.body()!!))
            } else {
                emit(State.Error(response.message().orEmpty()))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.orEmpty()))
        }
    }

}