package com.javlonrahimov1212.apod12.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod12.preferences.PreferenceManager
import com.javlonrahimov1212.apod12.utils.AppTheme
import kotlinx.coroutines.launch


/**
 * Created by Javlon on 25-Nov-20.
 */
class PreferencesViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {

    val notificationPref = preferenceManager.notificationPref.asLiveData()

    val wallpaperPref = preferenceManager.wallpaperPref.asLiveData()

    fun setAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        preferenceManager.setAppTheme(appTheme)
    }

    fun setNotificationPref(boolean: Boolean) = viewModelScope.launch {
        preferenceManager.setNotificationPref(boolean)
    }

    fun setWallpaperPref(boolean: Boolean) = viewModelScope.launch {
        preferenceManager.setWallpaperPref(boolean)
    }
}