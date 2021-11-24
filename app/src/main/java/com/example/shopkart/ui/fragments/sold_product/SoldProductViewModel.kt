package com.example.shopkart.ui.fragments.sold_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.SoldProduct
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 24-11-2021 11:17 AM.
 */

@HiltViewModel
class SoldProductViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    // List
    private var _soldProductStatus = MutableLiveData<Resource<List<SoldProduct>>>()
    val soldProductStatus: LiveData<Resource<List<SoldProduct>>> = _soldProductStatus


    /**
     * Get the list of my sold products.
     */
    fun getMySoldProducts() {
        firebaseUtil.getMySoldProducts {
            _soldProductStatus.postValue(it)
        }
    }
}