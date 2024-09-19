package com.example.nimbusweatherapp.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.data.internetStateObserver.InternetStateObserver
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository,
    private val internetStateObserver: InternetStateObserver
) : ViewModel() {


    ///settings variables
    val settingsLanguage = MutableLiveData(0)

    val settingsLocation = MutableLiveData(0)

    val settingsWindSpeed = MutableLiveData(0)

    val settingsTemperature = MutableLiveData(0)

    val settingsNotifications = MutableLiveData(true)

    /////

    val showHomeContent = MutableLiveData(false)

    //location information for egypt by default
    val currentLocation = MutableLiveData(Location(30.8025,26.8206))

    val hitTheApiInHomeFragment = MutableLiveData(true)


    ///internet state
    private val _internetState = MutableLiveData<ConnectivityObserver.InternetState>()
    val internetState : LiveData<ConnectivityObserver.InternetState> = _internetState

    fun observeOnInternetState()
    {
        viewModelScope.launch {
            internetStateObserver.observer().collect{
                _internetState.value = it
            }
        }
    }


    fun setSharedPreferencesString(stringKey: String, stringValue: String) {
        repository.setSharedPreferencesString(stringKey, stringValue)
    }

    fun getSharedPreferencesString(stringKey: String): String {
        return repository.getSharedPreferencesString(stringKey)
    }

    fun setSharedPreferencesBoolean(booleanKey: String, isTrue: Boolean) {
        repository.setSharedPreferencesBoolean(booleanKey, isTrue)
    }

    fun getSharedPreferencesBoolean(booleanKey: String): Boolean {
        return repository.getSharedPreferencesBoolean(booleanKey)
    }

}