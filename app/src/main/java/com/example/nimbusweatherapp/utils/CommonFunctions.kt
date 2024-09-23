package com.example.nimbusweatherapp.utils

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.nimbusweatherapp.R
import java.text.SimpleDateFormat
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
fun convertUnixToDay(unixTimestamp: Long,format : String): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)

    val dateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern(format)

    return dateTime.format(formatter)
}

fun convertMilliSecondsToTime(milliSeconds: Long, pattern: String): String
{
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())


    return dateFormat.format(milliSeconds)
}

fun setIcon(view: ImageView, state: String?)
{
    state?.let {
        when(it.lowercase(Locale.ROOT))
        {
            "clear sky" -> Glide.with(view).load(R.drawable.ic_sunny).into(view)
            "few clouds" -> Glide.with(view).load(R.drawable.ic_sunnycloudy).into(view)
            "scattered clouds" -> Glide.with(view).load(R.drawable.ic_cloudy).into(view)
            "broken clouds" , "overcast clouds"-> Glide.with(view).load(R.drawable.ic_very_cloudy).into(view)
            "shower rain" -> Glide.with(view).load(R.drawable.ic_rainshower).into(view)
            "rain" -> Glide.with(view).load(R.drawable.ic_sunnyrainy).into(view)
            "thunderstorm" -> Glide.with(view).load(R.drawable.ic_thunder).into(view)
            "snow" -> Glide.with(view).load(R.drawable.ic_snowy).into(view)
            "mist" -> Glide.with(view).load(R.drawable.ic_very_cloudy).into(view)
            else -> Glide.with(view).load(R.drawable.ic_sunny).into(view)
        }
    }
}

fun parseIntegerIntoArabic(number : String) : String
{
    return number
        .replace("1","١")
        .replace("2","٢")
        .replace("3","٣")
        .replace("4","٤")
        .replace("5","٥")
        .replace("6","٦")
        .replace("7","٧")
        .replace("8","٨")
        .replace("9","٩")
        .replace("0","٠")
}