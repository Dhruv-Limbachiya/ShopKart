package com.example.shopkart.ui.activities.registration

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.Resource

/**
 * Created By Dhruv Limbachiya on 13-10-2021 12:34.
 */
class RegistrationViewModel : ViewModel() {

    var observableName = ObservableString()
    var observableEmail = ObservableString()
    var observablePassword = ObservableString()
    var observableConfirmPassword = ObservableString()
    var observableIsTermAndConditions = ObservableBoolean()

    private var _resource = MutableLiveData<Resource<String>>()
    val resource : LiveData<Resource<String>> = _resource

    /**
     * Method to validate user inputs for Registration screen.
     */
    private fun validateRegistrationDetails() : Boolean {
        if(observableName.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Name"))
            return false
        }
        else if(observableEmail.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Email"))
            return false
        }
        else if(observablePassword.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Password"))
            return false
        }
        else if(observableConfirmPassword.trimmed.isBlank()) {
            _resource.postValue(Resource.Error("Please Enter Confirm Password"))
            return false
        }
        else if(observablePassword.trimmed != observableConfirmPassword.trimmed) {
            _resource.postValue(Resource.Error("Password doesn't match."))
            return false
        }
        else if(!observableIsTermAndConditions.get()){
           _resource.postValue(Resource.Error("Please agree to the terms and conditions"))
            return false
        }
        return true
    }

    /**
     * Method to register user in firebase.
     */
    fun registerUser() {
        if(validateRegistrationDetails()) {
            _resource.postValue(Resource.Success("Registration Successful"))
        }
    }
}

