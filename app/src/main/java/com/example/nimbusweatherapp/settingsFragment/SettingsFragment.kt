package com.example.nimbusweatherapp.settingsFragment

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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
        animateViews()
        setListeners()
    }

    private fun animateViews()
    {

        rotateView(binding.settingsImage)
    }

    private fun rotateView(view : View){
        val animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        animator.duration = 10000
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.RESTART
        animator.interpolator = LinearInterpolator()
        animator.start()
    }



    private fun init(){
        communicator = activity as Communicator

        //radio buttons
        binding.settingsEnglishRadioButton.isChecked = (sharedViewModel.settingsLanguage.value == Constants.ENGLISH_SELECTION_VALUE)
        binding.settingsArabicRadioButton.isChecked = (sharedViewModel.settingsLanguage.value == Constants.ARABIC_SELECTION_VALUE)

        val locationWay = listOf(getString(R.string.gps),getString(R.string.map))
        val locationAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,locationWay)
        binding.settingsLocationSpinner.adapter = locationAdapter
        binding.settingsLocationSpinner.setSelection(sharedViewModel.settingsLocation.value ?: 0)

        val windSpeed = listOf(getString(R.string.m_s),getString(R.string.km_h))
        val windSpeedAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,windSpeed)
        binding.settingsWindSpinner.adapter = windSpeedAdapter
        binding.settingsWindSpinner.setSelection(sharedViewModel.settingsWindSpeed.value ?: 0)

        val temperature = listOf(getString(R.string.kelvin),getString(R.string.celsius),getString(R.string.fahrenheit))
        val temperatureAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,temperature)
        binding.settingsTemperatureSpinner.adapter = temperatureAdapter
        binding.settingsTemperatureSpinner.setSelection(sharedViewModel.settingsTemperature.value ?: 0)

        binding.settingsNotificationsSwitch.isChecked = sharedViewModel.settingsNotifications.value ?: true


        setUpSettingsLayout()
        ///////

    }

    private fun setUpSettingsLayout()
    {

        binding.settingsEnglishRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                sharedViewModel.setSharedPreferencesString(Constants.LANGUAGE_KEY,Constants.ENGLISH_LANGUAGE)
                sharedViewModel.settingsLanguage.value = 0
                communicator.checkAndChangLocality()
                sharedViewModel.getTheLocationAgain.value = true
            }
        }

        binding.settingsArabicRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                sharedViewModel.setSharedPreferencesString(Constants.LANGUAGE_KEY,Constants.ARABIC_LANGUAGE)
                sharedViewModel.settingsLanguage.value = 1
                communicator.checkAndChangLocality()
                sharedViewModel.getTheLocationAgain.value = true
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
                val selectedValue = MainActivity.settingsInArabicAndEnglish.getOrDefault(binding.settingsLocationSpinner.selectedItem.toString(),Constants.GPS_LOCATION)
                sharedViewModel.setSharedPreferencesString(Constants.LOCATION_KEY,selectedValue)
                sharedViewModel.settingsLocation.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
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
                val selectedValue = MainActivity.settingsInArabicAndEnglish.getOrDefault(binding.settingsWindSpinner.selectedItem.toString(),Constants.METER_PER_SECOND)
                sharedViewModel.setSharedPreferencesString(Constants.WIND_SPEED_KEY,selectedValue)
                sharedViewModel.settingsWindSpeed.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
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
                val selectedValue = MainActivity.settingsInArabicAndEnglish.getOrDefault(binding.settingsTemperatureSpinner.selectedItem.toString(),Constants.KELVIN)
                sharedViewModel.setSharedPreferencesString(Constants.TEMPERATURE_KEY,selectedValue)
                sharedViewModel.settingsTemperature.value = MainActivity.settingsSelectionMap[selectedValue] ?: 0
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.settingsNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.setSharedPreferencesBoolean(Constants.NOTIFICATION_KEY,isChecked)
            sharedViewModel.settingsNotifications.value = isChecked
        }

    }

    private fun setListeners(){
        binding.settingsMenuIcon.setOnClickListener {
            communicator.openDrawer()
        }
    }






}