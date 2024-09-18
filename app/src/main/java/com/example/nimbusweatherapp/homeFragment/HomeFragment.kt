package com.example.nimbusweatherapp.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.FragmentHomeBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {


    lateinit var binding : FragmentHomeBinding
    private lateinit var communicator: Communicator

    ///viewModels
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val homeViewModel : HomeViewModel by viewModels()

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
    }

    private fun init(){
        communicator = requireActivity() as Communicator

        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.apply {
            homeMenuIcon.setOnClickListener {
                communicator.openDrawer()
            }
        }

        binding.homePermissionAllowButton.setOnClickListener {
            communicator.requestLocationPermission()
        }

        if(communicator.isLocationPermissionGranted())
        {
            sharedViewModel.showHomeContent.value = true
            //viewModel.getWeather()
            homeViewModel.getWeatherEveryThreeHours(26.8206,30.8025)
        }
        else
        {
            communicator.requestLocationPermission()
        }
    }

}