package com.example.nimbusweatherapp.mapFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.databinding.BottomSheetLayoutBinding
import com.example.nimbusweatherapp.databinding.FragmentMapBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.MainActivity
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_map,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initMap()
        observers()
    }
    private fun initViews()
    {
        bottomSheet = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomSheetLayoutBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout,null))
    }

    private fun observers()
    {
        mapViewModel.weatherForMapLocation.observe(viewLifecycleOwner){
            showBottomSheet(it)
        }
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
                        //showBottomSheet()
                        getWeatherForMapLocation(p)
                        zoomToLocation(geoPoint = p)
                        addMarker(geoPoint = p)
                    }
                    return true
                }

            }

            overlays.add(MapEventsOverlay(mapEventReceiver))

        }
    }

    private fun addMarker(geoPoint : GeoPoint)
    {
        val marker = Marker(binding.mapFragmentMainMap)
        marker.apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = requireContext().getDrawable(R.drawable.icon_location_favourite)
        }
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

    private fun showBottomSheet(weatherForLocation: WeatherForLocation)
    {
        bottomSheet.setCancelable(true)
        bottomSheet.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.weatherItem = weatherForLocation
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


}