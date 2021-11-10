package com.example.shopkart.ui.fragments.my_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.Address
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 10-11-2021 10:16 AM.
 */

@HiltViewModel
class MyAddressViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
) : BaseViewModel() {

    private var _addressList = MutableLiveData<Resource<List<Address>>>()
    val addressList: LiveData<Resource<List<Address>>> = _addressList

    init {
        getMyAddresses()
    }

    /**
     * Get user addresses
     */
    fun getMyAddresses() {
        firebaseUtil.getMyAddressesFromFireStore {
            _addressList.postValue(it)
        }
    }

    /**
     * Delete user address.
     */
    fun deleteAddress(id: String) {
        firebaseUtil.deleteAddressOnFireStoreDb(id) {
            _status.postValue(it)
        }
    }
}