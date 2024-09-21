package com.example.nimbusweatherapp.mapFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){

    private val _weatherForMapLocation = MutableLiveData<WeatherForLocation>()
    val weatherForMapLocation : LiveData<WeatherForLocation> = _weatherForMapLocation


    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    fun getWeatherForMapLocation(
        latitude: Double,
        longitude: Double ,
        language: String,
        units: String
    ){
        viewModelScope.launch {
            repository.getWeatherForLocation(latitude,longitude,language,units).collect{
                when(it)
                {
                    is State.Error ->
                    {
                        _loading.value = false
                        _errorMessage.value = it.message
                    }
                    State.Loading ->
                    {
                        _loading.value = true
                    }
                    is State.Success ->
                    {
                        _weatherForMapLocation.value = it.data
                        _loading.value = false
                    }
                }
            }
        }
    }

}