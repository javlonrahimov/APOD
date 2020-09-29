package com.javlonrahimov1212.apod.ui.explore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.javlonrahimov1212.apod.adapters.ApodExploreAdapter
import com.javlonrahimov1212.apod.adapters.OnItemClickedExploreAdapter
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentExploreBinding
import com.javlonrahimov1212.apod.models.Apod
import com.javlonrahimov1212.apod.retrofit.ApiHelper
import com.javlonrahimov1212.apod.retrofit.RetrofitBuilderSearch
import com.javlonrahimov1212.apod.ui.DetailsActivity


class ExploreFragment : Fragment() {

    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupViewModel()
        val adapter = ApodExploreAdapter()
        binding.recyclerViewExploreFragment.adapter = adapter
        adapter.onItemClickedExploreAdapter = object : OnItemClickedExploreAdapter {
            override fun onClick(apod: Apod) {
                val intent =
                    Intent(requireActivity().applicationContext, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.APOD_KEY, apod)
                startActivity(intent)
            }

        }
        binding.searchViewExploreFragment.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { q ->
                    if (q.isNotEmpty()) {
                        binding.astronautExploreFragment.visibility = View.GONE
                        binding.loadingExploreFragment.visibility = View.VISIBLE
                        viewModel.getSearchResults(q).observe(viewLifecycleOwner, {
                            adapter.apods = viewModel.extractUsefulData(it) as ArrayList<Apod>
                            binding.loadingExploreFragment.visibility = View.GONE
                        })
                    }
                }
                val imm: InputMethodManager =
                    activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view = activity!!.currentFocus
                if (view == null) {
                    view = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ExploreViewModelFactory(
                ApiHelper(RetrofitBuilderSearch.apiService),
                ApodDatabase.getDatabase(requireContext()).apodDao()
            )
        ).get(ExploreViewModel::class.java)
    }
}