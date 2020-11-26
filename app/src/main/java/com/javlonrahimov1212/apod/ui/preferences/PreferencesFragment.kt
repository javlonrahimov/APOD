package com.javlonrahimov1212.apod.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.javlonrahimov1212.apod.databinding.FragmentPreferencesBinding
import com.javlonrahimov1212.apod.preferences.PreferenceManager
import com.javlonrahimov1212.apod.ui.bottomsheet.BottomSheetThemeFragment
import com.javlonrahimov1212.apod.utils.AppTheme

class PreferencesFragment : Fragment() {

    private lateinit var viewModel: PreferencesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.appTheme.setOnClickListener {
            activity?.supportFragmentManager.let {
                BottomSheetThemeFragment.newInstance(Bundle()).apply {
                    show(it!!, tag)
                    mListener = object : BottomSheetThemeFragment.ItemClickListener {
                        override fun onItemClick(theme: AppTheme) {
                            when (theme) {
                                AppTheme.SYSTEM_DEFAULT -> {
                                    viewModel.setAppTheme(AppTheme.SYSTEM_DEFAULT)
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                }
                                AppTheme.DARK -> {
                                    viewModel.setAppTheme(AppTheme.DARK)
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                }
                                AppTheme.LIGHT -> {
                                    viewModel.setAppTheme(AppTheme.LIGHT)
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                }
                                else -> {
                                    viewModel.setAppTheme(AppTheme.SYSTEM_DEFAULT)
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                }
                            }
                        }

                    }
                }
            }
        }

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            PreferencesViewModelFactory(
                PreferenceManager(requireActivity().applicationContext)
            )
        ).get(PreferencesViewModel::class.java)
    }

}