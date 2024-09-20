package com.example.nimbusweatherapp.mainActivity

interface Communicator {

    fun openDrawer()

    fun isLocationPermissionGranted() : Boolean

    fun requestLocationPermission()

    fun onLocationPermissionGranted()

    fun checkAndChangLocality()

}