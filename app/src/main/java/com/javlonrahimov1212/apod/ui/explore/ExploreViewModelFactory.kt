package com.javlonrahimov1212.apod.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod.database.ApodDao
import com.javlonrahimov1212.apod.repository.MainRepository
import com.javlonrahimov1212.apod.retrofit.ApiHelper

class ExploreViewModelFactory(private val apiHelper: ApiHelper, private val apodDao: ApodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(MainRepository(apiHelper, apodDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}