package com.example.nimbusweatherapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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