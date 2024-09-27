package com.example.nimbusweatherapp.data.model


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    var description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
)