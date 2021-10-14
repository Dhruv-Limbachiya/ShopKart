package com.example.shopkart.ui.activities.login

import android.util.Patterns
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 13-10-2021 18:45.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    var observableEmail = ObservableString()
    var observablePassword = ObservableString()

    /**
     * Method to validate user inputs for Login screen.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            observableEmail.trimmed.isBlank() -> {
                _resource.postValue(Resource.Error("Please Enter Email"))
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(observableEmail.trimmed).matches() -> {
                _resource.postValue(Resource.Error("Please Enter Valid Email Id"))
                false
            }
            observablePassword.trimmed.isBlank() -> {
                _resource.postValue(Resource.Error("Please Enter Password"))
                false
            }
            observablePassword.trimmed.length < 6 -> {
                _resource.postValue(Resource.Error("Password should have at least 6 characters"))
                false
            }
            else -> true
        }
    }

    /**
     * Method to login the registered user.
     */
    fun loginRegisteredUser() {
        if (validateLoginDetails()) {
            _resource.postValue(Resource.Loading())
            firebaseAuth.signInWithEmailAndPassword(
                observableEmail.trimmed,
                observablePassword.trimmed
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resource.postValue(Resource.Success("Login Successful"))
                } else {
                    _resource.postValue(Resource.Error(task.exception?.message.toString()))
                }
            }
        }
    }
}