package com.example.shopkart.ui.fragments.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 03-11-2021 18:40.
 */

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    private var _cartItemStatus = MutableLiveData<Resource<List<CartItem>>>()
    val cartItemStatus: LiveData<Resource<List<CartItem>>> = _cartItemStatus

    val observableSubTotal = ObservableString()
    val observableTotal = ObservableString()
    val observableShippingCharge = ObservableString()

    init {
        loadCartItems()
    }

    /**
     * Loads all the cart items.
     */
    private fun loadCartItems() {
        firebaseUtil.getCartItems {
            _cartItemStatus.postValue(it)

            var subTotal = 0.0

            var total = 0.0

            it.data?.let { list ->
                for (cartItem in list) {
                    if(cartItem.stock_quantity.toInt() != 0) {
                        subTotal += cartItem.price.toDouble()
                            .times(cartItem.cart_quantity.toDouble())
                    }
                }
            }
            total = DEFAULT_SHIPPING_CHARGE + subTotal
            observableSubTotal.set(subTotal.toString())
            observableShippingCharge.set(DEFAULT_SHIPPING_CHARGE.toString())
            observableTotal.set(total.toString())
        }
    }

    /**
     * Invokes FirebaseUtil's removeCartItemOnFireStoreDb() method to remove specified cart item in cart_collections.
     */
    fun removeCartItem(cartItemId: String) {
        firebaseUtil.removeCartItemOnFireStoreDb(cartItemId) {
            _status.postValue(it)
            refreshCartItemList()
        }
    }

    /**
     * Updates cart item documents with the latest data.
     */
    fun updateCartItemData(id: String, data: HashMap<String, Any>) {
        firebaseUtil.updateCart(id,data) {
            _status.postValue(it)
            refreshCartItemList()
        }
    }

    /**
     * Refresh the cart items data.
     */
    private fun refreshCartItemList() {
        loadCartItems()
    }




    companion object {
        const val DEFAULT_SHIPPING_CHARGE = 40.0
    }
}