package com.example.nimbusweatherapp.data.model


import com.google.gson.annotations.SerializedName

data class WeatherEveryThreeHours(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<WeatherItemEveryThreeHours>,
    @SerializedName("message")
    val message: Int
)