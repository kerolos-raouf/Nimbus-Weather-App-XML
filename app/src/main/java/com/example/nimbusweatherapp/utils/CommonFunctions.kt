package com.example.nimbusweatherapp.utils

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.nimbusweatherapp.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun capitalizeWord(input: String): String {
    return input.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertUnixToDay(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)

    val dateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("EEEE")

    return dateTime.format(formatter)
}

fun setIcon(view: ImageView, state: String?)
{
    state?.let {
        when(it.lowercase(Locale.ROOT))
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