package com.example.shopkart.data.model

/**
 * Created By Dhruv Limbachiya on 14-10-2021 11:38.
 */


/**
 *  Data class to represent our app user info.
 */
data class User(
    var uid: String? = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var image: String = "",
    var mobile: String = "",
    var gender: String = "",
    var profileCompleted: Int = 0
)