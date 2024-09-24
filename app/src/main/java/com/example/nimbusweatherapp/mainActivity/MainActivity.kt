package com.example.nimbusweatherapp.mainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.databinding.ActivityMainBinding
import com.example.nimbusweatherapp.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , Communicator {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController

    //viewModel
    private val sharedViewModel : SharedViewModel by viewModels()

    //fusedLocationProviderClient
    private val fusedLocationProviderClient : FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }


    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 2

        val settingsSelectionMap = HashMap<String,Int>()
        val settingsMeasureUnitsMap = HashMap<Int,String>()
        val settingsInArabicAndEnglish = HashMap<String,String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSelectionMap()
        getAndSetSettingsValues()
        checkAndChangLocality()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this



        init()
        observers()
    }

    private fun init(){

        navController = Navigation.findNavController(this,R.id.navHost)
        binding.navigationView.setupWithNavController(navController)

        sharedViewModel.observeOnInternetState()
    }

    private fun observers()
    {
        sharedViewModel.internetState.observe(this){state->
            if(state == ConnectivityObserver.InternetState.AVAILABLE)
            {
                Snackbar.make(binding.root,"Internet Connected",Snackbar.LENGTH_SHORT).show()
            }else
            {
                Snackbar.make(binding.root,"Connection Lost",Snackbar.LENGTH_SHORT).show()
            }
        }

    }


    private fun initSelectionMap()
    {
        settingsSelectionMap[Constants.ENGLISH_LANGUAGE] = Constants.ENGLISH_SELECTION_VALUE
        settingsSelectionMap[Constants.ARABIC_LANGUAGE] = Constants.ARABIC_SELECTION_VALUE
        settingsSelectionMap[Constants.GPS_LOCATION] = Constants.GPS_SELECTION_VALUE
        settingsSelectionMap[Constants.MAP_LOCATION] = Constants.MAP_SELECTION_VALUE
        settingsSelectionMap[Constants.METER_PER_SECOND] = Constants.METER_PER_SECOND_SELECTION_VALUE
        settingsSelectionMap[Constants.KILOMETER_PER_HOUR] = Constants.KILOMETER_PER_HOUR_SELECTION_VALUE
        settingsSelectionMap[Constants.KELVIN] = Constants.KELVIN_SELECTION_VALUE
        settingsSelectionMap[Constants.CELSIUS] = Constants.CELSIUS_SELECTION_VALUE
        settingsSelectionMap[Constants.FAHRENHEIT] = Constants.FAHRENHEIT_SELECTION_VALUE


        settingsMeasureUnitsMap[Constants.METER_PER_SECOND_SELECTION_VALUE] = Constants.METER_PER_SECOND
        settingsMeasureUnitsMap[Constants.KILOMETER_PER_HOUR_SELECTION_VALUE] = Constants.KILOMETER_PER_HOUR
        settingsMeasureUnitsMap[Constants.KELVIN_SELECTION_VALUE] = Constants.STANDARD
        settingsMeasureUnitsMap[Constants.CELSIUS_SELECTION_VALUE] = Constants.METRIC
        settingsMeasureUnitsMap[Constants.FAHRENHEIT_SELECTION_VALUE] = Constants.IMPERIAL
        settingsMeasureUnitsMap[Constants.ENGLISH_SELECTION_VALUE] = Constants.ENGLISH_LANGUAGE
        settingsMeasureUnitsMap[Constants.ARABIC_SELECTION_VALUE] = Constants.ARABIC_LANGUAGE

        settingsInArabicAndEnglish[Constants.GPS_LOCATION] = Constants.GPS_LOCATION
        settingsInArabicAndEnglish[Constants.GPS_LOCATION_ARABIC] = Constants.GPS_LOCATION
        settingsInArabicAndEnglish[Constants.MAP_LOCATION] = Constants.MAP_LOCATION
        settingsInArabicAndEnglish[Constants.MAP_LOCATION_ARABIC] = Constants.MAP_LOCATION
        settingsInArabicAndEnglish[Constants.METER_PER_SECOND] = Constants.METER_PER_SECOND
        settingsInArabicAndEnglish[Constants.METER_PER_SECOND_ARABIC] = Constants.METER_PER_SECOND
        settingsInArabicAndEnglish[Constants.KILOMETER_PER_HOUR] = Constants.KILOMETER_PER_HOUR
        settingsInArabicAndEnglish[Constants.KILOMETER_PER_HOUR_ARABIC] = Constants.KILOMETER_PER_HOUR
        settingsInArabicAndEnglish[Constants.KELVIN] = Constants.KELVIN
        settingsInArabicAndEnglish[Constants.KELVIN_ARABIC] = Constants.KELVIN
        settingsInArabicAndEnglish[Constants.CELSIUS] = Constants.CELSIUS
        settingsInArabicAndEnglish[Constants.CELSIUS_ARABIC] = Constants.CELSIUS
        settingsInArabicAndEnglish[Constants.FAHRENHEIT] = Constants.FAHRENHEIT
        settingsInArabicAndEnglish[Constants.FAHRENHEIT_ARABIC] = Constants.FAHRENHEIT
    }

    /////communicator functions
    override fun openDrawer() = binding.drawerLayout.openDrawer(GravityCompat.START)


    override fun isLocationPermissionGranted() : Boolean
    {
        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun isPostNotificationsPermissionGranted(): Boolean {
        return (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun requestPostNotificationsPermission() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun isShowOnOtherAppsPermissionGranted(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    override fun requestShowOnOtherAppsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    override fun isGPSEnabled() : Boolean {
        val locationManager : LocationManager = getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun isInternetAvailable(): Boolean {
        return sharedViewModel.internetState.value == ConnectivityObserver.InternetState.AVAILABLE
    }


    @SuppressLint("MissingPermission")
    override fun getCurrentLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location = it.result
            if(location != null)
            {
                sharedViewModel.currentLocation.value = Location(location.latitude,location.longitude)
            }else
            {
                val locationRequest = LocationRequest.Builder(0).build()
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {

                        override fun onLocationResult(p0: LocationResult) {
                            val lastLocation = p0.lastLocation
                            lastLocation?.let {
                                sharedViewModel.currentLocation.value = Location(lastLocation.latitude,lastLocation.longitude)
                            }
                        }

                    },
                    Looper.getMainLooper())
            }
        }
    }

    override fun getReadableNameFromLocation(location: Location) : String {
        val geoCoder = Geocoder(applicationContext)
        val address = geoCoder.getFromLocation(location.latitude,location.longitude,1)
        return "${(address?.get(0)?.countryName) ?: Constants.GEOCODER_NOT_LOCATED} - "
    }

    override fun requestLocationPermission()
    {
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onLocationPermissionGranted() {
        if(isInternetAvailable())
        {
            sharedViewModel.showHomeContent.value = Constants.SHOW_CONTENT_LAYOUT
        }else{
            sharedViewModel.showHomeContent.value = Constants.SHOW_NO_INTERNET_LAYOUT
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                onLocationPermissionGranted()
            }
        }

        if(requestCode == POST_NOTIFICATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //requestPostNotificationsPermission()
            }
        }
    }

    private fun getAndSetSettingsValues()
    {
        sharedViewModel.settingsLanguage.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.LANGUAGE_KEY),Constants.ENGLISH_SELECTION_VALUE)

        sharedViewModel.settingsLocation.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.LOCATION_KEY),Constants.GPS_SELECTION_VALUE)

        sharedViewModel.settingsWindSpeed.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.WIND_SPEED_KEY),Constants.METER_PER_SECOND_SELECTION_VALUE)

        sharedViewModel.settingsTemperature.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.TEMPERATURE_KEY),Constants.KELVIN_SELECTION_VALUE)

        sharedViewModel.settingsNotifications.value =
            sharedViewModel.getSharedPreferencesBoolean(Constants.NOTIFICATION_KEY)
    }


    override fun checkAndChangLocality()
    {
        val languageCode = if(sharedViewModel.settingsLanguage.value == Constants.ENGLISH_SELECTION_VALUE) "en" else "ar"
        val locale = resources.configuration.locales[0]

        if(locale.language != languageCode)
        {

            val newLocale = Locale(languageCode)
            Locale.setDefault(newLocale)

            val config = resources.configuration

            config.setLocale(newLocale)
            config.setLayoutDirection(newLocale)

            resources.updateConfiguration(config,resources.displayMetrics)

            //val newConfigurationContext = createConfigurationContext(config)

            //resources.updateConfiguration(newConfigurationContext.resources.configuration,newConfigurationContext.resources.displayMetrics)

            recreate()

            Log.d("Kerolos", "checkAndChangLocality: changed")

        }
    }





}