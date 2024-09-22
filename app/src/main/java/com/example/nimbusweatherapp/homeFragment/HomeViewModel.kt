package com.example.nimbusweatherapp.homeFragment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.internetStateObserver.InternetStateObserver
import com.example.nimbusweatherapp.data.model.DaysWeather
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.timeout
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


    private val _weatherEveryThreeHours = MutableStateFlow<State<WeatherEveryThreeHours>>(State.Loading)
    val weatherEveryThreeHours : StateFlow<State<WeatherEveryThreeHours>> = _weatherEveryThreeHours

    private val _weatherForLocation = MutableLiveData<WeatherForLocation>()
    val weatherForLocation : LiveData<WeatherForLocation> = _weatherForLocation

    ///units
    val currentWindSpeed = MutableLiveData("m/s")

    //setNewName
    private val _setNewName = MutableLiveData(false)
    val setNewName : LiveData<Boolean> = _setNewName



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


    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherEveryThreeHours(latitude: Double, longitude: Double, language: String, units: String) {
        viewModelScope.launch {
            repository.getWeatherEveryThreeHours(latitude, longitude, language, units).collect{state->

                _weatherEveryThreeHours.value = state
                getWeatherForLocation(latitude, longitude, language, units)
                _weatherEveryThreeHours.value.toData()?.list?.let { itemEveryThreeHours ->
                    fillDaysWeather(
                        itemEveryThreeHours
                    )
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillDaysWeather(list : List<WeatherItemEveryThreeHours>)
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






    private fun getWeatherForLocation(latitude: Double, longitude: Double , language: String, units: String)
    {
        viewModelScope.launch {
            repository.getWeatherForLocation(latitude, longitude, language, units).collect{
                when(it)
                {
                    is State.Error -> {
                        _error.value = it.message
                    }
                    State.Loading -> {

                    }
                    is State.Success -> {
                        _weatherForLocation.value = it.data

                        _setNewName.value = true
                        addWindSpeedUnit(currentWindSpeed.value ?: "m/s")
                    }
                }
            }
        }
    }

    private fun addWindSpeedUnit(unit : String)
    {
        var currentSpeed = _weatherForLocation.value?.wind?.speed?.toDouble() ?: 0.0
        if(unit == "km/h" || unit == "كم/ساعة")
        {
            currentSpeed *= 3.6
        }

        _weatherForLocation.value = _weatherForLocation.value?.copy(
            wind = _weatherForLocation.value?.wind?.copy(speed = "${currentSpeed.toInt()} $unit") ?: Wind(0, 0.0, "m/s")
        )
    }

    fun setNewLocationName(newName : String)
    {
        _weatherForLocation.value = (if(newName.contains(Constants.GEOCODER_NOT_LOCATED)) _weatherForLocation.value?.name else newName)?.let {
            _weatherForLocation.value?.copy(
                name = it
            )
        }
    }
}