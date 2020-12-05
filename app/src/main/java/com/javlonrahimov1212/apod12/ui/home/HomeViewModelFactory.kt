package com.javlonrahimov1212.apod12.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod12.database.ApodDao
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper

class HomeViewModelFactory(private val apiHelper: ApiHelper, private val apodDao: ApodDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(MainRepository(apiHelper, apodDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}