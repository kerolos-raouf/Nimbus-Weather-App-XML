package com.example.nimbusweatherapp.mapFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.databinding.DataBindingUtil
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.FragmentMapBinding
import com.example.nimbusweatherapp.utils.Constants
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_map,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
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


}