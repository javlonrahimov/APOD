package com.javlonrahimov1212.apod12.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.javlonrahimov1212.apod12.R
import com.javlonrahimov1212.apod12.preferences.PreferenceManager
import com.javlonrahimov1212.apod12.utils.AppTheme
import kotlinx.android.synthetic.main.bottom_sheet_theme.*


/**
 * Created by Javlon on 25-Nov-20.
 */
class BottomSheetThemeFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        PreferenceManager(requireView().context).appThemeFlow.asLiveData().observe(this) {
            when (it) {
                AppTheme.SYSTEM_DEFAULT -> {
                    system_default.isChecked = true
                }
                AppTheme.LIGHT -> {
                    light.isChecked = true
                }
                AppTheme.DARK -> {
                    dark.isChecked = true
                }
                else -> {
                    system_default.isChecked = true
                }
            }
        }

        system_default.setOnClickListener {
            mListener?.onItemClick(AppTheme.SYSTEM_DEFAULT)
            dismissAllowingStateLoss()
        }

        dark.setOnClickListener {
            mListener?.onItemClick(AppTheme.DARK)
            dismissAllowingStateLoss()
        }

        light.setOnClickListener {
            mListener?.onItemClick(AppTheme.LIGHT)
            dismissAllowingStateLoss()
        }

        close_bottom_sheet.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }


    var mListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onItemClick(theme: AppTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BottomSheetThemeFragment {
            val fragment = BottomSheetThemeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}