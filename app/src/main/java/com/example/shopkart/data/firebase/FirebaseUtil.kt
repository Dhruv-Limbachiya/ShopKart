package com.example.shopkart.data.firebase

import android.util.Log
import com.example.shopkart.data.model.User
import com.example.shopkart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

/**
 * Created By Dhruv Limbachiya on 14-10-2021 11:38.
 */

class FirebaseUtil {



    // Get the FirebaseAuth instance for authentication like login, sign up, forgot password & sign out.
    val firebaseAuth = FirebaseAuth.getInstance()

    // Get the Firebase Firestore instance for storing & retrieving data.
    private val fireStoreDb = Firebase.firestore

    /**
     * Method to save the registered user info into Cloud Firestore.
     */
    fun saveUser(user: User, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb.collection(USER_COLLECTION)
            .document(user.uid ?: Random.toString())
            .set(user,SetOptions.merge()) // set user data in the given document, if it is already existed then merge the new data with the existing one.
            .addOnSuccessListener {
                onResponse(Resource.Success("Registration Successful"))
                Log.i(TAG, "saveUser: Data saved")
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
                Log.i(TAG, "saveUser: ${it.message.toString()}")
            }
    }

    companion object {
        const val USER_COLLECTION = "users"
        const val TAG = "FirebaseUtil"
    }
}