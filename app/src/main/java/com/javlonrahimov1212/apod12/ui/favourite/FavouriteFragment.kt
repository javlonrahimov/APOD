package com.javlonrahimov1212.apod12.ui.favourite

import android.annotation.SuppressLint
import android.content.Intent
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
import com.javlonrahimov1212.apod12.adapters.ApodGalleryAdapter
import com.javlonrahimov1212.apod12.adapters.OnItemClickedGalleryAdapter
import com.javlonrahimov1212.apod12.database.ApodDatabase
import com.javlonrahimov1212.apod12.databinding.FragmentFavouriteBinding
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod12.ui.DetailsActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class FavouriteFragment : Fragment() {

    lateinit var viewModel: FavouritesViewModel

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
        val binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ApodGalleryAdapter()
        binding.apodViewPagerFavouriteFragment.adapter = adapter
        adapter.onItemClickedGalleryAdapter = object : OnItemClickedGalleryAdapter {
            override fun onClick(date: String) {
                val intent =
                    Intent(requireActivity().applicationContext, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.APOD_DATE_KEY, date)
                startActivity(intent)
            }

            override fun onFavButtonClicked(apod: Apod) {
                viewModel.updateApod(apod.copy(isLiked = false))
            }
        }
        viewModel.favoritesApod.observe(viewLifecycleOwner, {
            adapter.submitListFav(it)
            if (it.isEmpty()){
                binding.noFavText.visibility = View.VISIBLE
            }else{
                binding.noFavText.visibility = View.GONE
            }
        })
        setUpViewPager(binding)
        return binding.root
    }

    private fun setUpViewPager(binding: FragmentFavouriteBinding) {
        binding.apodViewPagerFavouriteFragment.clipToPadding = false
        binding.apodViewPagerFavouriteFragment.clipChildren = false
        binding.apodViewPagerFavouriteFragment.offscreenPageLimit = 3
        binding.apodViewPagerFavouriteFragment.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(60))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.9f + r * 0.1f
        }
        binding.apodViewPagerFavouriteFragment.setPageTransformer(compositePageTransformer)

        binding.apodViewPagerFavouriteFragment.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (viewModel.favoritesApod.value!!.isNotEmpty()) {
                    val currentDate = viewModel.favoritesApod.value!![position].date
                    val dateArray = currentDate.split("-")
                    calendar.set(Calendar.DAY_OF_MONTH, dateArray[2].toInt())
                    calendar.set(Calendar.MONTH, dateArray[1].toInt() - 1)
                    calendar.set(Calendar.YEAR, dateArray[0].toInt())
                    val date = calendar.time
                    binding.dayOfWeekFavouriteFragment.text =
                        SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
                    val month = monthName[calendar[Calendar.MONTH]]
                    binding.dayOfMonthFavouritesFragment.text =
                        month + " " + calendar[Calendar.DAY_OF_MONTH]
                }else{

                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            FavouritesViewModelFactory(
                ApiHelper(RetrofitBuilderApod.apiService),
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao()
            )
        ).get(FavouritesViewModel::class.java)
    }
}