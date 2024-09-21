package com.example.nimbusweatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nimbusweatherapp.data.model.FavouriteLocation

@Database(entities = [FavouriteLocation::class], version = 1)
abstract class LocationDatabase : RoomDatabase()
{
    abstract fun favouriteLocationDao() : FavouriteLocationDao
}