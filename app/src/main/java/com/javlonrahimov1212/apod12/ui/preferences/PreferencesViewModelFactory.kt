package com.javlonrahimov1212.apod12.ui.preferences

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javlonrahimov1212.apod12.database.ApodDao
import com.javlonrahimov1212.apod12.preferences.PreferenceManager
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.ui.home.HomeViewModel


/**
 * Created by Javlon on 25-Nov-20.
 */
class PreferencesViewModelFactory(private val preferenceManager: PreferenceManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
            return PreferencesViewModel(preferenceManager) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}