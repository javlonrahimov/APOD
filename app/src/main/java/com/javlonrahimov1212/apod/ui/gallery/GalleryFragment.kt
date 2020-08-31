package com.javlonrahimov1212.apod.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.javlonrahimov1212.apod.adapters.ApodGalleryAdapter
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentGalleryBinding
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {

    private lateinit var viewModel: GalleryViewModel
    val monthName = arrayOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November",
        "December"
    )
    val calendar = GregorianCalendar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.apod30Days.observe(viewLifecycleOwner, {
            binding.apodViewPager.adapter = ApodGalleryAdapter(it)
            if (it.size < 30)
                viewModel.setLast30Apods()
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
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.9f + r * 0.1f
        }
        binding.apodViewPager.setPageTransformer(compositePageTransformer)

        binding.apodViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentDate = viewModel.apod30Days.value!![position].date
                val dateArray = currentDate.split("-")
                calendar.set(Calendar.DAY_OF_MONTH, dateArray[2].toInt())
                calendar.set(Calendar.MONTH, dateArray[1].toInt() - 1)
                calendar.set(Calendar.YEAR, dateArray[0].toInt())
                val date = calendar.time
                binding.dayOfWeekGalleryFragment.text =
                    SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
                val month = monthName[calendar[Calendar.MONTH]]
                binding.monthAndDayGalleryFragment.text =
                    month + " " + calendar[Calendar.DAY_OF_MONTH]
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            GalleryViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao()
            )
        ).get(GalleryViewModel::class.java)
    }

}