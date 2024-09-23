package com.example.nimbusweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.nimbusweatherapp.data.database.dao.FavouriteLocationDao
import com.example.nimbusweatherapp.data.database.LocationDatabase
import com.example.nimbusweatherapp.data.database.dao.AlertsDao
import com.example.nimbusweatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLocationDatabase(@ApplicationContext context: Context) : LocationDatabase
        = Room.databaseBuilder(context,LocationDatabase::class.java,Constants.LOCATION_DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideFavouriteDao(locationDatabase: LocationDatabase) : FavouriteLocationDao = locationDatabase.favouriteLocationDao()

    @Provides
    @Singleton
    fun provideAlertsDao(locationDatabase: LocationDatabase) : AlertsDao = locationDatabase.alertsDao()


}