package com.javlonrahimov1212.apod12.ui

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.utils.isDarkTheme
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
        val content: View = findViewById(R.id.container_details_activity)
        content.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        content.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = insets.systemWindowInsetBottom)
            view.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
        apodDate = intent.getStringExtra(APOD_DATE_KEY) ?: ""
        apod = intent.getSerializableExtra(APOD_KEY)
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}