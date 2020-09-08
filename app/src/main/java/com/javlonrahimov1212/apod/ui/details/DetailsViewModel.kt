package com.javlonrahimov1212.apod.ui.details

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.javlonrahimov1212.apod.database.ApodDao
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DetailsViewModel(
    private val apodDao: ApodDao, private val apodDate: String,
    private val myApplication: Application
) : AndroidViewModel(myApplication) {
    val apod = apodDao.getApodByDate(apodDate)


    @Throws(IOException::class)
    fun saveImage() {
        Glide.with(myApplication)
            .asBitmap()
            .load(apod.value!!.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    val saved: Boolean
                    val fos: OutputStream?
                    fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val resolver = myApplication.contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, apod.value!!.title)
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