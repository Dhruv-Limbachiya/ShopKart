package com.example.shopkart.ui.fragments.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 29-10-2021.
 */

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
) : BaseViewModel() {

    private var _response = MutableLiveData<Resource<Any>>()
    val response: LiveData<Resource<Any>> = _response


    /**
     * Gets all the products from the Firestore db.
     */
    fun getAllProducts() {
        firebaseUtil.getAllProductsFromFireStore {
            _response.postValue(it)
        }
    }
}