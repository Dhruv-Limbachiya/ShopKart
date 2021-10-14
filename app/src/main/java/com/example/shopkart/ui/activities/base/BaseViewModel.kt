package com.example.shopkart.ui.activities.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkart.util.Resource

/**
 * Created By Dhruv Limbachiya on 13-10-2021 18:46.
 */
open class BaseViewModel : ViewModel() {
    var _status = MutableLiveData<Resource<String>>()
    val status: LiveData<Resource<String>> = _status
}