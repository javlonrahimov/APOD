package com.javlonrahimov1212.apod.utils

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


object BinderAdapter {
    @BindingAdapter("imageReourceId")
    @JvmStatic
    fun loadResourceImage(view: ImageView, imageId: Int) {
        Glide.with(view.context)
            .load(imageId)
            .into(view)
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadRemoteImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            if (imageUrl.endsWith(".jpg")) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .into(view)
                Log.d("GLIDING", imageUrl)
            } else {
                Glide.with(view.context)
                    .load(getThumbnailUrl(imageUrl))
                    .into(view)
                Log.d("GLIDING", getThumbnailUrl(imageUrl))
            }
        }
    }
}

fun getCurrentDate(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return simpleDateFormat.format(Date())
}

fun getLast30Days(): List<String> {
    val result = ArrayList<String>()
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val calendar = Calendar.getInstance()

    for (i in 0..30) {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        result.add(simpleDateFormat.format(calendar.time))
    }

    return result
}

fun getThumbnailUrl(videoUrl: String): String {
    val videoId = videoUrl.substring(videoUrl.lastIndexOf("/") + 1).substring(0, 11)
    return "http://img.youtube.com/vi/$videoId/sddefault.jpg"
}

fun getWebPage(apodDate: String): String {
    val list = apodDate.split("-")
    val baseUrl = "https://apod.nasa.gov/apod/"
    val page = "ap${list[0].substring(2)}${list[1]}${list[2]}.html"

    return baseUrl + page
}
