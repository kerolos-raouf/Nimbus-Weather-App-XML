package com.example.nimbusweatherapp.data.sharedPreference

import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.utils.Constants

class FakeSharedPreferenceHandler : SettingsHandler {

    private val settingsMap = mutableMapOf<String, Any>()
    init {
        settingsMap[Constants.NOTIFICATION_KEY] = true
    }

    override fun setSharedPreferencesString(stringKey: String, stringValue: String) {
        settingsMap[stringKey] = stringValue
    }

    override fun getSharedPreferencesString(languageKey: String): String {
        return settingsMap.getOrDefault(languageKey,"") as String
    }

    override fun setSharedPreferencesBoolean(booleanKey: String, isTrue: Boolean) {
        settingsMap[booleanKey] = isTrue
    }

    override fun getSharedPreferencesBoolean(booleanKey: String): Boolean {
        return settingsMap.getOrDefault(booleanKey,false) as Boolean
    }
}