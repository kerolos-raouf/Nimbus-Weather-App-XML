package com.example.nimbusweatherapp.favouriteFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.databinding.ItemFavouriteBinding

class DiffUtilCallback : DiffUtil.ItemCallback<FavouriteLocation>() {
    override fun areItemsTheSame(oldItem: FavouriteLocation, newItem: FavouriteLocation) = oldItem.locationName == newItem.locationName
    override fun areContentsTheSame(oldItem: FavouriteLocation, newItem: FavouriteLocation) = oldItem == newItem
}

class FavouriteRecyclerViewAdapter (
    private val listener: FavouriteItemsListener
) : ListAdapter<FavouriteLocation, ViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.favouriteItem = item
        holder.binding.favouriteItemListener = listener
    }
}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val binding = ItemFavouriteBinding.bind(view)
}