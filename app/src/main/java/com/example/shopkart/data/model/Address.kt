package com.example.shopkart.data.model

/**
 * Created By Dhruv Limbachiya on 09-11-2021 17:03.
 */

data class Address(
    val user_id: String = "",
    val name: String = "",
    val mobileNumber: String = "",
    val address: String = "",
    val zipCode: String = "",
    val additionalNote: String = "",
    val type: String = "",
    val otherDetails: String = "",
    var id: String = "",
)
