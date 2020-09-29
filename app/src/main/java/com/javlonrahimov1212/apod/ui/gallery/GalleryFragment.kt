package com.javlonrahimov1212.apod.ui.gallery

import android.content.Intent
import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.javlonrahimov1212.apod.adapters.ApodGalleryAdapter
import com.javlonrahimov1212.apod.adapters.OnItemClickedGalleryAdapter
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentGalleryBinding
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod.ui.DetailsActivity
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
        val adapter = ApodGalleryAdapter()
        binding.apodViewPager.adapter = adapter
        viewModel.apod30Days.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            adapter.onItemClickedGalleryAdapter = object : OnItemClickedGalleryAdapter {
                override fun onClick(date: String) {
                    val intent =
                        Intent(requireActivity().applicationContext, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.APOD_DATE_KEY, date)
                    startActivity(intent)
                }

                override fun onFavButtonClicked(position: Int) {
                    val apod = it[position]
                    apod.isLiked = !apod.isLiked
                    viewModel.updateApod(apod)
                    adapter.notifyItemChanged(position)
                }
            }
            if (it.size < 30)
                viewModel.setLast30Apods()
        })
        binding.datePickerGalleryFragment.setOnClickListener {
            showDatePicker()
        }

        binding.jumpToFirstGalleryFragment.setOnClickListener {
            binding.apodViewPager.setCurrentItem(0, true)
        }

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
                ApiHelper(RetrofitBuilderApod.apiService),
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao()
            )
        ).get(GalleryViewModel::class.java)
    }

    private fun showDatePicker() {

        var selectedDate = ""

        val calendar = android.icu.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.clear()

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        calendar.timeInMillis = today

        val constraints = CalendarConstraints.Builder()

        val calendarStart: Calendar = GregorianCalendar.getInstance()

        calendarStart.set(2015, 1, 1)

        val minDate = calendarStart.timeInMillis

        constraints.setStart(minDate)
        constraints.setEnd(today)
        constraints.setValidator(DateValidatorPointBackward.now())

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("SELECT A DATE")
            .setCalendarConstraints(constraints.build())
            .setSelection(today)

        val materialDatePicker = builder.build()

        materialDatePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
            selectedDate = simpleDateFormat.format(Date(it))
            val intent =
                Intent(requireActivity().applicationContext, DetailsActivity::class.java)
            intent.putExtra("APOD_KEY", selectedDate)
            startActivity(intent)
        }

        materialDatePicker.show(childFragmentManager, "DATE_PICKER")
    }

}