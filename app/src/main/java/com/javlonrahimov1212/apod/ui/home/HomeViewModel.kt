package com.javlonrahimov1212.apod.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _apodToday = MutableLiveData<Apod>()
    val apodToday: LiveData<Apod>
        get() = _apodToday

    init {
        viewModelScope.launch {
            _apodToday.value = mainRepository.getApodToday()
        }
    }
}