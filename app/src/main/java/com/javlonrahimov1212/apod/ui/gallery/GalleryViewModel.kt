package com.javlonrahimov1212.apod.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.repository.MainRepository
import com.javlonrahimov1212.apod.utils.getLast30Days
import kotlinx.coroutines.launch

class GalleryViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _apod30Days = MutableLiveData<List<Apod>>()
    val apod30Days: LiveData<List<Apod>>
        get() = _apod30Days

    init {
        viewModelScope.launch {
            _apod30Days.value = mainRepository.getLAst30Apods(getLast30Days())
        }
    }
}