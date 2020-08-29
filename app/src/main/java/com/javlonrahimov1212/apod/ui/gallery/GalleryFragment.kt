package com.javlonrahimov1212.apod.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.javlonrahimov1212.apod.adapters.ApodGalleryAdapter
import com.javlonrahimov1212.apod.databinding.FragmentGalleryBinding
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilder
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {

    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.apod30Days.observe(viewLifecycleOwner, Observer {
            binding.apodViewPager.adapter = ApodGalleryAdapter(it)
        })
        setUpViewPager(binding)
        return binding.root
    }

    private fun setUpViewPager(binding: FragmentGalleryBinding) {
        binding.apodViewPager.clipToPadding = false
        binding.apodViewPager.clipChildren = false
        binding.apodViewPager.offscreenPageLimit = 3
        binding.apodViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(80))
        compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.9f + r * 0.1f
        })

        binding.apodViewPager.setPageTransformer(compositePageTransformer)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            GalleryViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(GalleryViewModel::class.java)
    }

}