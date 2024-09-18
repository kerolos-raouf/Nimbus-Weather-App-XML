package com.example.nimbusweatherapp.homeFragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.DaysWeather
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.utils.State
import com.example.nimbusweatherapp.utils.capitalizeWord
import com.example.nimbusweatherapp.utils.convertUnixToDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository

): ViewModel()
{


    private val _weatherEveryThreeHours = MutableLiveData<WeatherEveryThreeHours>()
    val weatherEveryThreeHours : LiveData<WeatherEveryThreeHours> = _weatherEveryThreeHours

    private val _weatherForLocation = MutableLiveData<WeatherForLocation>()
    val weatherForLocation : LiveData<WeatherForLocation> = _weatherForLocation


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

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean> = _loading



    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherEveryThreeHours(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.getWeatherEveryThreeHours(latitude, longitude).collect{
                when(it)
                {
                    is State.Error -> {
                        _error.value = it.message
                        _loading.value = false
                    }
                    State.Loading -> {
                        _loading.value = true
                    }
                    is State.Success -> {
                        _weatherEveryThreeHours.value = it.data
                        //Log.d("Kerolos", "getWeatherEveryThreeHours: ${_weatherEveryThreeHours.value!!.list.size}")
                        fillDaysWeather(_weatherEveryThreeHours.value!!.list)
                        _loading.value = false
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillDaysWeather(list : List<WeatherItemEveryThreeHours>)
    {
        val dayOneIndex = 1
        val dayTwoIndex = 9
        val dayThreeIndex = 17
        val dayFourIndex = 25
        val dayFiveIndex = 33
        _dayOne.value = DaysWeather(convertUnixToDay(list[dayOneIndex].dt.toLong()).substring(0,3),capitalizeWord(list[dayOneIndex].weather[0].description),list[dayOneIndex].main.temp.toString(),list[dayTwoIndex].main.temp.toString())
        _dayTwo.value = DaysWeather(convertUnixToDay(list[dayTwoIndex].dt.toLong()).substring(0,3),capitalizeWord(list[dayTwoIndex].weather[0].description),list[dayTwoIndex].main.temp.toString(),list[dayTwoIndex].main.temp.toString())
        _dayThree.value = DaysWeather(convertUnixToDay(list[dayThreeIndex].dt.toLong()).substring(0,3),capitalizeWord(list[dayThreeIndex].weather[0].description),list[dayThreeIndex].main.temp.toString(),list[dayTwoIndex].main.temp.toString())
        _dayFour.value = DaysWeather(convertUnixToDay(list[dayFourIndex].dt.toLong()).substring(0,3),capitalizeWord(list[dayFourIndex].weather[0].description),list[dayFourIndex].main.temp.toString(),list[dayTwoIndex].main.temp.toString())
        _dayFive.value = DaysWeather(convertUnixToDay(list[dayFiveIndex].dt.toLong()).substring(0,3),capitalizeWord(list[dayFiveIndex].weather[0].description),list[dayFiveIndex].main.temp.toString(),list[dayTwoIndex].main.temp.toString())
    }






    fun getWeatherForLocation(latitude: Double, longitude: Double)
    {
        viewModelScope.launch {
            repository.getWeatherForLocation(latitude, longitude).collect{
                when(it)
                {
                    is State.Error -> {
                        _error.value = it.message
                        _loading.value = false
                    }
                    State.Loading -> {
                        _loading.value = true
                    }
                    is State.Success -> {
                        _weatherForLocation.value = it.data
                        _loading.value = false
                    }
                }
            }
        }
    }
}