package com.example.nimbusweatherapp.settingsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.FragmentSettingsBinding
import com.example.nimbusweatherapp.mainActivity.Communicator


class SettingsFragment : Fragment() {


    private lateinit var binding : FragmentSettingsBinding
    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_settings,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        setListeners()
    }

    private fun init(){
        communicator = activity as Communicator

        val languages = listOf("English","Arabic")
        val languageAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,languages)
        binding.settingsLanguageSpinner.adapter = languageAdapter

        val locationWay = listOf("GPS","Map")
        val locationAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,locationWay)
        binding.settingsLocationSpinner.adapter = locationAdapter

        val windSpeed = listOf("m/s","km/h")
        val windSpeedAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,windSpeed)
        binding.settingsWindSpinner.adapter = windSpeedAdapter

        val temperature = listOf("Kelvin","Celsius","Fahrenheit")
        val temperatureAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,temperature)
        binding.settingsTemperatureSpinner.adapter = temperatureAdapter



    }

    private fun setListeners(){
        binding.settingsMenuIcon.setOnClickListener {
            communicator.openDrawer()
        }
    }






}