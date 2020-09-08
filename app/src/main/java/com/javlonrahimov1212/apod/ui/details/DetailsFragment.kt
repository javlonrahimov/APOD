package com.javlonrahimov1212.apod.ui.details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.javlonrahimov1212.apod.database.ApodDatabase
import com.javlonrahimov1212.apod.databinding.FragmentDetailsBinding
import com.javlonrahimov1212.apod.ui.DetailsActivity
import com.javlonrahimov1212.apod.utils.getWebPage


class DetailsFragment : Fragment() {

    private val REQUEST_STORAGE_PERMISSION = 0

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailsBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fullscreenButtonDetailsFragment.setOnClickListener {
            val action =
                DetailsFragmentDirections.actionDetailsFragmentToImageFullscreenFragment(viewModel.apod.value!!.url)
            binding.root.findNavController().navigate(action)
        }
        binding.backButtonDetailsFragment.setOnClickListener {
            (activity as DetailsActivity).finish()
        }

        binding.saveButtonApodDetailsFragment.setOnClickListener {
            checkForPermission()
        }

        binding.shareButtonApodDetailsFragment.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, viewModel.apod.value!!.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.openButtonApodDetailsFragment.setOnClickListener {
            val urlString = getWebPage(viewModel.apod.value!!.date)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            val title = "Select browser"
            val chooser = Intent.createChooser(intent, title)
            if (intent.resolveActivity(requireActivity().packageManager) != null)
                startActivity(chooser)
        }
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            DetailsViewModelFactory(
                ApodDatabase.getDatabase(requireActivity().applicationContext).apodDao(),
                (activity as DetailsActivity).apodDate,
                requireActivity().application
            )
        ).get(DetailsViewModel::class.java)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.saveImage()
                } else {
                    Toast.makeText(
                        activity,
                        "R.string.can_not_save_image_need_permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            viewModel.saveImage()
        }
    }
}