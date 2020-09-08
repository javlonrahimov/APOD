package com.javlonrahimov1212.apod.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.javlonrahimov1212.apod.R

class DetailsActivity : AppCompatActivity() {
    var apodDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        apodDate = intent.getStringExtra("APOD_KEY") ?: ""
    }
}