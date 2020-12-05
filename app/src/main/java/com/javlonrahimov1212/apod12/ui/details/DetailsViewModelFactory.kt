package com.javlonrahimov1212.apod12.ui.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod12.repository.MainRepository
import java.io.Serializable

class DetailsViewModelFactory(
    private val mainRepository: MainRepository,
    private val apodDate: String,
    private val application: Application,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(mainRepository, apodDate, application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}