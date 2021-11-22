package com.example.shopkart.data.model

/**
 * Created By Dhruv Limbachiya on 22-11-2021 05:37 PM.
 */
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address? = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    var id: String = ""
)
