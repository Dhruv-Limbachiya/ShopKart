package com.example.shopkart.ui.fragments.address_detail

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.Address
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 09-11-2021 16:28.
 */
@HiltViewModel
class AddressDetailViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
    @ApplicationContext private val application: Context
) : BaseViewModel() {

    var observableFullName = ObservableString()
    var observablePhoneNumber = ObservableString()
    var observableAddress = ObservableString()
    var observableZipCode = ObservableString()
    var observableAdditionalNote = ObservableString()
    var observableTypeHome = ObservableBoolean(true)
    var observableTypeOffice = ObservableBoolean()
    var observableTypeOther = ObservableBoolean()
    var observableOther = ObservableString()


    /**
     * Submits the address details on Firestore db.
     */
    fun onSubmitButtonClick() {
        if (validateAddressDetails()) {
            prepareAndUploadAddressDetails()
        }
    }

    /**
     * Function prepares and upload the address data on FireStore.
     */
    private fun prepareAndUploadAddressDetails() {
        firebaseUtil.firebaseAuth.currentUser?.uid?.let { uid ->

            // Address object to upload.
            val address = Address(
                uid ,
                observableFullName.trimmed,
                observablePhoneNumber.trimmed,
                observableAddress.trimmed,
                observableZipCode.trimmed,
                observableAdditionalNote.trimmed,
                getAddressType(),
                observableOther.trimmed
            )

            // Makes Firebase call to upload address data.
            firebaseUtil.uploadAddressDetails(address) {
                _status.postValue(it)
            }
        }
    }

    /**
     * Helper method to get user selected address type.
     */
    private fun getAddressType() = when {
        observableTypeHome.get() -> "Home"
        observableTypeOffice.get() -> "Office"
        observableTypeOther.get() -> "Other"
        else -> "Home"
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

}