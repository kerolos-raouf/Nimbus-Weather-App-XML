package com.example.nimbusweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.database.FavouriteLocationDao
import com.example.nimbusweatherapp.data.database.LocationDatabase
import com.example.nimbusweatherapp.data.database.LocationDatabaseHandler
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
    fun provideFavouriteDao(@ApplicationContext context: Context) : FavouriteLocationDao
        = Room.databaseBuilder(context,LocationDatabase::class.java,Constants.LOCATION_DATABASE_NAME)
            .build()
            .favouriteLocationDao()


}