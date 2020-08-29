package com.javlonrahimov1212.apod.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.adapters.ApodExploreAdapter
import com.javlonrahimov1212.apod.models.Photo
import kotlinx.android.synthetic.main.fragment_explore.view.*

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        view.recycler_view_explore_fragment.adapter = ApodExploreAdapter(populateData())
        return view
    }

    private fun populateData(): List<Photo> {
        val photos = ArrayList<Photo>()
        val photo = Photo(
            "Hello again",
            R.drawable.image,
            "hello"
        )

        for (i in 1..15) {
            photos.add(photo)
        }
        return photos
    }
}