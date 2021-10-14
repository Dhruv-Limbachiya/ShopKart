package com.example.shopkart.ui.activities.registration

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.data.model.User
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 13-10-2021 12:34.
 */

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    var observableFirstName = ObservableString()
    var observableLastName = ObservableString()
    var observableEmail = ObservableString()
    var observablePassword = ObservableString()
    var observableConfirmPassword = ObservableString()
    var observableIsTermAndConditions = ObservableBoolean()

    /**
     * Method to validate user inputs for Registration screen.
     */
    private fun validateRegistrationDetails(): Boolean {
        if (observableFirstName.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter First Name"))
            return false
        } else if (observableLastName.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Last Name"))
            return false
        } else if (observableEmail.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Email"))
            return false
        } else if (observablePassword.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Password"))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(observableEmail.trimmed).matches()) {
            _status.postValue(Resource.Error("Please Enter Valid Email Id"))
            return false
        } else if (observablePassword.trimmed.length < 6) {
            _status.postValue(Resource.Error("Password should have at least 6 characters"))
            return false
        } else if (observableConfirmPassword.trimmed.isBlank()) {
            _status.postValue(Resource.Error("Please Enter Confirm Password"))
            return false
        } else if (observableConfirmPassword.trimmed.length < 6) {
            _status.postValue(Resource.Error("Password should have at least 6 characters"))
            return false
        } else if (observablePassword.trimmed != observableConfirmPassword.trimmed) {
            _status.postValue(Resource.Error("Password doesn't match."))
            return false
        } else if (!observableIsTermAndConditions.get()) {
            _status.postValue(Resource.Error("Please agree to the terms and conditions"))
            return false
        }
        return true
    }

    /**
     * Method to register user in firebase.
     */
    fun registerUser() {
        if (validateRegistrationDetails()) {
            _status.postValue(Resource.Loading())

            firebaseUtil.firebaseAuth.createUserWithEmailAndPassword(
                observableEmail.trimmed,
                observablePassword.trimmed
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserInFireStore()
                } else {
                    _status.postValue(Resource.Error(task.exception?.message.toString()))
                }
            }
        }
    }

    /**
     * Collect user info from the entered data and save the data in fireStore by delegating to the FirebaseUtil's saveUser() method.
     */
    private fun saveUserInFireStore() {
        val user = User(
            uid = firebaseUtil.firebaseAuth.uid,
            firstName = observableFirstName.trimmed,
            lastName = observableLastName.trimmed,
            email = observableEmail.trimmed
        )
        firebaseUtil.saveUser(user) {
            // Receive a callback after completion of save operation.
            // Post the status of operation in the _resource LiveData.
            _status.postValue(it)
            firebaseUtil.firebaseAuth.signOut() // Sign out the currently registered because user can only sign in using our login screen.
        }
    }
}

