package com.example.nimbusweatherapp.utils

import android.health.connect.datatypes.units.Temperature
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.DaysWeather
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import java.util.Locale

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
fun setTemperature(view: TextView, temperature: Double?)
{
    temperature?.let {
        val temp = temperature / 10
        view.text = "${temp.toInt()}°"
    }
}

@BindingAdapter("app:setWeatherDescription")
fun setWeatherDescription(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {
        view.text = capitalizeWord(weatherForLocation.weather[0].description)
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
        view.text = "${weatherForLocation.wind.speed} ${view.context.getString(R.string.m_s)}"
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
        view.text = "${weatherForLocation.visibility} ${view.context.getString(R.string.meter)}"
    }
}

@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, state : String?)
{
    state?.let {
        setIcon(view, state)
    }
}


@BindingAdapter("app:setDayName")
fun setDayName(view: TextView, daysWeather : DaysWeather?)
{
    daysWeather?.let {
        view.text = daysWeather.day
    }
}

@BindingAdapter("app:setDayDescription")
fun setDayDescription(view: TextView, daysWeather : DaysWeather?)
{
    daysWeather?.let {
        view.text = daysWeather.weatherDescription
    }
}

@BindingAdapter("app:setDayTemperature")
fun setDayTemperature(view: TextView, daysWeather : DaysWeather?)
{
    daysWeather?.let {
        val low = daysWeather.lowTemperature.toDouble() / 10
        val high = daysWeather.highTemperature.toDouble() / 10
        view.text = "${low.toInt()}°-${high.toInt()}°"
    }
}

@BindingAdapter("app:setDayWeatherIcon")
fun setDayWeatherIcon(view: ImageView, daysWeather : DaysWeather?)
{
    daysWeather?.let {
        setIcon(view, daysWeather.weatherDescription)
    }
}


@BindingAdapter("app:setTime")
fun setTime(view: TextView, time : String?)
{
    time?.let {
        view.text = time.substring(11, 16)
    }
}











