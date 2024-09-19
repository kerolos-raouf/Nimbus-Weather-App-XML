package com.example.nimbusweatherapp.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.data.internetStateObserver.InternetStateObserver
import com.example.nimbusweatherapp.data.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(

) : ViewModel() {

    val showHomeContent = MutableLiveData(false)

    //location information for egypt by default
    val currentLocation = MutableLiveData(Location(30.8025,26.8206))

    val hitTheApiInHomeFragment = MutableLiveData(true)


    ///internet state
    private val _internetState = MutableLiveData<ConnectivityObserver.InternetState>()
    val internetState : LiveData<ConnectivityObserver.InternetState> = _internetState

    fun observeOnInternetState(internetStateObserver: InternetStateObserver)
    {
        viewModelScope.launch {
            internetStateObserver.observer().collect{
                _internetState.value = it
            }
        }
    }

}