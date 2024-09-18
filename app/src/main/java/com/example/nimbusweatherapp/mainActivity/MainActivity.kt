package com.example.nimbusweatherapp.mainActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , Communicator {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController

    //viewModel
    private val viewModel : SharedViewModel by viewModels()


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
        viewModel.showHomeContent.value = true
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