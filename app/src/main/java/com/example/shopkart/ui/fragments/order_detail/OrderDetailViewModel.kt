package com.example.shopkart.ui.fragments.order_detail

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableLong
import com.example.shopkart.data.model.Order
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created By Dhruv Limbachiya on 23-11-2021 12:43 PM.
 */

class OrderDetailViewModel : BaseViewModel() {

    // Shipping Address
    var observableAddressType = ObservableString()
    var observableFullName = ObservableString()
    var observablePhoneNumber = ObservableString()
    var observableAddress = ObservableString()
    var observableAdditionalNote = ObservableString()
    var observableOtherDetails = ObservableString()
    var observableOtherDetailsVisibility = ObservableBoolean()

    // Item Receipt
    val observableSubTotal = ObservableString()
    val observableTotal = ObservableString()
    val observableShippingCharge = ObservableString()

    // Order details
    var observableOrderId = ObservableString()
    var observableOrderDate = ObservableLong()
    var observableOrderStatus = ObservableLong()


    /**
     * Set order details into observables.
     */
    fun setOtherDetailsIntoObservables(order: Order) {
        observableOrderId.set(order.title)
        observableOrderDate.set(order.orderDateTime)
        observableOrderStatus.set(order.orderDateTime) // Determine order status on the basis of order placed time.

        observableSubTotal.set(order.sub_total_amount)
        observableTotal.set(order.total_amount)
        observableShippingCharge.set(order.shipping_charge)

        observableAddressType.set(order.address?.type)
        observableFullName.set(order.address?.name)
        observableAdditionalNote.set(order.address?.additionalNote)
        observablePhoneNumber.set("+91 ${order.address?.mobileNumber}")
        observableAddress.set("${order.address?.address} - ${order.address?.zipCode}")

        // Display other details only if address type is other.
        if (order.address?.type == "Other") {
            observableOtherDetailsVisibility.set(true)
            observableOtherDetails.set(order.address.otherDetails)
        }
    }
}