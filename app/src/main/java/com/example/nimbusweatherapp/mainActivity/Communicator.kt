package com.example.nimbusweatherapp.mainActivity

import com.example.nimbusweatherapp.data.model.Location

interface Communicator {

    fun openDrawer()

    fun isLocationPermissionGranted() : Boolean

    fun isGPSEnabled() : Boolean

    fun getCurrentLocation()

    fun getReadableNameFromLocation(location : Location) : String

    fun requestLocationPermission()

    fun onLocationPermissionGranted()

    fun checkAndChangLocality()

}