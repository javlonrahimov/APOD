package com.javlonrahimov1212.apod.ui.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod.database.ApodDao
import com.javlonrahimov1212.apod.models.Apod

class DetailsViewModelFactory(private val apodDao: ApodDao, private val apodDate: String, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(apodDao, apodDate, application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}