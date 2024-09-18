package com.example.nimbusweatherapp.data.model


import com.google.gson.annotations.SerializedName

data class WeatherItemEveryThreeHours(
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("dt_txt")
    val textDataTime: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("pop")
    val pop: Int,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)