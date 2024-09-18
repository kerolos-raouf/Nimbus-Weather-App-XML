package com.example.nimbusweatherapp.mainActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , Communicator {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController

    //viewModel
    private val sharedViewModel : SharedViewModel by viewModels()





    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        init()

    }

    private fun init(){

        navController = Navigation.findNavController(this,R.id.navHost)
        binding.navigationView.setupWithNavController(navController)

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



}