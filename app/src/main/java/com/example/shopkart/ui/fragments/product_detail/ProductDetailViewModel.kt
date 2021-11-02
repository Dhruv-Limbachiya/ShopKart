package com.example.shopkart.ui.fragments.product_detail

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Product
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 01-11-2021.
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
) : BaseViewModel() {

    val observableProductImageUri = ObservableString()
    val observableProductTitle = ObservableString()
    val observableProductPrice = ObservableString()
    val observableProductDescription = ObservableString()
    val observableProductStockQuantity = ObservableString()

    val observableGoToCartVisible = ObservableBoolean()

    private var _response = MutableLiveData<Resource<Any>>()
    val response: LiveData<Resource<Any>> = _response


    /**
     * Make a firebase query call to get the product details.
     */
    fun getProductDetails(productId: String) {
        firebaseUtil.getProductDetailsById(productId) {
            when (it.data) {
                is Product -> {
                    setProductDetails(it.data)
                }
            }

            _response.postValue(it)
        }
    }

    /**
     * Triggers on "Go To Cart" button click. Responsible to upload cart item data on Firestore db.
     */
    fun onAddToCartButtonClick(cartItem: CartItem) {
        firebaseUtil.uploadCartItem(cartItem) {
            _status.postValue(it)
        }
    }

    /**
     * Make a call to Firestore to check if the product is already added or not.
     */
    fun checkProductAlreadyExistInCartItemInFireStore(productId: String) {
        firebaseUtil.checkProductAlreadyExist(productId) {
            _statusBool.postValue(it)
        }
    }

    /**
     * Set data to all the observables.
     */
    private fun setProductDetails(data: Product) {
        observableProductImageUri.set(data.image)
        observableProductTitle.set(data.title)
        observableProductPrice.set(data.price)
        observableProductDescription.set(data.description)
        observableProductStockQuantity.set(data.stock_quantity)
    }
}