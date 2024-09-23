package com.example.nimbusweatherapp.data.database

import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.database.dao.AlertsDao
import com.example.nimbusweatherapp.data.database.dao.FavouriteLocationDao
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationDatabaseHandler @Inject constructor(
    private val favouriteLocationDao: FavouriteLocationDao,
    private val alertDao: AlertsDao
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

    override fun getAllAlerts(): Flow<List<Alert>> {
        return alertDao.getAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        alertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertDao.deleteAlert(alert)
    }
}