package com.javlonrahimov1212.apod12.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.database.ApodDatabase
import com.javlonrahimov1212.apod12.databinding.FragmentHomeBinding
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod12.ui.DetailsActivity

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

        binding.copyrightHomeFragment.isSelected = true

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