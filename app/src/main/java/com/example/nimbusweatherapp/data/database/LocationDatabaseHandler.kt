package com.example.nimbusweatherapp.data.database

import androidx.lifecycle.LiveData
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationDatabaseHandler @Inject constructor(
    private val favouriteLocationDao: FavouriteLocationDao
) : LocalDataSource{
    override fun getAllLocations(): Flow<List<FavouriteLocation>> {
        return favouriteLocationDao.getAllFavouriteLocations()
    }

    override suspend fun insertLocation(favouriteLocation: FavouriteLocation) {
        favouriteLocationDao.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun deleteLocation(favouriteLocation: FavouriteLocation) {
        favouriteLocationDao.deleteFavouriteLocation(favouriteLocation)
    }
}