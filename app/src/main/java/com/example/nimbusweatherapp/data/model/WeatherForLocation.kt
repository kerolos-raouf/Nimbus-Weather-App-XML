package com.example.nimbusweatherapp.data.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WeatherForLocation(
    @SerializedName("clouds")
    @Embedded
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Int,
    @SerializedName("coord")
    @Embedded
    val coord: Coord,
    @SerializedName("dt")
    @PrimaryKey
    val date: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    @Embedded
    val main: Main,
    @SerializedName("name")
    var name: String,
    @SerializedName("sys")
    @Embedded
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    @Embedded
    val wind: Wind
)