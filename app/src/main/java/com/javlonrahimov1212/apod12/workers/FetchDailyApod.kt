package com.javlonrahimov1212.apod12.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.asLiveData
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.database.ApodDatabase
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.preferences.PreferenceManager
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod12.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Javlon on 27-Nov-20.
 */
class FetchDailyApod(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    val mainRepository = MainRepository(
        ApiHelper(RetrofitBuilderApod.apiService),
        ApodDatabase.getDatabase(context).apodDao()
    )

    override suspend fun doWork(): Result {

        val mCalendar: Calendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        val mGMTOffset = mTimeZone.rawOffset

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(
            Calendar.HOUR_OF_DAY, (6 + TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)).toInt()
        )
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<FetchDailyApod>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(MainActivity.TASK_ID)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(dailyWorkRequest)
        fetchApod()

        GlobalScope.launch(Dispatchers.Main) {
            mainRepository.getApodToday().observeForever {
                PreferenceManager(applicationContext).notificationPref.asLiveData()
                    .observeForever { canShow ->
                        if (canShow)
                            sendNotification(it.title, it.explanation)
                    }
                PreferenceManager(applicationContext).wallpaperPref.asLiveData()
                    .observeForever { canSet ->
                        if (canSet)
                            setWallpaper(it)
                    }

                Glide.with(applicationContext)
                    .downloadOnly().load(it.url).submit()
            }
        }
        return Result.success()
    }

    private suspend fun fetchApod(): Boolean {
        return mainRepository.setApodToday()
    }

    private fun sendNotification(title: String, message: String) {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            "default"
        )
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }

    private fun setWallpaper(apod: Apod) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(apod.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    WallpaperManager.getInstance(applicationContext)
                        .setBitmap(
                            resource,
                            null,
                            true,
                            WallpaperManager.FLAG_LOCK
                        )
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }
}
