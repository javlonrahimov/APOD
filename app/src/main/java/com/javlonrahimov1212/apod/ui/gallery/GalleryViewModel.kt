package com.javlonrahimov1212.apod.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import com.javlonrahimov1212.apod.utils.getLast30Days
import kotlinx.coroutines.launch

class GalleryViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val apod30Days: LiveData<List<Apod>> = mainRepository.getLast30Apods()

    fun setLast30Apods() = viewModelScope.launch {
        mainRepository.setLast30Apods(getLast30Days())
    }

}