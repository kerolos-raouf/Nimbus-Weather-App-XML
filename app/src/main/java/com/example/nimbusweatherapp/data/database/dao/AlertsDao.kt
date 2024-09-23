package com.example.nimbusweatherapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nimbusweatherapp.data.model.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {


    @Query("SELECT * FROM Alert")
    fun getAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert)

    @Delete
    suspend fun deleteAlert(alert: Alert)

}