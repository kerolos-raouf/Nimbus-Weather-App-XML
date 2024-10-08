package com.example.nimbusweatherapp.mapFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.CityForSearchItem
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){


    private val _weatherForLocation = MutableStateFlow<State<WeatherForLocation>>(State.Loading)
    val weatherForLocation : StateFlow<State<WeatherForLocation>> = _weatherForLocation

    private val _citiesListForSearch = MutableStateFlow<List<CityForSearchItem>>(emptyList())
    val citiesListForSearch : StateFlow<List<CityForSearchItem>> = _citiesListForSearch



    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage : LiveData<String> = _successMessage

    fun getWeatherForMapLocation(
        latitude: Double,
        longitude: Double ,
        language: String = Constants.ENGLISH_LANGUAGE,
        units: String = Constants.STANDARD
    ){
        viewModelScope.launch {
            repository.getWeatherForLocation(latitude,longitude,language,units,false).collect{ state->
                _weatherForLocation.value = state
            }
        }
    }

    fun getWeatherByCountryName(
        countryName : String,
        language: String,
        units: String
    ){
        viewModelScope.launch {
            repository.getWeatherByCountryName(countryName,language,units).collect{state->
                _weatherForLocation.value = state
            }
        }
    }

    fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)
    {
        viewModelScope.launch {
            try {
                repository.insertFavouriteLocation(favouriteLocation)
                _successMessage.value = "Location was added successfully."
            }catch (e : Exception)
            {
                _errorMessage.value = e.message
            }
        }
    }

    fun getCitiesListForSearch(locationName : String)
    {
        viewModelScope.launch {
            repository.getCitiesForSearch(locationName).collect{state->
                when(state)
                {
                    is State.Error -> {
                        _errorMessage.value = state.message
                    }
                    State.Loading -> {

                    }
                    is State.Success -> {
                        _citiesListForSearch.value = state.data
                    }
                }
            }
        }
    }

}