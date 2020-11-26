package com.javlonrahimov1212.apod.ui

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.utils.isDarkTheme
import java.io.Serializable

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val APOD_DATE_KEY = "apod_date_key"
        const val APOD_KEY = "photo_key"
    }

    var clipboard: ClipboardManager? = null
    lateinit var apodDate: String
    var apod: Serializable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        if (!isDarkTheme(this))
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        apodDate = intent.getStringExtra(APOD_DATE_KEY) ?: ""
        apod = intent.getSerializableExtra(APOD_KEY)
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}