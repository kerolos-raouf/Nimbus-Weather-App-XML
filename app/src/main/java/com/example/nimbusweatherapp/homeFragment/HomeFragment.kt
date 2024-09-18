package com.example.nimbusweatherapp.homeFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.databinding.FragmentHomeBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {


    lateinit var binding : FragmentHomeBinding
    private lateinit var communicator: Communicator

    ///viewModels
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    //fusedLocationProviderClient
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observers()
    }

    private fun init(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        communicator = requireActivity() as Communicator

        binding.sharedViewModel = sharedViewModel
        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.apply {
            homeMenuIcon.setOnClickListener {
                communicator.openDrawer()
            }
        }

        binding.homePermissionAllowButton.setOnClickListener {
            communicator.requestLocationPermission()
        }

        binding.homeFAB.setOnClickListener {
            checkGPSIfEnabled()
        }

        if(communicator.isLocationPermissionGranted())
        {
            sharedViewModel.showHomeContent.value = true
            doCallsOnGetLocation(26.8206,30.8025)
        }
        else
        {
            communicator.requestLocationPermission()
        }
    }

    private fun observers()
    {
        sharedViewModel.currentLocation.observe(viewLifecycleOwner) {
            doCallsOnGetLocation(it.latitude,it.longitude)
        }
    }


    private fun doCallsOnGetLocation(lat : Double, lon : Double)
    {
        homeViewModel.getWeatherEveryThreeHours(lat,lon)
        homeViewModel.getWeatherForLocation(lat,lon)
    }


    ////////get location
    private fun checkGPSIfEnabled()
    {
        if(isGPSEnabled())
        {
            getCurrentLocation()
        }else
        {
            Toast.makeText(requireContext(), "Please Enable GPS", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation()
    {
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


    private fun isGPSEnabled() : Boolean
    {
        val locationManager : LocationManager = requireContext().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }





}