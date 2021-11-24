package com.example.shopkart.ui.fragments.checkout

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.Address
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Order
import com.example.shopkart.data.model.Product
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.ui.fragments.cart.CartViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 22-11-2021 10:53 AM.
 */

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

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

    // List
    private var _cartItemStatus = MutableLiveData<Resource<List<CartItem>>>()
    val cartItemStatus: LiveData<Resource<List<CartItem>>> = _cartItemStatus

    private val products = mutableListOf<Product>()

    private val cartItems = mutableListOf<CartItem>()

    var address: Address? = null

    init {
        getProducts()
        getCartItems()
    }

    /**
     * Set the address details into view observables.
     */
    fun setCheckoutAddressDetails(address: Address) {

        this.address = address

        observableAddressType.set(address.type)
        observableFullName.set(address.name)
        observablePhoneNumber.set("+91 ${address.mobileNumber}")
        observableAddress.set("${address.address} - ${address.zipCode}")
        observableAdditionalNote.set(address.additionalNote)

        // Display other details only if address type is other.
        if (address.type == "Other") {
            observableOtherDetailsVisibility.set(true)
            observableOtherDetails.set(address.otherDetails)
        }
    }

    /**
     * Loads all the cart items.
     */
    private fun getCartItems() {
        firebaseUtil.getCartItems {
            _cartItemStatus.postValue(it)
            it.data?.let {
                val data = it
                setItemReceipt(data)
            }
        }
    }

    /**
     * Calculates the subtotal, shipping charge and total amount to make receipt.
     */
    private fun setItemReceipt(data: List<CartItem>) {
        if (data.isNotEmpty()) {
            cartItems.addAll(data)

            if (products.isNotEmpty()) {
                for (product in products) {
                    for (cartItem in cartItems) {
                        if (product.id == cartItem.productId) {
                            cartItem.stock_quantity = product.stock_quantity
                        }
                    }
                }
            }

            var subTotal = 0.0
            data.let { list ->
                for (cartItem in list) {
                    if (cartItem.stock_quantity.toInt() != 0) {
                        subTotal += cartItem.price.toDouble()
                            .times(cartItem.cart_quantity.toDouble())
                    }
                }
            }
            val total: Double = CartViewModel.DEFAULT_SHIPPING_CHARGE + subTotal
            observableSubTotal.set(subTotal.toString())
            observableShippingCharge.set(CartViewModel.DEFAULT_SHIPPING_CHARGE.toString())
            observableTotal.set(total.toString())
        }
    }


    /**
     * Gets the products from the Firestore db.
     */
    private fun getProducts() {
        firebaseUtil.getProductsFromFireStore {
            it.data?.let { productList ->
                val data = productList as List<Product>
                if (data.isNotEmpty()) {
                    products.addAll(data)
                }
            }
        }
    }

    /**
     * Places user order by storing order details on Firestore db.
     */
    fun placeOrder() {
        firebaseUtil.firebaseAuth.currentUser?.uid?.let {
            val order = Order(
                it,
                cartItems as ArrayList<CartItem>,
                address ?: Address(),
                "My order ${System.currentTimeMillis()}",
                cartItems[0].image,
                observableSubTotal.trimmed,
                observableShippingCharge.trimmed,
                observableTotal.trimmed
            )

            firebaseUtil.uploadOrderDetails(order) {
                _status.postValue(it)
            }
        }
    }

}