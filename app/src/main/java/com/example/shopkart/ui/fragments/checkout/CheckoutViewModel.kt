package com.example.shopkart.ui.fragments.checkout

import androidx.databinding.ObservableBoolean
import com.example.shopkart.data.model.Address
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString

/**
 * Created By Dhruv Limbachiya on 22-11-2021 10:53 AM.
 */
class CheckoutViewModel : BaseViewModel() {

    var observableAddressType = ObservableString()
    var observableFullName = ObservableString()
    var observablePhoneNumber = ObservableString()
    var observableAddress = ObservableString()
    var observableAdditionalNote = ObservableString()
    var observableOtherDetails = ObservableString()
    var observableOtherDetailsVisibility = ObservableBoolean()

    /**
     * Set the address details into view observables.
     */
    fun setCheckoutAddressDetails(address: Address) {
        observableAddressType.set(address.type)
        observableFullName.set(address.name)
        observablePhoneNumber.set("+91 ${address.mobileNumber}")
        observableAddress.set("${address.address} - ${address.zipCode}")
        observableAdditionalNote.set(address.additionalNote)

        // Display other details only if address type is other.
        if(address.type == "Other") {
            observableOtherDetailsVisibility.set(true)
            observableOtherDetails.set(address.otherDetails)
        }

    }
}