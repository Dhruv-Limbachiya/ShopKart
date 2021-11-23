package com.example.shopkart.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Created By Dhruv Limbachiya on 22-11-2021 05:37 PM.
 */

@Parcelize
data class Order(
    val user_id: String = "",
    val items: @RawValue ArrayList<CartItem> = ArrayList(),
    val address: Address? = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val orderDateTime: Long = System.currentTimeMillis(),
    var id: String = ""
) : Parcelable
