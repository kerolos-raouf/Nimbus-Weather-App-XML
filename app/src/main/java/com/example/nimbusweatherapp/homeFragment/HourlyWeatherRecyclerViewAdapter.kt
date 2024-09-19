package com.example.nimbusweatherapp.homeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusweatherapp.R

import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.databinding.ItemHomeHourlyWeatherBinding

class DiffUtil : DiffUtil.ItemCallback<WeatherItemEveryThreeHours>() {
    override fun areItemsTheSame(
        oldItem: WeatherItemEveryThreeHours,
        newItem: WeatherItemEveryThreeHours
    ): Boolean = oldItem.dt == newItem.dt

    override fun areContentsTheSame(
        oldItem: WeatherItemEveryThreeHours,
        newItem: WeatherItemEveryThreeHours
    ): Boolean = oldItem == newItem

}

class HourlyWeatherRecyclerViewAdapter : ListAdapter<WeatherItemEveryThreeHours, ViewHolder>(DiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_hourly_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.weatherItem = currentItem
    }
}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val binding = ItemHomeHourlyWeatherBinding.bind(view)
}