package com.example.nimbusweatherapp.data.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WeatherItemEveryThreeHours(
    @SerializedName("clouds")
    @Embedded
    val clouds: Clouds,
    @SerializedName("dt")
    @PrimaryKey
    val dt: Int,
    @SerializedName("dt_txt")
    val textDataTime: String,
    @SerializedName("main")
    @Embedded
    val main: Main,
    @SerializedName("pop")
    val pop: Double,

    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    @Embedded
    val wind: Wind
)