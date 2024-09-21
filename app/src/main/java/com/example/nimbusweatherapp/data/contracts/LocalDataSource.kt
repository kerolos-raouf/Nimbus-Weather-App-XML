package com.example.nimbusweatherapp.data.contracts

import androidx.lifecycle.LiveData
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllLocations() : Flow<List<FavouriteLocation>>

    suspend fun insertLocation(favouriteLocation: FavouriteLocation)

    suspend fun deleteLocation(favouriteLocation: FavouriteLocation)

}