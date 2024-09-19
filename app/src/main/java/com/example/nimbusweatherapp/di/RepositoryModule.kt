package com.example.nimbusweatherapp.di

import android.content.SharedPreferences
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.data.repository.RepositoryImpl
import com.example.nimbusweatherapp.data.sharedPreference.SharedPreferenceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
{

    @Provides
    @Singleton
    fun provideSharedPreferenceHandler(sharedPreferences: SharedPreferences) : SettingsHandler =
        SharedPreferenceHandler(sharedPreferences)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource,settingsHandler: SettingsHandler): Repository =
        RepositoryImpl(remoteDataSource,settingsHandler)


}