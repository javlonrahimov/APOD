package com.javlonrahimov1212.apod.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.javlonrahimov1212.apod.R
import kotlinx.android.synthetic.main.bottom_sheet_more.*


/**
 * Created by Javlon on 22-Nov-20.
 */

class BottomSheetMoreFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        open_in_browser.setOnClickListener {
            mListener?.onItemClick("open_in")
            dismissAllowingStateLoss()
        }

        copy_image_link.setOnClickListener {
            mListener?.onItemClick("copy_link")
            dismissAllowingStateLoss()
        }

        set_as_wallpaper.setOnClickListener {
            mListener?.onItemClick("wallpaper")
            dismissAllowingStateLoss()
        }

        close_bottom_sheet.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    var mListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BottomSheetMoreFragment {
            val fragment = BottomSheetMoreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}