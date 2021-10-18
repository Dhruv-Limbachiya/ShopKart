package com.example.shopkart

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Created By Dhruv Limbachiya on 14-10-2021 12:29.
 */
@HiltAndroidApp
class ShopKartApplication : Application() {

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
}