package com.example.nimbusweatherapp.utils

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.DaysWeather
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import org.w3c.dom.Text

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
fun <T>showProgressBar(view: View, state: State<T>)
{
    if(state is State.Loading)
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

        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            tempString = parseIntegerIntoArabic(tempString)
        }
        view.text = "$tempString°"
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
    value?.let {
        var temp = value
        if(view.context.resources.configuration.locales.get(0).language == Constants.ARABIC_LANGUAGE)
        {
            temp = parseIntegerIntoArabic(temp)
        }
        view.text = temp//"${value.wind.speed} ${view.context.getString(R.string.m_s)}"
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











