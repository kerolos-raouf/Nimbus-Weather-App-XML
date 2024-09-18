package com.example.nimbusweatherapp.di

import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.data.repository.RepositoryImpl
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
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = RepositoryImpl(remoteDataSource)


}