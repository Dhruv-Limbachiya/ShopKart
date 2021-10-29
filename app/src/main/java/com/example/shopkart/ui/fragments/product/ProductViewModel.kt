package com.example.shopkart.ui.fragments.product

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
class ProductViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
    @ApplicationContext private val application: Context
) : BaseViewModel() {

    private var _response = MutableLiveData<Resource<Any>>()
    val response: LiveData<Resource<Any>> = _response

    /**
     * Gets the products from the Firestore db.
     */
    fun getProducts() {
        firebaseUtil.getProductsFromFireStore {
            _response.postValue(it)
        }
    }

    /**
     * Delete the specific product on Firestore,
     */
    fun deleteProduct(productId: String) {
        firebaseUtil.deleteProduct(productId) {
            _status.postValue(it)
        }
    }
}