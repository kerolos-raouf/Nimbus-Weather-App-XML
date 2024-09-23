package com.example.nimbusweatherapp.detailsFragment

import android.os.Build
import android.os.Bundle
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
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.databinding.FragmentDetailsBinding
import com.example.nimbusweatherapp.homeFragment.HourlyWeatherRecyclerViewAdapter
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {


    //binding
    private lateinit var binding : FragmentDetailsBinding

    //communicator
    private val communicator : Communicator by lazy {
        requireActivity() as Communicator
    }

    //view models
    private val detailsViewModel : DetailsViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    //adapter
    private lateinit var mAdapter : HourlyWeatherRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        observers()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init()
    {
        mAdapter = HourlyWeatherRecyclerViewAdapter()
        binding.homeRecyclerView.adapter = mAdapter
        binding.detailsViewModel = detailsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //doCallsOnGetLocation(sharedViewModel.currentLocation.value.latitude, sharedViewModel.currentLocation.value.longitude)

        val favouriteLocation = arguments?.let {
            DetailsFragmentArgs.fromBundle(it).location
        }

        if (favouriteLocation != null)
        {
            doCallsOnGetLocation(favouriteLocation.latitude,favouriteLocation.longitude)
        }
    }

    private fun observers()
    {
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                detailsViewModel.weatherEveryThreeHours.collect{state->

                    when(state)
                    {
                        is State.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            binding.detailsProgressAnimation.visibility = View.GONE
                        }
                        State.Loading -> {
                            binding.detailsProgressAnimation.visibility = View.VISIBLE
                        }
                        is State.Success ->
                        {
                            mAdapter.submitList(state.toData()?.list)
                            binding.detailsProgressAnimation.visibility = View.GONE
                        }
                    }
                }
            }
        }

        detailsViewModel.setNameAfterGettingDataFromServer.observe(viewLifecycleOwner){
            if(it)
            {
                sharedViewModel.currentLocation.value.let {
                    detailsViewModel.weatherForLocation.value?.let {location->
                        detailsViewModel.setNewLocationName(communicator.getReadableNameFromLocation(
                            Location(location.coord.lat,location.coord.lon)
                        ))
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doCallsOnGetLocation(lat : Double, lon : Double)
    {
        //set wind speed unit before calling api
        if (sharedViewModel.settingsWindSpeed.value == 0)
        {
            detailsViewModel.currentWindSpeed.value = getString(R.string.m_s)
        }else{
            detailsViewModel.currentWindSpeed.value = getString(R.string.km_h)
        }

        detailsViewModel.getWeatherEveryThreeHours(
            lat,lon,
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsLanguage.value,
                Constants.ENGLISH_LANGUAGE),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsTemperature.value,
                Constants.STANDARD)
        )

    }


}