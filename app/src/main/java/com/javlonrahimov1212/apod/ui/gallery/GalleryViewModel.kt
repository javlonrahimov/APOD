package com.javlonrahimov1212.apod.ui.gallery

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class GalleryViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val apod30Days: LiveData<List<Apod>> = mainRepository.getLast30Apods()

    fun setLast30Apods() = viewModelScope.launch {
        mainRepository.setLast30Apods(getLast30Days())
    }


    fun updateApod(apod: Apod) = viewModelScope.launch {
        mainRepository.updateApod(apod)
    }

    private fun getLast30Days(): List<String> {
        val result = ArrayList<String>()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()

        for (i in 0..30) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            result.add(simpleDateFormat.format(calendar.time))
        }

        return result
    }
}