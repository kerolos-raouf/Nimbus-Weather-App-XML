package com.example.nimbusweatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nimbusweatherapp.data.database.dao.AlertsDao
import com.example.nimbusweatherapp.data.database.dao.FavouriteLocationDao
import com.example.nimbusweatherapp.data.database.dao.WeatherDao
import com.example.nimbusweatherapp.data.database.typeConverter.WeatherTypeConverter
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours

@Database(entities = [FavouriteLocation::class,Alert::class, WeatherForLocation::class,WeatherItemEveryThreeHours::class], version = 1)
@TypeConverters(WeatherTypeConverter::class)
abstract class LocationDatabase : RoomDatabase()
{
    abstract fun favouriteLocationDao() : FavouriteLocationDao
    abstract fun alertsDao() : AlertsDao
    abstract fun weatherDao() : WeatherDao
}