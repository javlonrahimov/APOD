package com.javlonrahimov1212.apod12.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod12.database.ApodDao
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.ui.gallery.GalleryViewModel

class FavouritesViewModelFactory(private val apiHelper: ApiHelper, private val apodDao: ApodDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            return FavouritesViewModel(MainRepository(apiHelper, apodDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}