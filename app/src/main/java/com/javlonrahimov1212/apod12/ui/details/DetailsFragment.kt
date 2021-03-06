package com.javlonrahimov1212.apod12.ui.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.database.ApodDatabase
import com.javlonrahimov1212.apod12.databinding.FragmentDetailsBinding
import com.javlonrahimov1212.apod12.models.Apod
import com.javlonrahimov1212.apod12.repository.MainRepository
import com.javlonrahimov1212.apod12.retrofit.ApiHelper
import com.javlonrahimov1212.apod12.retrofit.RetrofitBuilderApod
import com.javlonrahimov1212.apod12.ui.DetailsActivity
import com.javlonrahimov1212.apod12.ui.bottomsheet.BottomSheetMoreFragment
import com.javlonrahimov1212.apod12.utils.getWebPage


class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel
    lateinit var apod: Apod

    @SuppressLint("QueryPermissionsNeeded")
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

        binding.favButtonApodDetailsFragment.setOnClickListener {
            apod.isLiked = !apod.isLiked
            viewModel.updateApod(apod)
        }

        viewModel.apod.observe(viewLifecycleOwner, {
            it?.let { a ->
                apod = a
                if (apod.isLiked) {
                    binding.favButtonApodDetailsFragment.setImageResource(R.drawable.ic_round_favorite_24)
                } else {
                    binding.favButtonApodDetailsFragment.setImageResource(R.drawable.ic_round_favorite_border_24)
                }
            }
        })

        binding.shareButtonApodDetailsFragment.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, viewModel.apod.value!!.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.moreButtonApodDetailsFragment.setOnClickListener {
            activity?.supportFragmentManager.let {
                BottomSheetMoreFragment.newInstance(Bundle()).apply {
                    show(it!!, tag)
                    mListener = object : BottomSheetMoreFragment.ItemClickListener {
                        override fun onItemClick(item: String) {
                            when (item) {
                                "open_in" -> {
                                    openInBrowser()
                                }
                                "copy_link" -> {
                                    copyToClipboard()
                                }
                                "wallpaper" -> {
                                    viewModel.setWallpaper()
                                }
                            }
                        }

                    }
                }
            }
        }

        binding.playVideoButtonDetailsFragment.setOnClickListener {
            val urlString = viewModel.apod.value!!.url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            val title = "Select method"
            val chooser = Intent.createChooser(intent, title)
            if (intent.resolveActivity(requireActivity().packageManager) != null)
                startActivity(chooser)
        }

        viewModel.apod.observe(viewLifecycleOwner, {
            if (viewModel.apod.value != null && viewModel.apod.value!!.media_type == "video") {
                binding.playVideoButtonDetailsFragment.visibility = View.VISIBLE
                binding.fullscreenButtonDetailsFragment.visibility = View.GONE
                binding.saveButtonApodDetailsFragment.isEnabled = false
            }
            if ((activity as DetailsActivity).apod != null) {
                binding.favButtonApodDetailsFragment.visibility = View.GONE
                binding.imageViewApodDetailsFragment.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        })

        binding.copyrightApodDetailsFragment.isSelected = true

        return binding.root
    }

    private fun openInBrowser() {
        val urlString = getWebPage(viewModel.apod.value!!.date)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        val title = "Select browser"
        val chooser = Intent.createChooser(intent, title)
        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivity(chooser)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            DetailsViewModelFactory(
                MainRepository(
                    ApiHelper(RetrofitBuilderApod.apiService),
                    ApodDatabase.getDatabase(requireContext()).apodDao()
                ),
                (activity as DetailsActivity).apodDate,
                requireActivity().application,
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

    fun copyToClipboard() {
        val clipboard = (requireActivity() as DetailsActivity).clipboard
        val clip: ClipData = ClipData.newPlainText("text", apod.url)
        clipboard?.let {
            it.setPrimaryClip(clip)
            Toast.makeText(
                requireContext(),
                "Copied to clipboard",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 0
    }
}