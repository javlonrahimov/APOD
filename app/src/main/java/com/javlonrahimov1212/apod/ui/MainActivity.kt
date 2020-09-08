package com.javlonrahimov1212.apod.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.utils.NetworkStatus
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NetworkStatus.init(application)
        setUpNavigation()
        NetworkStatus.observe(this, { isConnected ->
            if (isConnected)
                connection_status_view.visibility = View.GONE
            else
                connection_status_view.visibility = View.VISIBLE
        })
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