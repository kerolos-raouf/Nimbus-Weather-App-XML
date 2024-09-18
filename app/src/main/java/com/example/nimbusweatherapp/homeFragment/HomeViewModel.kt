package com.example.nimbusweatherapp.homeFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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




    fun getWeatherEveryThreeHours(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.getWeatherEveryThreeHours(latitude, longitude).collect{
                when(it)
                {
                    is State.Error -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Error ${it.message}")
                    }
                    State.Loading -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Loading")
                    }
                    is State.Success -> {
                        Log.d("Kerolos", "getWeatherEveryThreeHours: Success${it.data.list.size}")
                    }
                }
            }
        }
    }
}