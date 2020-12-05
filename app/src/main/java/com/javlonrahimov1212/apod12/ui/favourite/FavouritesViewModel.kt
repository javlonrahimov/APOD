package com.javlonrahimov1212.apod12.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.repository.MainRepository
import kotlinx.coroutines.launch

class FavouritesViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val favoritesApod = mainRepository.getFavouritesApod()

    fun updateApod(apod: Apod) = viewModelScope.launch {
        mainRepository.updateApod(apod)
    }
}