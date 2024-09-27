package com.example.nimbusweatherapp.homeFragment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.DaysWeather
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.data.model.Wind
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import com.example.nimbusweatherapp.utils.capitalizeWord
import com.example.nimbusweatherapp.utils.convertUnixToDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository

): ViewModel()
{

    val weatherEveryThreeHours : StateFlow<List<WeatherItemEveryThreeHours>> = repository.getWeatherItemEveryThreeHoursFromLocal()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val weatherForLocation : StateFlow<List<WeatherForLocation>> = repository.getWeatherForLocationFromLocal().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    ///units
    val currentWindSpeed = MutableLiveData("m/s")

    //setNewName
    private val _setNameAfterGettingDataFromServer = MutableStateFlow(false)
    val setNameAfterGettingDataFromServer : StateFlow<Boolean> = _setNameAfterGettingDataFromServer



    private val _dayOne = MutableLiveData<DaysWeather>()
    val dayOne : LiveData<DaysWeather> = _dayOne

    private val _dayTwo = MutableLiveData<DaysWeather>()
    val dayTwo : LiveData<DaysWeather> = _dayTwo

    private val _dayThree = MutableLiveData<DaysWeather>()
    val dayThree : LiveData<DaysWeather> = _dayThree

    private val _dayFour = MutableLiveData<DaysWeather>()
    val dayFour : LiveData<DaysWeather> = _dayFour

    private val _dayFive = MutableLiveData<DaysWeather>()
    val dayFive : LiveData<DaysWeather> = _dayFive

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading


    suspend fun getWeatherForLocationCount() : Int
    {
        return repository.getWeatherForLocationCount()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeatherBaseOnLocation(location  : Location, language: String, units: String)
    {
        viewModelScope.launch {
            repository.getWeatherForLocation(location.latitude, location.longitude, language, units).collectLatest {state->
                when(state)
                {
                    is State.Error -> {
                        _error.value = state.message
                        _loading.value = false
                    }
                    State.Loading -> {
                        _loading.value = true
                    }
                    is State.Success -> {
                        getWeatherEveryThreeHours(location.latitude, location.longitude, language, units)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherEveryThreeHours(latitude: Double, longitude: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getWeatherEveryThreeHours(latitude, longitude, language, units)
                .collectLatest { state ->

                    when (state) {
                        is State.Error -> {
                            _error.value = state.message
                            _loading.value = false
                        }
                        State.Loading -> {
                        }
                        is State.Success -> {
                            _loading.value = false
                        }
                    }
                }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fillDaysWeather(list : List<WeatherItemEveryThreeHours>)
    {
        val format = "EEEE"
        val dayOneIndex = 1
        val dayTwoIndex = 9
        val dayThreeIndex = 17
        val dayFourIndex = 25
        val dayFiveIndex = 33
        _dayOne.value = DaysWeather(convertUnixToDay(list[dayOneIndex].dt.toLong(),format),capitalizeWord(list[dayOneIndex].weather[0].description),list[dayOneIndex].main.tempMin.toString(),list[dayTwoIndex].main.tempMax.toString())
        _dayTwo.value = DaysWeather(convertUnixToDay(list[dayTwoIndex].dt.toLong(),format),capitalizeWord(list[dayTwoIndex].weather[0].description),list[dayTwoIndex].main.tempMin.toString(),list[dayTwoIndex].main.tempMax.toString())
        _dayThree.value = DaysWeather(convertUnixToDay(list[dayThreeIndex].dt.toLong(),format),capitalizeWord(list[dayThreeIndex].weather[0].description),list[dayThreeIndex].main.tempMin.toString(),list[dayTwoIndex].main.tempMax.toString())
        _dayFour.value = DaysWeather(convertUnixToDay(list[dayFourIndex].dt.toLong(),format),capitalizeWord(list[dayFourIndex].weather[0].description),list[dayFourIndex].main.tempMin.toString(),list[dayTwoIndex].main.tempMax.toString())
        _dayFive.value = DaysWeather(convertUnixToDay(list[dayFiveIndex].dt.toLong(),format),capitalizeWord(list[dayFiveIndex].weather[0].description),list[dayFiveIndex].main.tempMin.toString(),list[dayTwoIndex].main.tempMax.toString())

    }



    fun setNewLocationName(newName : String)
    {
        //_weatherForLocation.value = _weatherForLocation.value?.copy(name = "${newName}${_weatherForLocation.value?.name}")
    }
}