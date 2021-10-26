package com.example.shopkart.ui.activities.base

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shopkart.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private var mDialog: Dialog? = null
    private var mDoubleBackPress = false

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

    /**
     * Inform user to press back button twice to exit from the app.
     */
    fun exitOnDoubleBackPress() {
        // If user already press back button once then allow user to exit from the app.
        if(mDoubleBackPress) {
            return super.onBackPressed()
        }

        mDoubleBackPress = true

        Toast.makeText(
            this@BaseActivity,
            "Please click BACK again to exit",
            Toast.LENGTH_SHORT
        ).show()

        lifecycleScope.launch {
            /**
             * Wait 2 sec for the user input.
             * If user press back within 2 sec then it will allow to exit otherwise it will reset the
             * backPress boolean and prevent to exit.
             */
            delay(2000)
            mDoubleBackPress = false
        }
    }



}