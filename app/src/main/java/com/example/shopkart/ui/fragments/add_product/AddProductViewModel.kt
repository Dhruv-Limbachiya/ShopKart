package com.example.shopkart.ui.fragments.add_product

import androidx.core.net.toUri
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import com.example.shopkart.R
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.ui.fragments.profile.ProfileViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource

/**
 * Created by Dhruv Limbachiya on 28-10-2021.
 */
class AddProductViewModel : BaseViewModel() {

    var observableProductImageUri = ObservableString()
    var observableProductTitle = ObservableString()
    var observableProductPrice = ObservableString()
    var observableProductDescription = ObservableString()
    var observableProductQuantity = ObservableString()


    /**
     * Validates the user input for Add Product screen.
     */
    private fun validateProductDetails(): Boolean {
        when {
            observableProductImageUri.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Choose Your Product Image"))
                return false
            }
            observableProductTitle.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Enter Product Title"))
                return false
            }
            observableProductPrice.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Enter Product Price"))
                return false
            }
            observableProductPrice.trimmed == "0" -> {
                _status.postValue(Resource.Error("Please Enter Valid Product Price"))
                return false
            }
            observableProductDescription.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Enter Product Description"))
                return false
            }
            observableProductQuantity.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Enter Product Description"))
                return false
            }
            observableProductQuantity.trimmed == "0" -> {
                _status.postValue(Resource.Error("Please Enter Valid Number for Product Quantity"))
                return false
            }
            else -> return true
        }
    }

    /**
     * Saves the product in the fire store database.
     */
    fun onProductSaveButtonClick() {
        if (validateProductDetails()) {
            _status.postValue(Resource.Success("Product data saved."))
        }
    }

}