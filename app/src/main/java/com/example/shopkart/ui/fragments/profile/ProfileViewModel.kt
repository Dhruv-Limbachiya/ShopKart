package com.example.shopkart.ui.fragments.profile

import com.example.shopkart.R
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.activities.base.BaseViewModel
import com.example.shopkart.util.ObservableString
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


    init {
        observableFirstName.set(sharedPreferenceUtil.getString(R.string.prefFirstName))
        observableLastName.set(sharedPreferenceUtil.getString(R.string.prefLastName))
        observableEmail.set(sharedPreferenceUtil.getString(R.string.prefEmail))
    }
}