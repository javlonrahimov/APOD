package com.javlonrahimov1212.apod.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.models.Photo
import com.javlonrahimov1212.apod.models.SearchResult
import com.javlonrahimov1212.apod.repository.MainRepository

class ExploreViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getSearchResults(query: String) = liveData {
        emit(mainRepository.getSearchResults(query))
    }

    fun extractUsefulData(searchResult: SearchResult): List<Apod> {

        val results = ArrayList<Apod>()

        for (i in searchResult.collection.items) {
            results.add(
                Apod(
                    title = i.data[0].title,
                    explanation = i.data[0].description,
                    url = i.links[0].href,
                    date = i.data[0].date_created
                )
            )
        }
        return results
    }
}