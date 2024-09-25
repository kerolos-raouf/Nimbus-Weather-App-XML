package com.example.nimbusweatherapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    //weather for location
    @Query("SELECT * FROM WeatherForLocation")
    fun getWeatherForLocation(): Flow<List<WeatherForLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeatherForLocation(weatherForLocation: WeatherForLocation)

    @Query("DELETE FROM WeatherForLocation")
    suspend fun deleteWeatherForLocation()


    //weather item every three hours
    @Query("SELECT * FROM WeatherItemEveryThreeHours")
    fun getWeatherItemEveryThreeHours(): Flow<List<WeatherItemEveryThreeHours>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllWeatherItemEveryThreeHours(weatherItemEveryThreeHours: List<WeatherItemEveryThreeHours>)

    @Query("DELETE FROM WeatherItemEveryThreeHours")
    suspend fun deleteAllWeatherItemEveryThreeHours()


}