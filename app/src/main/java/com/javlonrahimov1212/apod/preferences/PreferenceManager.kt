package com.javlonrahimov1212.apod.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.javlonrahimov1212.apod.utils.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


/**
 * Created by Javlon on 26-Nov-20.
 */

class PreferenceManager(context: Context) {
    private val dataStore = context.createDataStore(name = "preferences_apod")


    val appThemeFlow: Flow<AppTheme> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            when (it[APP_THEME] ?: false) {
                "system_default" -> AppTheme.SYSTEM_DEFAULT
                "dark" -> AppTheme.DARK
                "light" -> AppTheme.LIGHT
                else -> AppTheme.SYSTEM_DEFAULT
            }
        }


    suspend fun setAppTheme(appTheme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[APP_THEME] = when (appTheme) {
                AppTheme.SYSTEM_DEFAULT -> "system_default"
                AppTheme.DARK -> "dark"
                AppTheme.LIGHT -> "light"
                else -> "system_default"
            }
        }
    }

    companion object {
        val APP_THEME = preferencesKey<String>("app_theme")
    }
}