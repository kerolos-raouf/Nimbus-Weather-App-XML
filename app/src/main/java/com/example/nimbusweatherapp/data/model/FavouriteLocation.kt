package com.example.nimbusweatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteLocation(
    @PrimaryKey
    val locationName : String,
    val latitude : Double,
    val longitude : Double
)
