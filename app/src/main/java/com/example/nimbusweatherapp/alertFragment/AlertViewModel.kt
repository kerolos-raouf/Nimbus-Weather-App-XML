package com.example.nimbusweatherapp.alertFragment

import androidx.lifecycle.ViewModel
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts : StateFlow<List<Alert>> = _alerts




}