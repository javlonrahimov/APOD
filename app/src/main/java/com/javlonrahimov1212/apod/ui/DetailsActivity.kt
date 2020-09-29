package com.javlonrahimov1212.apod.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.models.Apod
import java.io.Serializable

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val APOD_DATE_KEY = "apod_date_key"
        const val APOD_KEY = "photo_key"
    }

    lateinit var apodDate: String
    var apod: Serializable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        apodDate = intent.getStringExtra(APOD_DATE_KEY) ?: ""
        apod = intent.getSerializableExtra(APOD_KEY)
    }
}