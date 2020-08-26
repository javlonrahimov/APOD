package com.javlonrahimov1212.apod.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.adapters.ApodGalleryAdapter
import com.javlonrahimov1212.apod.models.Apod
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        view.apod_view_pager.adapter = ApodGalleryAdapter(populateData())
        view.apod_view_pager.clipToPadding = false
        view.apod_view_pager.clipChildren = false
        view.apod_view_pager.offscreenPageLimit = 3
        view.apod_view_pager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(80))
        compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.9f + r * 0.1f
        })

        view.apod_view_pager.setPageTransformer(compositePageTransformer)

        return view
    }

    private fun populateData(): List<Apod> {
        val apods = ArrayList<Apod>()
        val apod = Apod(
            "Hello again",
            "From July of 1997, a ramp from the Pathfinder lander, the Sojourner robot rover, airbags, a couch, Barnacle Bill and Yogi Rock appear together in this 3D stereo view of the surface of Mars. Barnacle Bill is the rock just left of the solar-paneled Sojourner. Yogi is the big friendly-looking boulder at top right.",
            R.drawable.image,
            "NASA"
        )

        for (i in 1..10) {
            apods.add(apod)
        }
        return apods
    }

}