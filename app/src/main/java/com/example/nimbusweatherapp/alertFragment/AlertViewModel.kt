package com.example.nimbusweatherapp.alertFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts : StateFlow<List<Alert>> = repository.getAllAlerts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )






    fun deleteAlert(alert: Alert)
    {
        viewModelScope.launch {
            repository.deleteAlert(alert)
        }
    }

    fun addAlert(alert: Alert)
    {
        viewModelScope.launch {
            repository.insertAlert(alert)
        }
    }


}