package com.example.nimbusweatherapp.settingsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.FragmentSettingsBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.Constants


class SettingsFragment : Fragment() {


    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val settingsViewModel : SettingsViewModel by viewModels()


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

        //spinners
        val languages = listOf(Constants.ENGLISH_LANGUAGE,Constants.ARABIC_LANGUAGE)
        val languageAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,languages)
        binding.settingsLanguageSpinner.adapter = languageAdapter
        binding.settingsLanguageSpinner.setSelection(sharedViewModel.settingsLanguage.value ?: 0)

        val locationWay = listOf(Constants.GPS_LOCATION,Constants.MAP_LOCATION)
        val locationAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,locationWay)
        binding.settingsLocationSpinner.adapter = locationAdapter
        binding.settingsLocationSpinner.setSelection(sharedViewModel.settingsLocation.value ?: 0)

        val windSpeed = listOf(Constants.METER_PER_SECOND,Constants.KILOMETER_PER_HOUR)
        val windSpeedAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,windSpeed)
        binding.settingsWindSpinner.adapter = windSpeedAdapter
        binding.settingsWindSpinner.setSelection(sharedViewModel.settingsWindSpeed.value ?: 0)

        val temperature = listOf(Constants.KELVIN,Constants.CELSIUS,Constants.FAHRENHEIT)
        val temperatureAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,temperature)
        binding.settingsTemperatureSpinner.adapter = temperatureAdapter
        binding.settingsTemperatureSpinner.setSelection(sharedViewModel.settingsTemperature.value ?: 0)

        binding.settingsNotificationsSwitch.isChecked = sharedViewModel.settingsNotifications.value ?: true


        setUpSpinnersSelection()
        ///////

    }

    private fun setUpSpinnersSelection()
    {
        binding.settingsLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = binding.settingsLanguageSpinner.selectedItem.toString()
                sharedViewModel.settingsLanguage.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
                sharedViewModel.setSharedPreferencesString(Constants.LANGUAGE_KEY,selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.settingsLocationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = binding.settingsLocationSpinner.selectedItem.toString()
                sharedViewModel.settingsLocation.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
                sharedViewModel.setSharedPreferencesString(Constants.LOCATION_KEY,selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.settingsWindSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = binding.settingsWindSpinner.selectedItem.toString()
                sharedViewModel.settingsWindSpeed.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
                sharedViewModel.setSharedPreferencesString(Constants.WIND_SPEED_KEY,selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        binding.settingsTemperatureSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = binding.settingsTemperatureSpinner.selectedItem.toString()
                sharedViewModel.settingsTemperature.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
                sharedViewModel.setSharedPreferencesString(Constants.TEMPERATURE_KEY,selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.settingsNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.settingsNotifications.value = isChecked
            sharedViewModel.setSharedPreferencesBoolean(Constants.NOTIFICATION_KEY,isChecked)
        }

    }

    private fun setListeners(){
        binding.settingsMenuIcon.setOnClickListener {
            communicator.openDrawer()
        }
    }






}