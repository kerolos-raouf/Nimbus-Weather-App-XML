package com.example.nimbusweatherapp.favouriteFragment

import com.example.nimbusweatherapp.data.model.FavouriteLocation

interface FavouriteItemsListener
{
    fun onDeleteButtonClicked(favouriteLocation: FavouriteLocation)

    fun onItemClicked(favouriteLocation: FavouriteLocation)
}