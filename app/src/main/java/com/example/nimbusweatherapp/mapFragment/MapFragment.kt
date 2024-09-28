package com.example.nimbusweatherapp.mapFragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.model.Location
import com.example.nimbusweatherapp.databinding.BottomSheetLayoutBinding
import com.example.nimbusweatherapp.databinding.FragmentMapBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.util.Locale


@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    //communicator
    private val communicator by lazy {
        (requireActivity() as Communicator)
    }

    ///view models
    private val mapViewModel : MapViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    //bottom sheet
    private lateinit var bottomSheet : BottomSheetDialog
    private lateinit var bottomSheetBinding : BottomSheetLayoutBinding


    //marker
    private var lastMarker : Marker ?= null

    //countries list
    private lateinit var countriesList : Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_map,container,false)
        binding.viewModel = mapViewModel
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initMap()
        observers()
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initViews()
    {
        bottomSheet = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomSheetLayoutBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout,null))


        autoCompleteEditTextSetup()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(FlowPreview::class)
    private fun autoCompleteEditTextSetup()
    {
        //auto complete edit text setup
        lifecycleScope.launch {
            val sharedFlow = MutableSharedFlow<String>()
            binding.mapAutoCompleteEditText.doOnTextChanged{ text,_,_,_ ->
                launch {
                    sharedFlow.emit(text.toString())
                }
            }

            sharedFlow
                .debounce(1000)
                .distinctUntilChanged()
                .collect{cityName->
                    mapViewModel.getCitiesListForSearch(cityName)
                }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                mapViewModel.citiesListForSearch.collect{newCitiesList->
                    if(newCitiesList.isNotEmpty())
                    {
                        val citiesList = newCitiesList.map { city->
                            "${city.name}, ${city.country}"
                        }
                        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,citiesList)
                        binding.mapAutoCompleteEditText.apply {
                            setAdapter(adapter)
                            threshold = 1
                            refreshAutoCompleteResults()

                            setOnItemClickListener { parent, view, position, id ->
                                val selectedCity = newCitiesList[position]
                                mapViewModel.getWeatherForMapLocation(selectedCity.latitude,selectedCity.longitude)
                            }
                        }
                    }
                }
            }
        }




        //search icon
        binding.mapSearchIcon.setOnClickListener{

            val newName = getNearestNameForCountry(binding.mapAutoCompleteEditText.text.toString())
            if (newName != null)
            {
                getWeatherByName(newName)
            }else
            {
                Toast.makeText(requireContext(),"Illegal Country Name",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observers()
    {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    mapViewModel.weatherForLocation.collect{ state->
                        when(state)
                        {
                            is State.Error -> {
                                Toast.makeText(requireContext(),state.message,Toast.LENGTH_SHORT).show()
                            }
                            State.Loading -> {}
                            is State.Success -> {
                                showBottomSheet(Location(state.data.coord.lat,state.data.coord.lon),state.data.name)
                                val geoPoint = GeoPoint(state.data.coord.lat,state.data.coord.lon)
                                zoomToLocation(geoPoint)
                                addNewMarkerAndRemoveLastOne(geoPoint)
                            }
                        }
                    }
                }
            }


        }


        mapViewModel.successMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNearestNameForCountry(name : String) : String?
    {
        for(country in countriesList)
        {
            if (country.lowercase(Locale.getDefault()) == name.lowercase(Locale.getDefault()))
            {
                return  country
            }
        }
        return null
    }

    private fun initMap() {
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences(Constants.MAP_SHARED_PREFERENCE_NAME, MODE_PRIVATE))

        binding.mapFragmentMainMap.apply {
            setMultiTouchControls(true)
            controller.setZoom(5.0)

            //set max and min zoom
            maxZoomLevel = 30.0
            minZoomLevel = 3.0


            val mapEventReceiver = object : MapEventsReceiver{
                override fun singleTapConfirmedHelper(p: GeoPoint?) = false

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    p?.let {
                        onMapLongPressActions(p)
                    }
                    return true
                }

            }

            overlays.add(MapEventsOverlay(mapEventReceiver))

        }
    }

    private fun onMapLongPressActions(geoPoint: GeoPoint)
    {
        if(communicator.isInternetAvailable())
        {
            getWeatherForMapLocation(geoPoint)
        }else
        {
            Toast.makeText(requireContext(),"No internet connection.",Toast.LENGTH_SHORT).show()
        }

    }

    private fun addNewMarkerAndRemoveLastOne(geoPoint: GeoPoint)
    {
        lastMarker?.let {
            binding.mapFragmentMainMap.apply {
                Log.d("Kerolos", "addNewMarkerAndRemoveLastOne: was removed")
                overlays.remove(it)
                invalidate()
            }
        }
        addMarker(geoPoint = geoPoint)
    }

    private fun addMarker(geoPoint : GeoPoint)
    {
        val marker = Marker(binding.mapFragmentMainMap)

        marker.apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = requireContext().getDrawable(R.drawable.icon_location_favourite)
        }
        lastMarker = marker
        binding.mapFragmentMainMap.apply {
            overlays.add(marker)
            overlays.add(marker)
            invalidate()
        }
    }

    private fun zoomToLocation(geoPoint : GeoPoint)
    {
        binding.mapFragmentMainMap.controller.apply {
            animateTo(geoPoint,17.0,2000)
        }
    }

    private fun showBottomSheet(location: Location, locationName : String)
    {
        bottomSheet.setCancelable(true)
        bottomSheet.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.viewModel = mapViewModel

        bottomSheetBinding.bottomSheetSaveButton.setOnClickListener{
            mapViewModel.insertFavouriteLocation(FavouriteLocation(
                "${communicator.getReadableNameFromLocation(location)}${locationName}",
                location.latitude,
                location.longitude
            ))
        }

        bottomSheetBinding.bottomSheetSearchInHome.setOnClickListener {
            sharedViewModel.currentLocation.value = location
            sharedViewModel.getTheLocationAgain.value = false
            sharedViewModel.getTheLocationForMap.value = true
            bottomSheet.dismiss()
            val navOption = NavOptions.Builder().setPopUpTo(R.id.homeFragment,true).build()
            findNavController().navigate(R.id.homeFragment,null,navOption)
        }


        bottomSheet.show()
    }

    private fun getWeatherForMapLocation(geoPoint : GeoPoint)
    {
        mapViewModel.getWeatherForMapLocation(
            geoPoint.latitude,
            geoPoint.longitude,
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsLanguage.value,Constants.ENGLISH_LANGUAGE),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsTemperature.value,Constants.STANDARD)
        )
    }

    private fun getWeatherByName(name : String)
    {
        mapViewModel.getWeatherByCountryName(
            name,
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsLanguage.value,Constants.ENGLISH_LANGUAGE),
            MainActivity.settingsMeasureUnitsMap.getOrDefault(sharedViewModel.settingsTemperature.value,Constants.STANDARD)
        )
    }



}