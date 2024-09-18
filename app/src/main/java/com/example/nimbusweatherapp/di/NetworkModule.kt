package com.example.nimbusweatherapp.di

import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.network.NetworkApi
import com.example.nimbusweatherapp.data.network.NetworkHandler
import com.example.nimbusweatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule
{
    @Provides
    @Singleton
    fun provideNetworkApi(): NetworkApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkHandler(networkApi: NetworkApi): RemoteDataSource = NetworkHandler(networkApi)

}