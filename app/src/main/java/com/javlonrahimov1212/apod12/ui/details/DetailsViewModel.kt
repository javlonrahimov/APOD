package com.javlonrahimov1212.apod12.ui.details

import android.app.Application
import android.app.WallpaperManager
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.repository.MainRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DetailsViewModel(
    private val mainRepository: MainRepository,
    apodDate: String,
    private val myApplication: Application,
) : AndroidViewModel(myApplication) {

    var apod = mainRepository.getApodByDate(apodDate)

    init {
        viewModelScope.launch {
            try {
                mainRepository.setApodByDate(date = apodDate)
            } catch (unknownHostException: UnknownHostException) {
            } catch (socketTimeOutException: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun updateApod(apod: Apod) = viewModelScope.launch {
        mainRepository.updateApod(apod)
    }

    fun saveImage() {
        try {
            Glide.with(myApplication)
                .asBitmap()
                .load(apod.value!!.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        var saved = false
                        val fos: OutputStream?
                        fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val resolver = myApplication.contentResolver
                            val contentValues = ContentValues()
                            contentValues.put(
                                MediaStore.MediaColumns.DISPLAY_NAME,
                                apod.value!!.title
                            )
                            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                            contentValues.put(
                                MediaStore.MediaColumns.RELATIVE_PATH,
                                "Pictures/" + "APOD"
                            )
                            val imageUri =
                                resolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                                )
                            resolver.openOutputStream(imageUri!!)
                        } else {
                            val imagesDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                            ).toString() + File.separator + "APOD"
                            val file = File(imagesDir)
                            if (!file.exists()) {
                                file.mkdir()
                            }
                            val image = File(imagesDir, "${apod.value!!.title}.png")
                            FileOutputStream(image)
                        }
                        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        if (saved) {
                            Toast.makeText(myApplication, "Image saved!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                myApplication,
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        fos!!.flush()
                        fos.close()

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
        } catch (e: Exception) {
            Toast.makeText(
                myApplication,
                "Something went wrong!",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun setWallpaper() {

        Glide.with(myApplication)
            .asBitmap()
            .load(apod.value!!.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    WallpaperManager.getInstance(myApplication)
                        .setBitmap(
                            resource,
                            null,
                            true,
                            WallpaperManager.FLAG_LOCK
                        )
                    Toast.makeText(
                        myApplication,
                        "Wallpaper applied to lockscreen!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }

}