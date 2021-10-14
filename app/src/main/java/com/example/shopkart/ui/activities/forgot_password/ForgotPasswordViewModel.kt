package com.example.shopkart.ui.activities.forgot_password

import android.util.Patterns
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Dhruv Limbachiya on 14-10-2021 14:30.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    var observableEmail = ObservableString()

    /**
    * Method to validate user inputs for Login screen.
    */
    private fun validateEmail(): Boolean {
        return when {
            observableEmail.trimmed.isBlank() -> {
                _status.postValue(Resource.Error("Please Enter Email"))
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(observableEmail.trimmed).matches() -> {
                _status.postValue(Resource.Error("Please Enter Valid Email Id"))
                false
            }
            else -> true
        }
    }

    /**
     * Method responsible for sending an email to reset the user password.
     */
    fun onForgotPasswordTextClick() {
        if (validateEmail()) {
            _status.postValue(Resource.Loading())
            firebaseUtil.firebaseAuth.sendPasswordResetEmail(
                observableEmail.trimmed,
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _status.postValue(Resource.Success("Mail sent.Check your inbox."))
                } else {
                    _status.postValue(Resource.Error(task.exception?.message.toString()))
                }
            }
        }
    }
}