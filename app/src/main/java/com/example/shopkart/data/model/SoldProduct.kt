package com.example.shopkart.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created By Dhruv Limbachiya on 24-11-2021 10:33 AM.
 */

@Parcelize
data class SoldProduct(
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",
    val order_id: String = "",
    val order_date: Long = 0L,
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val address: Address = Address(),
    var id: String = "",
) : Parcelable
