package com.javlonrahimov1212.apod12.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.javlonrahimov1212.apod12.utils.AppTheme
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

    val notificationPref: Flow<Boolean> = dataStore.data.map {
        it[IS_NOTIFICATION_ENABLED] ?: true
    }

    val wallpaperPref: Flow<Boolean> = dataStore.data.map {
        it[CAN_SET_WALLPAPER_DAILY] ?: false
    }

    val lastApodDate : Flow<String> = dataStore.data.map {
        it[LAST_APOD_DATE] ?: ""
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

    suspend fun setNotificationPref(boolean: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLED] = boolean
        }
    }

    suspend fun setWallpaperPref(boolean: Boolean) {
        dataStore.edit { preferences ->
            preferences[CAN_SET_WALLPAPER_DAILY] = boolean
        }
    }

    suspend fun setLastApodDate(string: String){
        dataStore.edit { preferences ->
            preferences[LAST_APOD_DATE] = string
        }
    }

    companion object {
        val APP_THEME = preferencesKey<String>("app_theme")
        val IS_NOTIFICATION_ENABLED = preferencesKey<Boolean>("is_notification_enabled")
        val CAN_SET_WALLPAPER_DAILY = preferencesKey<Boolean>("can_set_wallpaper_daily")
        val LAST_APOD_DATE = preferencesKey<String>("last_apod_date")
    }
}