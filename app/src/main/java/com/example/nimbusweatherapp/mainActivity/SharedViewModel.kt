package com.example.nimbusweatherapp.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nimbusweatherapp.data.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(

) : ViewModel() {

    val showHomeContent = MutableLiveData(false)

    //location information for egypt by default
    val currentLocation = MutableLiveData(Location(30.8025,26.8206))




}