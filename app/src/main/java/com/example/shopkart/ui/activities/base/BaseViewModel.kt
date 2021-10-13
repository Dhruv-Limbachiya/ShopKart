package com.example.shopkart.ui.activities.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkart.util.Resource
import com.google.firebase.auth.FirebaseAuth

/**
 * Created By Dhruv Limbachiya on 13-10-2021 18:46.
 */
open class BaseViewModel : ViewModel() {

    var _resource = MutableLiveData<Resource<String>>()
    val resource: LiveData<Resource<String>> = _resource

    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
    }
}