package com.example.nimbusweatherapp.homeFragment

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.data.model.Location
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        animateViews()
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

    private fun animateViews()
    {
        animateHomeWeatherIcon()
    }

    private fun animateHomeWeatherIcon()
    {
        val animator = ObjectAnimator.ofFloat(binding.homeWeatherIcon, "rotation", 0f, 360f)
        animator.repeatCount = 1
        animator.duration = 2000
        animator.start()

        val alpha = ObjectAnimator.ofFloat(binding.homeWeatherIcon, "alpha", 0f, 1f)
        alpha.repeatCount = 1
        alpha.duration = 2000
        alpha.start()

        val scaleX = ObjectAnimator.ofFloat(binding.homeWeatherIcon, "scaleX", 0f, 1f)
        scaleX.repeatCount = 1
        scaleX.duration = 2000
        scaleX.start()

        val scaleY = ObjectAnimator.ofFloat(binding.homeWeatherIcon, "scaleY", 0f, 1f)
        scaleY.repeatCount = 1
        scaleY.duration = 2000
        scaleY.start()

        val transition = ObjectAnimator.ofFloat(binding.homeWeatherIcon, "translationY", -100f, 0f)
        transition.repeatCount = 1
        transition.duration = 2000
        transition.start()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observers()
    {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                sharedViewModel.currentLocation.collect{newLocation->
                    if(sharedViewModel.internetState.value == ConnectivityObserver.InternetState.AVAILABLE)
                    {
                        doCallsOnGetLocation(newLocation.latitude,newLocation.longitude)
                    }
                }
            }
        }


        homeViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                homeViewModel.weatherEveryThreeHours.collect{list->
                    if(list.isNotEmpty())
                    {
                        showContentAfterObserveOnLocalData()
                        homeViewModel.fillDaysWeather(list)
                        mAdapter.submitList(list)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                homeViewModel.setNameAfterGettingDataFromServer.collect{
                    if(it)
                    {
                        sharedViewModel.currentLocation.value.let {
                            homeViewModel.setNewLocationName(communicator.getReadableNameFromLocation(
                                sharedViewModel.currentLocation.value
                            ))
                        }
                    }
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
            checkOnStateToChangeUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkOnStateToChangeUI()
    {

        if(!communicator.isInternetAvailable() && homeViewModel.weatherForLocation.value.isEmpty())
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
            showContentAfterObserveOnLocalData()
        }
    }

    private fun showContentAfterObserveOnLocalData()
    {
        sharedViewModel.showHomeContent.value = Constants.SHOW_CONTENT_LAYOUT
        if (communicator.isGPSEnabled() && sharedViewModel.getTheLocationAgain.value == true)
        {
            communicator.getCurrentLocation()
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

        homeViewModel.updateWeatherBaseOnLocation(
            Location(lat,lon),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsLanguage.value,Constants.ENGLISH_LANGUAGE),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsTemperature.value,Constants.STANDARD)
        )

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