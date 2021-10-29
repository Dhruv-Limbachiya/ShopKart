package com.example.shopkart.ui.fragments.base

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.example.shopkart.R

open class BaseFragment : Fragment() {

    private var mDialog: Dialog? = null

    /**
    * Method to show progress bar dialog.
    */
    fun showProgressbar() {
        mDialog = Dialog(requireActivity())
        mDialog?.let {
            it.setContentView(R.layout.progressbar_dialog_layout)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            it.show()
        }
    }

    /**
     * Method to hide progress bar dialog.
     */
    fun hideProgressbar() {
        mDialog?.dismiss()
    }
}