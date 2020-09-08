package com.javlonrahimov1212.apod.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentHomeBinding
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilder
import com.javlonrahimov1212.apod.ui.DetailsActivity
import com.javlonrahimov1212.apod.utils.getCurrentDate

class HomeFragment : Fragment() {


    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.apodToday.observe(viewLifecycleOwner, {
            if (it == null) {
                viewModel.setApodToday()
            } else
                if (it.date != getCurrentDate()) {
                    viewModel.setApodToday()
                }
        })

        binding.cardViewApodFragmentHome.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, DetailsActivity::class.java)
            intent.putExtra("APOD_KEY", viewModel.apodToday.value?.date)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            HomeViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao()
            )
        ).get(HomeViewModel::class.java)
    }
}