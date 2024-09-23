package com.example.nimbusweatherapp.alertFragment

import com.example.nimbusweatherapp.data.model.Alert

interface AlertItemListener {
    fun onDeleteButtonClicked(alert: Alert)
}