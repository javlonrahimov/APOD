package com.javlonrahimov1212.apod.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.preferences.PreferenceManager
import com.javlonrahimov1212.apod.utils.AppTheme
import com.javlonrahimov1212.apod.utils.NetworkStatus
import com.javlonrahimov1212.apod.utils.isDarkTheme
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetworkStatus.init(application)
        preferenceManager = PreferenceManager(applicationContext)
        observeAppThemePreferences()
        setUpNavigation()
        NetworkStatus.observe(this, { isConnected ->
            if (isConnected)
                connection_status_view.visibility = View.GONE
            else
                connection_status_view.visibility = View.VISIBLE
        })
    }

    private fun observeAppThemePreferences() {
        preferenceManager.appThemeFlow.asLiveData().observe(this) {
            when (it) {
                AppTheme.SYSTEM_DEFAULT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                AppTheme.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                AppTheme.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            if (!isDarkTheme(this))
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            bottom_navigation,
            navHostFragment!!.navController
        )
    }
}