package com.javlonrahimov1212.apod.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.javlonrahimov1212.apod.R
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentHomeBinding
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod.ui.DetailsActivity

class HomeFragment : Fragment() {


    private lateinit var viewModel: HomeViewModel
    lateinit var apod: Apod

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.apodToday.observe(viewLifecycleOwner, {
            it?.let { a ->
                apod = a
                if (apod.isLiked) {
                    binding.favButtonApodHomeFragment.setImageResource(R.drawable.ic_round_favorite_24)
                } else {
                    binding.favButtonApodHomeFragment.setImageResource(R.drawable.ic_round_favorite_border_24)
                }
            }
        })

        binding.cardViewApodFragmentHome.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.APOD_DATE_KEY, viewModel.apodToday.value?.date)
            startActivity(intent)
        }

        binding.favButtonApodHomeFragment.setOnClickListener {
            apod.isLiked = !apod.isLiked
            viewModel.updateApod(apod)
        }

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            HomeViewModelFactory(
                ApiHelper(RetrofitBuilderApod.apiService),
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao()
            )
        ).get(HomeViewModel::class.java)
    }
}