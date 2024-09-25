package com.example.nimbusweatherapp.data.database.typeConverter

import androidx.room.TypeConverter
import com.example.nimbusweatherapp.data.model.Weather
import com.google.gson.Gson

class WeatherTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
        return gson.toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(weatherListString: String): List<Weather> {
        return gson.fromJson(weatherListString, Array<Weather>::class.java).toList()
    }




}