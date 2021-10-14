package com.example.shopkart.data.firebase

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Created By Dhruv Limbachiya on 14-10-2021 11:38.
 */

class FirebaseUtil {

    // Get the FirebaseAuth instance for authentication like login, sign up, forgot password & sign out.
    val firebaseAuth = FirebaseAuth.getInstance()

    // Get the Firebase Firestore instance for storing & retrieving data.
    val fireStore = Firebase.firestore
}