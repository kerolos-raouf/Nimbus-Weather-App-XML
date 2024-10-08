package com.example.nimbusweatherapp.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.databinding.BindingAdapter
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.DaysWeather
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import org.w3c.dom.Text

@BindingAdapter("app:showContent")
fun showHomeContent(view: View, show: Int)
{
    if(show == Constants.SHOW_CONTENT_LAYOUT)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showPermissionLayout")
fun showHomePermissionLayout(view: View, show: Int)
{
    if(show == Constants.SHOW_PERMISSION_DENIED_LAYOUT)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showInternetConnectionLayout")
fun showInternetConnectionLayout(view: View, show: Int)
{
    if(show == Constants.SHOW_NO_INTERNET_LAYOUT)
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
        val temp = (temperature / 10).toInt()
        var tempString = temp.toString()

        var tempUint = "K"
        val sharedPrefUnit = view.context.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getString(Constants.TEMPERATURE_KEY,Constants.KELVIN)
        if (sharedPrefUnit == Constants.CELSIUS)
        {
            tempUint = "C"
        }else if(sharedPrefUnit == Constants.FAHRENHEIT)
        {
            tempUint = "F"
        }

        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }
        view.text = "$tempString °$tempUint"
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



        var tempString = "L:${low.toInt()}° - H:${high.toInt()}°"

        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
            tempString.replace("L","اقل").replace("H","اعلى")
        }
        view.text = "$tempString"
    }
}

@BindingAdapter("app:setPressure")
fun setPressure(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {

        var tempString = "${weatherForLocation.main.pressure} hpa"
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }
        view.text = tempString
    }
}

@BindingAdapter("app:setHumidity")
fun setHumidity(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {

        var tempString = "${weatherForLocation.main.humidity} %"
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }

        view.text = tempString
    }
}

@BindingAdapter("app:setTextFromViewModel")
fun setTextFromViewModel(view: TextView, value : String?)
{
    if(!value.isNullOrEmpty() && value != "null")
    {
        var temp = value
        var unit = view.context.getString(R.string.m_s)
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            temp = parseIntegerIntoArabic(temp)
        }
        val sharedPrefUnit = view.context.getSharedPreferences(Constants.SETTINGS_SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
            .getString(Constants.WIND_SPEED_KEY,Constants.METER_PER_SECOND)
        if(sharedPrefUnit == Constants.KILOMETER_PER_HOUR || sharedPrefUnit == Constants.KILOMETER_PER_HOUR_ARABIC)
        {
            unit = view.context.getString(R.string.km_h)
            if(temp.toFloatOrNull() != null)
            {
                temp = ("%.2f".format(temp.toFloat() * 3.6))
            }
        }

        view.text = "$temp $unit"//"${value.wind.speed} ${view.context.getString(R.string.m_s)}"
    }
}

@BindingAdapter("app:setCountryName")
fun setCountryName(view: TextView, value : String?)
{
    value?.let {
        view.text = value
    }
}

@BindingAdapter("app:setVisibility")
fun setVisibility(view: TextView, visibility : String?)
{
    visibility?.let {
        var temp = visibility
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            temp = parseIntegerIntoArabic(temp)
        }

        view.text = "${temp} ${view.context.getString(R.string.meter)}"
    }
}

@BindingAdapter("app:setCloud")
fun setCloud(view: TextView, weatherForLocation : WeatherForLocation?)
{
    weatherForLocation?.let {

        var tempString = "${weatherForLocation.clouds.all} %"
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }

        view.text = tempString
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

        var tempString = "${low.toInt()}°-${high.toInt()}°"

        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }

        view.text = tempString
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
        var temp = time
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            temp = parseIntegerIntoArabic(temp)
        }
        view.text = temp.substring(11, 16)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("app:setDateAndTime")
fun setDateAndTime(view: TextView, time : Int?)
{
    time?.let {
        var temp = convertUnixToDay(time.toLong(), Constants.DATE_FORMAT_FOR_HOME_FRAGMENT_WEATHER)
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            temp = parseIntegerIntoArabic(temp)
        }
        view.text = temp
    }
}











