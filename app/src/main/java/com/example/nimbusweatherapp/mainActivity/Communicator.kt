package com.example.nimbusweatherapp.mainActivity

import com.example.nimbusweatherapp.data.model.Location

interface Communicator {

    fun openDrawer()

    fun isLocationPermissionGranted() : Boolean

    fun isPostNotificationsPermissionGranted() : Boolean

    fun requestPostNotificationsPermission()

    fun isShowOnOtherAppsPermissionGranted() : Boolean

    fun requestShowOnOtherAppsPermission()

    fun isGPSEnabled() : Boolean

    fun isInternetAvailable() : Boolean

    fun getCurrentLocation()

    fun getReadableNameFromLocation(location : Location) : String

    fun requestLocationPermission()

    fun onLocationPermissionGranted()

    fun checkAndChangLocality()

}