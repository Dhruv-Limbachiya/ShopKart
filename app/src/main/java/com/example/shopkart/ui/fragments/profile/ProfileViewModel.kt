package com.example.shopkart.ui.fragments.profile

import androidx.databinding.ObservableBoolean
import com.example.shopkart.R
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import com.example.shopkart.util.SharePreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil,
    private val sharedPreferenceUtil: SharePreferenceUtil
) : BaseViewModel() {

    var observableFirstName = ObservableString()
    var observableLastName = ObservableString()
    var observableEmail = ObservableString()
    var observableMobile = ObservableString()
    var observableGender = ObservableBoolean()


    init {
        observableFirstName.set(sharedPreferenceUtil.getString(R.string.prefFirstName))
        observableLastName.set(sharedPreferenceUtil.getString(R.string.prefLastName))
        observableEmail.set(sharedPreferenceUtil.getString(R.string.prefEmail))
    }

    /**
     * Validates the user input for Profile screen.
     */
    private fun validateProfileDetails(): Boolean {
        if (observableFirstName.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your First Name"))
            return false
        } else if (observableLastName.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your Last Name"))
            return false
        } else if (observableMobile.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Your Mobile Number"))
            return false
        } else if (observableMobile.trimmed.length != 10) {
            _status.postValue(Resource.Error("Please Enter Valid Mobile Number"))
            return false
        } else if (!observableGender.get()) {
            _status.postValue(Resource.Error("Please Select Your Gender"))
            return false
        }
        return true
    }


    /**
     * Saves user profile on profile button click.
     */
    fun onProfileButtonClick() {
        if (validateProfileDetails()) {

            val userHashMap = HashMap<String, Any>()

            // Check user has changed its firstName if that is the case then update firstName in fireStore by adding its value in HashMap.
            if (sharedPreferenceUtil.getString(R.string.prefFirstName) != (observableFirstName.trimmed)) {
                userHashMap[KEY_FIRSTNAME] = observableFirstName.trimmed
            }

            // Check user has changed its lastName if that is the case then update lastName in fireStore  by adding its value in HashMap.
            if (sharedPreferenceUtil.getString(R.string.prefLastName) != (observableLastName.trimmed)) {
                userHashMap[KEY_LASTNAME] = observableLastName.trimmed
            }

            userHashMap[KEY_MOBILE] = observableMobile.trimmed

            userHashMap[KEY_GENDER] = if (observableGender.get()) {
                MALE
            } else {
                FEMALE
            }

            firebaseUtil.updateUserProfile(userHashMap) {
                _status.postValue(it)
                when (it) {
                    is Resource.Success -> {
                        updateUserSharedPreference(userHashMap)
                    }
                    else -> {
                        /* NO OPERATION */
                    }
                }
            }

        }
    }

    /**
     * Update the user profile details in SharedPreferences.
     */
    private fun updateUserSharedPreference(userHashMap: HashMap<String, Any>) {
        sharedPreferenceUtil.setString(R.string.prefFirstName, observableFirstName.trimmed)
        sharedPreferenceUtil.setString(R.string.prefLastName, observableLastName.trimmed)
        sharedPreferenceUtil.setString(R.string.prefMobile, observableMobile.trimmed)
        sharedPreferenceUtil.setString(R.string.prefEmail, observableEmail.trimmed)
        sharedPreferenceUtil.setString(R.string.prefGender, userHashMap[KEY_GENDER].toString())
    }


    companion object {
        const val KEY_FIRSTNAME = "firstName"
        const val KEY_LASTNAME = "lastName"
        const val KEY_GENDER = "gender"
        const val KEY_MOBILE = "mobile"
        const val MALE = "male"
        const val FEMALE = "female"
    }
}