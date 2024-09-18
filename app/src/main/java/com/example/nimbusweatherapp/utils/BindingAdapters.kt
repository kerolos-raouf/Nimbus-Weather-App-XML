package com.example.nimbusweatherapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.WeatherForLocation

@BindingAdapter("app:showContent")
fun showHomeContent(view: View, show: Boolean)
{
    if(show)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showPermissionLayout")
fun showHomePermissionLayout(view: View, show: Boolean)
{
    if(!show)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}


@BindingAdapter("app:showProgressBar")
fun showProgressBar(view: View, show: Boolean)
{
    if(show)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:setCountryName")
fun setCountryName(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = weatherForLocation.name
    }
}

@BindingAdapter("app:setTemperature")
fun setTemperature(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        val temp = weatherForLocation.main.temp / 10
        view.text = "${temp.toInt()}°"
    }
}

@BindingAdapter("app:setWeatherDescription")
fun setWeatherDescription(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = weatherForLocation.weather[0].description
    }
}

@BindingAdapter("app:setLowAndHighTemperature")
fun setLowAndHighTemperature(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        val low = weatherForLocation.main.temp / 10
        val high = weatherForLocation.main.temp / 10
        view.text = "L:${low.toInt()}° - H:${high.toInt()}°"
    }
}

@BindingAdapter("app:setPressure")
fun setPressure(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = "${weatherForLocation.main.pressure} hpa"
    }
}

@BindingAdapter("app:setHumidity")
fun setHumidity(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = "${weatherForLocation.main.humidity} %"
    }
}

@BindingAdapter("app:setWindSpeed")
fun setWindSpeed(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = "${weatherForLocation.wind.speed} m/s"
    }
}

@BindingAdapter("app:setCloud")
fun setCloud(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = "${weatherForLocation.clouds.all} %"
    }
}

@BindingAdapter("app:setVisibility")
fun setVisibility(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = "${weatherForLocation.visibility} m"
    }
}

@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        when(weatherForLocation.weather[0].description)
        {
            "clear sky" -> Glide.with(view).load(R.drawable.ic_sunny).into(view)
            "few clouds" -> Glide.with(view).load(R.drawable.ic_sunnycloudy).into(view)
            "scattered clouds" -> Glide.with(view).load(R.drawable.ic_cloudy).into(view)
            "broken clouds" -> Glide.with(view).load(R.drawable.ic_very_cloudy).into(view)
            "shower rain" -> Glide.with(view).load(R.drawable.ic_rainshower).into(view)
            "rain" -> Glide.with(view).load(R.drawable.ic_sunnyrainy).into(view)
            "thunderstorm" -> Glide.with(view).load(R.drawable.ic_thunder).into(view)
            "snow" -> Glide.with(view).load(R.drawable.ic_snowy).into(view)
            "mist" -> Glide.with(view).load(R.drawable.ic_very_cloudy).into(view)
            else -> Glide.with(view).load(R.drawable.ic_sunny).into(view)
        }
    }
}










