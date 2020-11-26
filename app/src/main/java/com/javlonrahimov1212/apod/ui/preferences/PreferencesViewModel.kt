package com.javlonrahimov1212.apod.ui.preferences

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.preferences.PreferenceManager
import com.javlonrahimov1212.apod.utils.AppTheme
import kotlinx.coroutines.launch


/**
 * Created by Javlon on 25-Nov-20.
 */
class PreferencesViewModel(val preferenceManager: PreferenceManager) : ViewModel() {

    fun setAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        preferenceManager.setAppTheme(appTheme)
    }

}