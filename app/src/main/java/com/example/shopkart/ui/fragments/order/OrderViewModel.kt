package com.example.shopkart.ui.fragments.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Order
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 23-11-2021 11:35 AM.
 */

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel()  {

    // List
    private var _orderStatus = MutableLiveData<Resource<List<Order>>>()
    val orderStatus: LiveData<Resource<List<Order>>> = _orderStatus


    /**
     * Get the user placed order list.
     */
    fun getMyOrders() {
        firebaseUtil.getMyOrders {
            _orderStatus.postValue(it)
        }
    }
}