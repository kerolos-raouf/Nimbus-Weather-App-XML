package com.example.nimbusweatherapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Alert(
    val time : Long,
    val type : AlertType,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
) : Parcelable

enum class AlertType {
    Alarm,
    Notification
}