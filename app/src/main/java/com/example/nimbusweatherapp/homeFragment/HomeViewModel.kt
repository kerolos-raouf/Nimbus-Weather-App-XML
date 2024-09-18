package com.example.nimbusweatherapp.homeFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository

): ViewModel()
{


    private val _weatherEveryThreeHours = MutableLiveData<List<WeatherEveryThreeHours>>(emptyList())
    val weatherEveryThreeHours : LiveData<List<WeatherEveryThreeHours>> = _weatherEveryThreeHours

    private val _weatherForLocation = MutableLiveData<WeatherForLocation>()
    private val weatherForLocation : LiveData<WeatherForLocation> = _weatherForLocation

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading



    fun getWeatherEveryThreeHours(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.getWeatherEveryThreeHours(latitude, longitude).collect{
                when(it)
                {
                    is State.Error -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Error ${it.message}")
                        _error.value = it.message
                        _loading.value = false
                    }
                    State.Loading -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Loading")
                        _loading.value = true
                    }
                    is State.Success -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Success${it.data.size}")
                        _weatherEveryThreeHours.value = it.data
                        _loading.value = false
                    }
                }
            }
        }
    }


    fun getWeatherForLocation(latitude: Double, longitude: Double)
    {
        viewModelScope.launch {
            repository.getWeatherForLocation(latitude, longitude).collect{
                when(it)
                {
                    is State.Error -> {
                        Log.d("Kerolos", "getWeatherForLocation: Error ${it.message}")
                        _error.value = it.message
                        _loading.value = false
                    }
                    State.Loading -> {
                        Log.d("Kerolos", "getWeatherForLocation: Loading")
                        _loading.value = true
                    }
                    is State.Success -> {
                        Log.d("Kerolos", "getWeatherForLocation: Success ${it.data}")
                        _weatherForLocation.value = it.data
                        _loading.value = false
                    }
                }
            }
        }
    }
}