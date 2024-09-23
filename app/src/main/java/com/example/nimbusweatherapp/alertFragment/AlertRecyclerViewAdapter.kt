package com.example.nimbusweatherapp.alertFragment

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.databinding.ItemAlarmBinding
import com.example.nimbusweatherapp.databinding.ItemFavouriteBinding
import com.example.nimbusweatherapp.favouriteFragment.FavouriteItemsListener
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.convertMilliSecondsToTime
import com.example.nimbusweatherapp.utils.convertUnixToDay

class DiffUtilCallback : DiffUtil.ItemCallback<Alert>() {
    override fun areItemsTheSame(oldItem: Alert, newItem: Alert) = oldItem.time == newItem.time

    override fun areContentsTheSame(oldItem: Alert, newItem: Alert) = oldItem == newItem

}

class AlertRecyclerViewAdapter (
    private val listener: AlertItemListener
) : ListAdapter<Alert, ViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.alarmType.text = item.type.toString()
        holder.binding.alarmTimeText.text = convertMilliSecondsToTime(item.time,Constants.DATE_FORMAT_FOR_ALERT)
        holder.binding.alarmDeleteImageView.setOnClickListener{
            listener.onDeleteButtonClicked(item)
        }
    }
}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val binding = ItemAlarmBinding.bind(view)
}