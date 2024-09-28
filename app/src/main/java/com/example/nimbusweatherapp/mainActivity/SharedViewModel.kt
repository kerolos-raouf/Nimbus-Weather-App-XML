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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {


    ///settings variables
    val settingsLanguage = MutableLiveData(Constants.ENGLISH_SELECTION_VALUE)

    val settingsLocation = MutableLiveData(Constants.GPS_SELECTION_VALUE)

    val settingsWindSpeed = MutableLiveData(Constants.METER_PER_SECOND_SELECTION_VALUE)

    val settingsTemperature = MutableLiveData(Constants.KELVIN_SELECTION_VALUE)

    val settingsNotifications = MutableLiveData(true)

    /////

    val showHomeContent = MutableLiveData(Constants.SHOW_PERMISSION_DENIED_LAYOUT)

    //location information for egypt by default
    val currentLocation = MutableStateFlow(Location(30.8025,26.8206))
    val getTheLocationAgain = MutableLiveData(false)
    val getTheLocationForMap = MutableStateFlow(false)



    ///internet state
    private val _internetState = MutableStateFlow(ConnectivityObserver.InternetState.Unavailable)
    val internetState : StateFlow<ConnectivityObserver.InternetState> = _internetState

    init {
        observeOnInternetState()
    }

    private fun observeOnInternetState()
    {
        viewModelScope.launch(Dispatchers.IO) {
            connectivityObserver.observer().collect{
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