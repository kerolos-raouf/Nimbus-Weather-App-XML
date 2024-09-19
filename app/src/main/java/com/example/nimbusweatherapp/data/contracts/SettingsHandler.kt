package com.example.nimbusweatherapp.data.contracts

interface SettingsHandler
{

    fun setSharedPreferencesString(stringKey : String,stringValue : String)

    fun getSharedPreferencesString(languageKey : String) : String

    fun setSharedPreferencesBoolean(booleanKey : String,isTrue : Boolean)

    fun getSharedPreferencesBoolean(booleanKey : String) : Boolean


}