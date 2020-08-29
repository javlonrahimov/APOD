package com.javlonrahimov1212.apod.utils

import android.icu.text.SimpleDateFormat
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(
            status = Status.SUCCESS,
            data = data,
            message = null
        )

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> = Resource(
            status = Status.LOADING,
            data = data,
            message = null
        )
    }
}

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
        Glide.with(view.context)
            .load(imageUrl)
            .into(view)
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
        calendar.add(Calendar.DAY_OF_YEAR, -i)
        result.add(simpleDateFormat.format(calendar.time))
    }

    return result
}

