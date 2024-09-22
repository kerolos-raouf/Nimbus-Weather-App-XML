package com.example.nimbusweatherapp.favouriteFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _favouriteLocations = MutableLiveData<List<FavouriteLocation>>(emptyList())
    val favouriteLocations : StateFlow<List<FavouriteLocation>> = repository.getAllLocations().stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message




    fun deleteFavouriteLocation(location: FavouriteLocation)
    {
        viewModelScope.launch {
            repository.deleteFavouriteLocation(location)
            _message.value = "Location Deleted Successfully"
        }
    }
}