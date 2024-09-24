package com.example.nimbusweatherapp.utils

object Constants
{
    const val BASE_URL = "https://api.openweathermap.org/"

    const val SETTINGS_SHARED_PREFERENCE_NAME = "Settings_Shared_Preference"

    const val LOCATION_DATABASE_NAME = "LOCATION_DATABASE"

    //Languages
    const val LANGUAGE_KEY = "language"
    const val ENGLISH_LANGUAGE = "en"
    const val ARABIC_LANGUAGE = "ar"
    const val ENGLISH_SELECTION_VALUE = 0
    const val ARABIC_SELECTION_VALUE = 1


    //Location
    const val LOCATION_KEY = "location"
    const val GPS_LOCATION = "GPS"
    const val GPS_LOCATION_ARABIC = "الموقع الحالى"
    const val MAP_LOCATION = "Map"
    const val MAP_LOCATION_ARABIC = "الخريطة"
    const val GPS_SELECTION_VALUE = 0
    const val MAP_SELECTION_VALUE = 1

    // Temperature
    const val TEMPERATURE_KEY = "temperature"
    const val KELVIN = "Kelvin"
    const val KELVIN_ARABIC = "كيلفن"
    const val CELSIUS = "Celsius"
    const val CELSIUS_ARABIC = "سيليزيس"
    const val FAHRENHEIT = "Fahrenheit"
    const val FAHRENHEIT_ARABIC = "فيهرنهايت"
    const val KELVIN_SELECTION_VALUE = 0
    const val CELSIUS_SELECTION_VALUE = 1
    const val FAHRENHEIT_SELECTION_VALUE = 2

    //unit system
    const val IMPERIAL = "imperial"
    const val METRIC = "metric"
    const val STANDARD = "standard"


    //Wind Speed
    const val WIND_SPEED_KEY = "windSpeed"
    const val METER_PER_SECOND = "m/s"
    const val METER_PER_SECOND_ARABIC = "متر/ثانية"
    const val KILOMETER_PER_HOUR = "km/h"
    const val KILOMETER_PER_HOUR_ARABIC = "كم/ساعة"
    const val METER_PER_SECOND_SELECTION_VALUE = 0
    const val KILOMETER_PER_HOUR_SELECTION_VALUE = 1

    ///Notification
    const val NOTIFICATION_KEY = "notification"


    ////format
    const val DATE_FORMAT_FOR_HOURLY_WEATHER = "EEEE"
    const val DATE_FORMAT_FOR_HOME_FRAGMENT_WEATHER = "EEEE, dd-MM-yyyy, hh:mm a"
    const val DATE_FORMAT_FOR_ALERT = "EEEE \n dd-MM-yyyy \n hh:mm a"


    //map fragment
    const val MAP_SHARED_PREFERENCE_NAME = "map_shared_preference"


    //value of geo coder
    const val GEOCODER_NOT_LOCATED = "NOT_LOCATED"

    ///constants to control the home fragment
    const val SHOW_CONTENT_LAYOUT = 0
    const val SHOW_NO_INTERNET_LAYOUT = 1
    const val SHOW_PERMISSION_DENIED_LAYOUT = 2

    ///alarm fragment
    const val ALERT_TYPE = "ALERT_TYPE"
    const val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"
    const val NOTIFICATION_CHANNEL_ID = 1
    const val ALERT_ACTION_NOTIFICATION = "com.example.NOTIFICATION_ACTION"
    const val ALERT_ACTION_ALARM = "com.example.ALARM_ACTION"

}