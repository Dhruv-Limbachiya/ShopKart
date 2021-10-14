package com.example.shopkart.ui.activities.registration

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource

/**
 * Created By Dhruv Limbachiya on 13-10-2021 12:34.
 */
class RegistrationViewModel : BaseViewModel() {

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
            _resource.postValue(Resource.Error("Please Enter First Name"))
            return false
        } else if (observableLastName.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Last Name"))
            return false
        } else if (observableEmail.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Email"))
            return false
        } else if (observablePassword.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Password"))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(observableEmail.trimmed).matches()) {
            _resource.postValue(Resource.Error("Please Enter Valid Email Id"))
            return false
        } else if (observablePassword.trimmed.length < 6) {
            _resource.postValue(Resource.Error("Password should have at least 6 characters"))
            return false
        } else if (observableConfirmPassword.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Confirm Password"))
            return false
        } else if (observableConfirmPassword.trimmed.length < 6) {
            _resource.postValue(Resource.Error("Password should have at least 6 characters"))
            return false
        } else if (observablePassword.trimmed != observableConfirmPassword.trimmed) {
            _resource.postValue(Resource.Error("Password doesn't match."))
            return false
        } else if (!observableIsTermAndConditions.get()) {
            _resource.postValue(Resource.Error("Please agree to the terms and conditions"))
            return false
        }
        return true
    }

    /**
     * Method to register user in firebase.
     */
    fun registerUser() {
        if (validateRegistrationDetails()) {
            _resource.postValue(Resource.Loading())
            firebaseAuth.createUserWithEmailAndPassword(
                observableEmail.trimmed,
                observablePassword.trimmed
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.signOut() // Sign out the currently registered because user can only sign in using our login screen.
                    _resource.postValue(Resource.Success("Registration Successful"))
                } else {
                    _resource.postValue(Resource.Error(task.exception?.message.toString()))
                }
            }
        }
    }
}

