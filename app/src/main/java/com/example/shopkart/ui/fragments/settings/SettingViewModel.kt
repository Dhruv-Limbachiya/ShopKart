package com.example.shopkart.ui.fragments.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkart.R
import com.example.shopkart.data.firebase.FirebaseUtil
import com.example.shopkart.ui.fragments.profile.ProfileViewModel
import com.example.shopkart.util.ObservableString
import com.example.shopkart.util.SharePreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Dhruv Limbachiya on 26-10-2021.
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreferenceUtil: SharePreferenceUtil,
    private val firebaseUtil: FirebaseUtil
) : ViewModel(){

    var observableImage = ObservableString()
    var observableFullName = ObservableString()
    var observableEmail = ObservableString()
    var observableMobile = ObservableString()
    var observableGender = ObservableString()

    // Tracks the edit button clicks
    private var _onEditClick = MutableLiveData<Boolean>()
    val onEditClick: LiveData<Boolean> = _onEditClick

    // Tracks the logout button clicks.
    private var _onLogoutClick = MutableLiveData<Boolean>()
    val onLogoutClick: LiveData<Boolean> = _onLogoutClick

    init {
        initObservables()
    }

    /**
     * Initializes all the observables with shared preference values.
     */
    private fun initObservables() {
        observableImage.set(sharedPreferenceUtil.getString(R.string.prefProfileImagePath))
        observableFullName.set(sharedPreferenceUtil.getString(R.string.prefFullName))
        observableEmail.set(sharedPreferenceUtil.getString(R.string.prefEmail))
        observableMobile.set(sharedPreferenceUtil.getString(R.string.prefMobile))
        observableGender.set(sharedPreferenceUtil.getString(R.string.prefGender))
    }

    /**
     * Invokes when user tap on edit button.
     */
    fun onEditButtonClick() {
        _onEditClick.postValue(true)
    }

    /**
     * Invokes when user tap on logout button.
     */
    fun onLogoutButtonClick() {
        firebaseUtil.firebaseAuth.signOut()
        _onLogoutClick.postValue(true)
    }

    /**
     * Method will reset all the click events
     */
    fun resetClicks() {
        _onEditClick.postValue(false)
        _onLogoutClick.postValue(false)
    }
}