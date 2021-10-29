package com.example.shopkart.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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


fun showToast(context: Context, message: String) {
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}

/**
 * Utility function to get the extension from the uri.
 */
fun getExtension(imageUri: Uri, context: Context) =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(imageUri))


@BindingAdapter(value = ["setImageUrl","setPlaceholder"],requireAll = false)
@SuppressLint("CheckResult")
fun AppCompatImageView.setImageUrl(imageUrl: String, placeHolder: Drawable) {

    val requestOptions = RequestOptions().apply {
        placeholder(placeHolder)
        error(placeHolder)
        diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(imageUrl)
        .into(this)
}
