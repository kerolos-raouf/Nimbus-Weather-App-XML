package com.example.nimbusweatherapp.mainActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.databinding.ActivityMainBinding
import com.example.nimbusweatherapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , Communicator {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController

    //viewModel
    private val sharedViewModel : SharedViewModel by viewModels()


    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        val settingsSelectionMap = HashMap<String,Int>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this


        initSelectionMap()
        init()
        observers()
        getAndSetSettingsValues()
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
                Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show()
            }else
            {
                Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT).show()
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
    }

    override fun openDrawer() = binding.drawerLayout.openDrawer(GravityCompat.START)


    override fun isLocationPermissionGranted() : Boolean
    {
        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED)
    }

    override fun requestLocationPermission()
    {
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onLocationPermissionGranted() {
        sharedViewModel.showHomeContent.value = true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)
            {
                onLocationPermissionGranted()
            }
        }
    }

    private fun getAndSetSettingsValues()
    {
        sharedViewModel.settingsLanguage.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.LANGUAGE_KEY),0)

        sharedViewModel.settingsLocation.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.LOCATION_KEY),0)

        sharedViewModel.settingsWindSpeed.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.WIND_SPEED_KEY),0)

        sharedViewModel.settingsTemperature.value =
            settingsSelectionMap.getOrDefault(sharedViewModel.getSharedPreferencesString(Constants.TEMPERATURE_KEY),0)

        sharedViewModel.settingsNotifications.value =
            sharedViewModel.getSharedPreferencesBoolean(Constants.NOTIFICATION_KEY)
    }





}