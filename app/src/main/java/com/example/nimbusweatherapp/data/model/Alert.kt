package com.example.nimbusweatherapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Alert(
    @PrimaryKey
    val time : Long,
    val type : AlertType,
) : Parcelable

enum class AlertType {
    Alarm,
    Notification
}