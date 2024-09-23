package com.example.nimbusweatherapp.homeFragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.FragmentHomeBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import com.example.nimbusweatherapp.utils.customAlertDialog.CustomAlertDialog
import com.example.nimbusweatherapp.utils.customAlertDialog.ICustomAlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {


    lateinit var binding : FragmentHomeBinding
    private lateinit var communicator: Communicator

    ///viewModels
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    //fusedLocationProviderClient
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient


    ///adapter
    private lateinit var mAdapter : HourlyWeatherRecyclerViewAdapter


    //custom alert dialog
    private lateinit var customAlertDialog: CustomAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observers()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){


        customAlertDialog = CustomAlertDialog(requireActivity())

        mAdapter = HourlyWeatherRecyclerViewAdapter()
        binding.homeRecyclerView.adapter = mAdapter

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
            if(sharedViewModel.settingsLocation.value == Constants.GPS_SELECTION_VALUE)
            {
                if(communicator.isLocationPermissionGranted())
                {
                    checkIfGPSIsEnabled()
                }
            }else
            {
                findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
            }
        }

        checkOnStateToChangeUI()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observers()
    {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                sharedViewModel.currentLocation.collect{newLocation->
                    doCallsOnGetLocation(newLocation.latitude,newLocation.longitude)
                }
            }
        }


        homeViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                homeViewModel.weatherEveryThreeHours.collect{state->
                    when(state)
                    {
                        is State.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        State.Loading -> {}
                        is State.Success -> {
                            state.toData()?.let {
                                mAdapter.submitList(state.toData()?.list)
                            }
                        }
                    }
                }
            }
        }

        homeViewModel.setNameAfterGettingDataFromServer.observe(viewLifecycleOwner){
            if(it)
            {
                sharedViewModel.currentLocation.value?.let {
                    homeViewModel.setNewLocationName(communicator.getReadableNameFromLocation(
                        sharedViewModel.currentLocation.value!!
                    ))
                }
            }
        }

        sharedViewModel.settingsLocation.observe(viewLifecycleOwner) {
            if(it == Constants.GPS_SELECTION_VALUE)
            {
                Glide.with(this).load(R.drawable.icon_location).into(binding.homeFAB)
            }else
            {
                Glide.with(this).load(R.drawable.icon_map_location).into(binding.homeFAB)
            }
        }

        sharedViewModel.internetState.observe(viewLifecycleOwner){
            if(homeViewModel.weatherForLocation.value == null)
            {
                checkOnStateToChangeUI()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkOnStateToChangeUI()
    {

        if(!communicator.isInternetAvailable())
        {

            sharedViewModel.showHomeContent.value = Constants.SHOW_NO_INTERNET_LAYOUT
        }
        else if(!communicator.isLocationPermissionGranted())
        {
            sharedViewModel.showHomeContent.value = Constants.SHOW_PERMISSION_DENIED_LAYOUT
            communicator.requestLocationPermission()
        }
        else
        {
            sharedViewModel.showHomeContent.value = Constants.SHOW_CONTENT_LAYOUT
            if(sharedViewModel.hitTheApiInHomeFragment.value == true)
            {
                doCallsOnGetLocation(sharedViewModel.currentLocation.value!!.latitude,sharedViewModel.currentLocation.value!!.longitude)
                sharedViewModel.hitTheApiInHomeFragment.value = false
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun doCallsOnGetLocation(lat : Double, lon : Double)
    {
        //set wind speed unit before calling api
        if (sharedViewModel.settingsWindSpeed.value == 0)
        {
            homeViewModel.currentWindSpeed.value = getString(R.string.m_s)
        }else{
            homeViewModel.currentWindSpeed.value = getString(R.string.km_h)
        }

        homeViewModel.getWeatherEveryThreeHours(
            lat,lon,
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsLanguage.value,Constants.ENGLISH_LANGUAGE),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsTemperature.value,Constants.STANDARD)
        )

        //homeViewModel.getWeatherForLocation(lat,lon)
    }



    ////////get location
    private fun checkIfGPSIsEnabled()
    {
        if(communicator.isGPSEnabled())
        {
            communicator.getCurrentLocation()
        }else
        {
            customAlertDialog.showAlertDialog(
                message = requireActivity().getString(R.string.gps_is_off_turn_it_on),
                actionText = requireActivity().getString(R.string.turn_on),
                buttonBackground = requireContext().getColor(R.color.background),
                object : ICustomAlertDialog {
                    override fun onActionClicked() {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                }
            )
        }
    }





}