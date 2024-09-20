package com.example.nimbusweatherapp.utils

object Constants
{
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    const val SETTINGS_SHARED_PREFERENCE_NAME = "Settings_Shared_Preference"

    //Languages
    const val LANGUAGE_KEY = "language"
    const val ENGLISH_LANGUAGE = "en"
    const val ARABIC_LANGUAGE = "ar"
    const val ENGLISH_SELECTION_VALUE = 0
    const val ARABIC_SELECTION_VALUE = 1


    //Location
    const val LOCATION_KEY = "location"
    const val GPS_LOCATION = "GPS"
    const val MAP_LOCATION = "Map"
    const val GPS_SELECTION_VALUE = 0
    const val MAP_SELECTION_VALUE = 1

    // Temperature
    const val TEMPERATURE_KEY = "temperature"
    const val KELVIN = "Kelvin"
    const val CELSIUS = "Celsius"
    const val FAHRENHEIT = "Fahrenheit"
    const val KELVIN_SELECTION_VALUE = 0
    const val CELSIUS_SELECTION_VALUE = 1
    const val FAHRENHEIT_SELECTION_VALUE = 2


    //Wind Speed
    const val WIND_SPEED_KEY = "windSpeed"
    const val METER_PER_SECOND = "m/s"
    const val KILOMETER_PER_HOUR = "km/h"
    const val METER_PER_SECOND_SELECTION_VALUE = 0
    const val KILOMETER_PER_HOUR_SELECTION_VALUE = 1

    ///Notification
    const val NOTIFICATION_KEY = "notification"

}