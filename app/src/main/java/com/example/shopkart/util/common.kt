package com.example.shopkart.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.shopkart.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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


@BindingAdapter("setOrderDate")
fun AppCompatTextView.setOrderDate(timeInMillis: Long) {
    val dateFormat = "dd-MMM-yyyy HH:mm"
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    this.text = formatter.format(calendar.time)
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("setOrderStatus")
fun AppCompatTextView.setOrderStatus(timeInMillis: Long) {
    val diffInMillis = System.currentTimeMillis() - timeInMillis
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis) // return hours from the millis

    var status = context.getString(R.string.status_pending)
    var color: ColorStateList = context.getColorStateList(R.color.gray)

    when {
        diffInHours < 1 -> {
            // Pending
            status = context.getString(R.string.status_pending)
            color = context.getColorStateList(R.color.colorOrderStatusPending)
        }

        diffInHours < 2 -> {
            // assume the product is dispatched. Hence,show delivery is in progress.
            status = context.getString(R.string.status_in_progress)
            color = context.getColorStateList(R.color.colorOrderStatusInProcess)
        }

        diffInHours < 3 -> {
            // assume the product delivered. Hence,show delivered as status.
            status = context.getString(R.string.status_delivered)
            color = context.getColorStateList(R.color.colorOrderStatusDelivered)
        }
    }

    this.text = status
    this.setTextColor(color)
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * Utility function to get the extension from the uri.
 */
fun getExtension(imageUri: Uri, context: Context) =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(imageUri))


@BindingAdapter(value = ["setImageUrl", "setPlaceholder"], requireAll = false)
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
