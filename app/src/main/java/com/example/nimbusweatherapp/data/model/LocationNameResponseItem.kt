package com.example.nimbusweatherapp.data.model


import com.google.gson.annotations.SerializedName

data class LocationNameResponseItem(
    @SerializedName("country")
    val country: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("local_names")
    val localNames: LocalNames?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("state")
    val state: String?
)