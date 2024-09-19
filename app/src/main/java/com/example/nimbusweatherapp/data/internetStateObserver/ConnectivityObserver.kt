package com.example.nimbusweatherapp.data.internetStateObserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observer() : Flow<InternetState>

    enum class InternetState{
        AVAILABLE,
        Unavailable,
        Losing,
        Lost
    }
}
