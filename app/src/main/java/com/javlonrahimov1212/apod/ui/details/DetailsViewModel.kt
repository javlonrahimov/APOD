package com.javlonrahimov1212.apod.ui.details

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.Serializable
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DetailsViewModel(
    private val mainRepository: MainRepository, apodDate: String,
    private val myApplication: Application,
    private val apodR: Serializable?
) : AndroidViewModel(myApplication) {

    private var title = ""
    private var url = ""

    var apod = mainRepository.getApodByDate(apodDate)
        get() {
            if (apodR != null) {
                url = (apodR as Apod).url
                title = apodR.title
                return liveData { emit(apodR) }
            }
            return field
        }

    init {
        viewModelScope.launch {
            try {
                mainRepository.setApodByDate(date = apodDate)
            } catch (unknownHostException: UnknownHostException) {
                Log.d("TAG_DETAILS_VIEW_MODEL", unknownHostException.message.toString())
            } catch (socketTimeOutException: SocketTimeoutException) {
                Log.d("TAG_DETAILS_VIEW_MODEL", socketTimeOutException.message.toString())
            } catch (e: Exception) {
                Log.d("TAG_DETAILS_VIEW_MODEL", "Exeption" + e.message.toString())
            }
        }
    }

    fun updateApod(apod: Apod) = viewModelScope.launch {
        mainRepository.updateApod(apod)
    }

    @Throws(IOException::class)
    fun saveImage() {
        if (apod.value != null) {
            url = apod.value!!.url
            title = apod.value!!.title
        }
        Glide.with(myApplication)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    val saved: Boolean
                    val fos: OutputStream?
                    fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val resolver = myApplication.contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, title)
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
                        val image = File(imagesDir, "${title}.png")
                        FileOutputStream(image)
                    }
                    saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    if (saved) {
                        Toast.makeText(myApplication, "Image saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(myApplication, "Something went wrong!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    fos!!.flush()
                    fos.close()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }

}