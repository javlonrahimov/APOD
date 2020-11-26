package com.javlonrahimov1212.apod.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val apodToday: LiveData<Apod> = mainRepository.getApodToday()
    val dayOfWeek = liveData<String> {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        emit(SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time))
    }

    val monthAndDay = liveData {
        val c = Calendar.getInstance()
        val monthName = arrayOf(
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"
        )
        val month = monthName[c[Calendar.MONTH]]

        emit(month + " " + c[Calendar.DAY_OF_MONTH])
    }

    fun updateApod(apod: Apod) = viewModelScope.launch {
        mainRepository.updateApod(apod)
    }

    init {
        viewModelScope.launch {
            try {
                mainRepository.setApodToday()
            } catch (unknownHostException: UnknownHostException) {
            } catch (socketTimeOutException: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }
}