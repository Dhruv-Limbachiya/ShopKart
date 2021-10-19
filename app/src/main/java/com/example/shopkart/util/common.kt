package com.example.shopkart.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import com.example.shopkart.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created By Dhruv Limbachiya on 13-10-2021 12:38.
 */

fun showSnackBar(view: View, message: String, isError: Boolean = false) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackBar.view
    val context = view.context

    if (isError) {
        snackBarView.setBackgroundColor(context.resources.getColor(R.color.color_error))
    } else {
        snackBarView.setBackgroundColor(context.resources.getColor(R.color.color_green))
    }

    snackBar.show()
}

/**
 * Utility function to get the extension from the uri.
 */
fun getExtension(imageUri: Uri, context: Context) =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(imageUri))