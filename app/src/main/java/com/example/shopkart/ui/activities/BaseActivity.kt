package com.example.shopkart.ui.activities

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.shopkart.R

open class BaseActivity : AppCompatActivity() {

    private var mDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        // Hide the status bar.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // Hide the status bar.
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            actionBar?.hide()
        }
    }

    /**
     * Method to show progress bar dialog.
     */
    fun showProgressbar() {
        mDialog = Dialog(this)
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