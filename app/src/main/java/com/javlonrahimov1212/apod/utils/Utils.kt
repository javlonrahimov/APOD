package com.javlonrahimov1212.apod.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.javlonrahimov1212.apod.R
import java.util.*


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
            when {
                imageUrl.endsWith(".jpg") -> {
                    Glide.with(view.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.transparent)
                        .into(view)
                    Log.d("GLIDING", imageUrl)
                }
                imageUrl.contains("youtube") -> {
                    Glide.with(view.context)
                        .load(getThumbnailUrl(imageUrl))
                        .placeholder(R.drawable.transparent)
                        .into(view)
                }
                else -> {
                    view.setImageResource(R.drawable.transparent)
                }
            }
        }
    }
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
