package com.example.nimbusweatherapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.sharedPreference.SharedPreferenceHandler
import com.example.nimbusweatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }



}