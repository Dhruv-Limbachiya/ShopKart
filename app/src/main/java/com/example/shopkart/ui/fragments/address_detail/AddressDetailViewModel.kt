package com.example.shopkart.ui.fragments.address_detail

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.example.shopkart.R
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.Address
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import com.example.shopkart.util.SharePreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 09-11-2021 16:28.
 */
@HiltViewModel
class AddressDetailViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
    private val sharePreferenceUtil: SharePreferenceUtil,
    @ApplicationContext private val application: Context
) : BaseViewModel() {

    private var address: Address? = null

    var observableFullName = ObservableString()
    var observablePhoneNumber = ObservableString()
    var observableAddress = ObservableString()
    var observableZipCode = ObservableString()
    var observableAdditionalNote = ObservableString()
    var observableTypeHome = ObservableBoolean()
    var observableTypeOffice = ObservableBoolean()
    var observableTypeOther = ObservableBoolean()
    var observableOther = ObservableString()
    var observableButtonName = ObservableString()

    /**
     * Submits the address details on Firestore db.
     */
    fun onSubmitButtonClick() {
        if (validateAddressDetails()) {
            if (address != null) {
                // Update the latest address on Firestore.
                updateAddressDetails()
            } else {
                // Add new address details on Firestore.
                addAddressDetails()
            }
        }
    }

    /**
     * Update the address detail.
     */
    private fun updateAddressDetails() {
        firebaseUtil.updateAddressDetailOnFireStore(getPreparedAddressInstance()) {
            _status.postValue(it)
        }
    }

    /**
     * Function prepares and upload the address data on FireStore.
     */
    private fun addAddressDetails() {
        // Makes Firebase call to upload address data.
        firebaseUtil.uploadAddressDetails(getPreparedAddressInstance()) {
            _status.postValue(it)
        }
    }


    /**
     * Filled address info into views for Edit operation.
     */
    fun setAddressInfo(address: Address) {
        this.address = address

        observableFullName.set(address.name)
        observablePhoneNumber.set(address.mobileNumber)
        observableAddress.set(address.address)
        observableZipCode.set(address.zipCode)
        observableAdditionalNote.set(address.additionalNote)

        when (address.type) {
            ADDRESS_TYPE_HOME -> observableTypeHome.set(true)
            ADDRESS_TYPE_OFFICE -> observableTypeOffice.set(true)
            ADDRESS_TYPE_OTHER -> {
                observableTypeOther.set(true)
                observableOther.set(address.otherDetails)
            }
            else -> ADDRESS_TYPE_HOME
        }
    }


    /**
     * Creates and return address instance with all the details.
     */
    private fun getPreparedAddressInstance() = Address(
        sharePreferenceUtil.getString(R.string.prefUserId).toString(),
        observableFullName.trimmed,
        observablePhoneNumber.trimmed,
        observableAddress.trimmed,
        observableZipCode.trimmed,
        observableAdditionalNote.trimmed,
        getAddressType(),
        if (observableTypeOther.get()) {
            observableOther.trimmed
        } else "",
        address?.id ?: ""
    )

    /**
     * Helper method to get user selected address type.
     */
    private fun getAddressType() = when {
        observableTypeHome.get() -> ADDRESS_TYPE_HOME
        observableTypeOffice.get() -> ADDRESS_TYPE_OFFICE
        observableTypeOther.get() -> ADDRESS_TYPE_OTHER
        else -> ADDRESS_TYPE_HOME
    }

    /**
     * Validates the user input for Address Detail Screen.
     */
    private fun validateAddressDetails(): Boolean {
        if (observableFullName.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your Full Name"))
            return false
        } else if (observablePhoneNumber.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your Phone Number"))
            return false
        } else if (observablePhoneNumber.trimmed.length != 10) {
            _status.postValue(Resource.Error("Please Enter Valid Phone Number"))
            return false
        } else if (observableAddress.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your Address"))
            return false
        } else if (observableZipCode.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your ZipCode or Pincode"))
            return false
        } else if (observableAdditionalNote.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Additional Note"))
            return false
        } else if (!observableTypeHome.get() && !observableTypeOffice.get() && !observableTypeOther.get()) {
            _status.postValue(Resource.Error("Please Select Your Address type"))
            return false
        } else if (observableTypeOther.get() && observableOther.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Other Details"))
            return false
        }
        return true
    }

    companion object {
        const val ADDRESS_TYPE_HOME = "Home"
        const val ADDRESS_TYPE_OFFICE = "Office"
        const val ADDRESS_TYPE_OTHER = "Other"
    }
}