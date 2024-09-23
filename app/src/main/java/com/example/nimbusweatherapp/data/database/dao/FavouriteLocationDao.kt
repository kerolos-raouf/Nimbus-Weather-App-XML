package com.example.nimbusweatherapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteLocationDao {

    @Query("Select * From FavouriteLocation")
    fun getAllFavouriteLocations() : Flow<List<FavouriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)

    @Delete
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)

}