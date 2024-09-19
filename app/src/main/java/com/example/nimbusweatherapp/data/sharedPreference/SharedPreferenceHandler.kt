package com.example.nimbusweatherapp.data.sharedPreference

import android.content.SharedPreferences
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import javax.inject.Inject

class SharedPreferenceHandler @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsHandler {
    override fun setSharedPreferencesString(stringKey: String, stringValue: String) {
        sharedPreferences.edit().putString(stringKey, stringValue).apply()
    }

    override fun getSharedPreferencesString(languageKey: String): String {
        return sharedPreferences.getString(languageKey, "") ?: ""
    }

    override fun setSharedPreferencesBoolean(booleanKey: String, isTrue: Boolean) {
        sharedPreferences.edit().putBoolean(booleanKey, isTrue).apply()
    }

    override fun getSharedPreferencesBoolean(booleanKey: String): Boolean {
        return sharedPreferences.getBoolean(booleanKey, true)
    }


}