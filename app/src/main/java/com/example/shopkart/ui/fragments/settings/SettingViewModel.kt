package com.example.shopkart.ui.fragments.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.shopkart.R
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
    private val sharedPreferenceUtil: SharePreferenceUtil
) : ViewModel(){

    var observableImage = ObservableString()
    var observableFullName = ObservableString()
    var observableEmail = ObservableString()
    var observableMobile = ObservableString()
    var observableGender = ObservableString()


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
}