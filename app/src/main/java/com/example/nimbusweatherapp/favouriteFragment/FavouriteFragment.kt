package com.example.nimbusweatherapp.favouriteFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.databinding.FragmentFavouriteBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteFragment : Fragment() {


    //bindning
    private lateinit var binding : FragmentFavouriteBinding


    ///communicator
    private val communicator : Communicator by lazy { activity as Communicator }

    //view models
    private val favouriteViewModel : FavouriteViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    ///adapters
    private lateinit var favouriteAdapter : FavouriteRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_favourite,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observers()
    }

    private fun initViews()
    {
        favouriteAdapter = FavouriteRecyclerViewAdapter(object : FavouriteItemsListener{
            override fun onDeleteButtonClicked(favouriteLocation: FavouriteLocation) {
                favouriteViewModel.deleteFavouriteLocation(favouriteLocation)
            }
        })

        binding.apply {
            favouriteMenuIcon.setOnClickListener {
                communicator.openDrawer()
            }

            homeFAB.setOnClickListener {
                findNavController().navigate(R.id.action_favouriteFargment_to_mapFragment)
            }

            favouriteRecyclerView.adapter = favouriteAdapter
        }




    }

    private fun observers()
    {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favouriteViewModel.favouriteLocations.collect{locations->
                    favouriteAdapter.submitList(locations)
                }
            }
        }

    }


}