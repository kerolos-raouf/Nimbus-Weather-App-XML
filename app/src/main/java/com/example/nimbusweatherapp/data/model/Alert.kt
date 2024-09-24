package com.example.nimbusweatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alert(
    val time : Long,
    val type : AlertType,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
)

enum class AlertType {
    Alarm,
    Notification
}