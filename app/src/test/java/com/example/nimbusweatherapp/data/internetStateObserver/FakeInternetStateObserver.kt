package com.example.nimbusweatherapp.data.internetStateObserver

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeInternetStateObserver : ConnectivityObserver {
    override fun observer(): Flow<ConnectivityObserver.InternetState> = flow {
        emit(ConnectivityObserver.InternetState.AVAILABLE)
    }
}