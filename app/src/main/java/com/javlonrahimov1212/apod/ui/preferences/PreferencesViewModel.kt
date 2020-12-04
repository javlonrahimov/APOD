package com.javlonrahimov1212.apod.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.preferences.PreferenceManager
import com.javlonrahimov1212.apod.utils.AppTheme
import kotlinx.coroutines.launch


/**
 * Created by Javlon on 25-Nov-20.
 */
class PreferencesViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {

    val notificationPref = preferenceManager.notificationPref.asLiveData()

    fun setAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        preferenceManager.setAppTheme(appTheme)
    }

    fun setNotificationPref(boolean: Boolean) = viewModelScope.launch {
        preferenceManager.setNotificationPref(boolean)
    }
}