package com.javlonrahimov1212.apod12.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod12.database.ApodDao
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper

class GalleryViewModelFactory(private val apiHelper: ApiHelper, private val apodDao: ApodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(MainRepository(apiHelper, apodDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}