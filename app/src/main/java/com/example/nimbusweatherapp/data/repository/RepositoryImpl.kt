package com.example.nimbusweatherapp.data.repository

import android.util.Log
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.CityForSearchItem
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.utils.Constants
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
        units: String,
        saveLocally: Boolean
    ): Flow<State<WeatherEveryThreeHours>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherEveryThreeHours(latitude, longitude,language,units)

            val hourlyListResponse = response.body()?.list
            if (response.isSuccessful && response.body() != null) {

                if(saveLocally)
                {
                    //refresh hourly data
                    hourlyListResponse?.let {
                        localDataSource.refreshWeatherItemEveryThreeHours(it)
                    }
                }

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
        units: String,
        saveLocally: Boolean
    ): Flow<State<WeatherForLocation>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getWeatherForLocation(latitude, longitude,language,units)
            val nameResponse = remoteDataSource.getLocationInfoUsingCoordinates(latitude, longitude)

            var name = nameResponse.body()?.get(0)?.name ?: ""


            var weatherForLocation = response.body()


            if(getSharedPreferencesString(Constants.LANGUAGE_KEY) == Constants.ARABIC_LANGUAGE)
            {
                name = nameResponse.body()?.get(0)?.localNames?.ar ?: nameResponse.body()?.get(0)?.name ?: "not found"
            }


            if (response.isSuccessful && response.body() != null
                && nameResponse.isSuccessful && nameResponse.body() != null
                && weatherForLocation != null
                ) {
                weatherForLocation = weatherForLocation.copy(name = name)
                if(saveLocally)
                {
                    //REFRESH data
                    localDataSource.refreshWeatherForLocation(weatherForLocation)
                }

                emit(State.Success(weatherForLocation))
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
    override fun getLocationInfoUsingCoordinates(
        latitude: Double,
        longitude: Double
    ): Flow<State<LocationNameResponse>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getLocationInfoUsingCoordinates(latitude, longitude)
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
    override fun getLocationInfoUsingName(locationName: String): Flow<State<LocationNameResponse>> = flow {
        emit(State.Loading)
        try {
            val response = remoteDataSource.getLocationInfoUsingName(locationName)
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

    ////local data source
    override fun getAllLocations(): Flow<List<FavouriteLocation>> = localDataSource.getAllLocations()

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.insertLocation(favouriteLocation)
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.deleteLocation(favouriteLocation)
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        localDataSource.deleteAlert(alert)
    }

    ////weather for location
    override fun getWeatherForLocationFromLocal(): Flow<List<WeatherForLocation>> {
        return localDataSource.getWeatherForLocation()
    }

    override suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation) {
        localDataSource.insertWeatherForLocation(weatherForLocation)
    }

    override suspend fun deleteWeatherForLocation() {
        localDataSource.deleteWeatherForLocation()
    }

    override suspend fun refreshWeatherForLocation(weatherForLocation: WeatherForLocation) {
        localDataSource.refreshWeatherForLocation(weatherForLocation)
    }

    override suspend fun getWeatherForLocationCount(): Int {
        return localDataSource.getWeatherForLocationCount()
    }

    override fun getWeatherItemEveryThreeHoursFromLocal(): Flow<List<WeatherItemEveryThreeHours>> {
        return localDataSource.getWeatherItemEveryThreeHours()
    }


    override suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>) {
        localDataSource.insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList)
    }

    override suspend fun deleteAllWeatherItemEveryThreeHours() {
        localDataSource.deleteAllWeatherItemEveryThreeHours()
    }

    override suspend fun refreshWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList: List<WeatherItemEveryThreeHours>) {
        localDataSource.refreshWeatherItemEveryThreeHours(weatherItemEveryThreeHoursList)
    }

    //cities for search
    @OptIn(FlowPreview::class)
    override fun getCitiesForSearch(name: String): Flow<State<List<CityForSearchItem>>> = flow {
        emit(State.Loading)
        try {

            val citiesResponse = remoteDataSource.getCitiesListForSearch(name)
            if(citiesResponse.isSuccessful && citiesResponse.body() != null) {
                emit(State.Success(citiesResponse.body()!!))
            }else
            {
                emit(State.Error(citiesResponse.message().orEmpty()))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.orEmpty()))
        }
    }.timeout(10.seconds).catch {
        emit(State.Error("Time out"))
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