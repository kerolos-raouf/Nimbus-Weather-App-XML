package com.example.nimbusweatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alert(
    @PrimaryKey
    val time : Long,
    val type : AlertType,
)

enum class AlertType {
    Alarm,
    Notification
}