package com.example.shopkart.util

import android.content.Context
import com.example.shopkart.R

class SharePreferenceUtil (val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_name),0)

    fun getBoolean(prefName: Int): Boolean {
        return sharedPreferences.getBoolean(context.resources.getString(prefName), false)
    }

    fun getBooleanDefault(prefName: Int, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(context.resources.getString(prefName), defaultValue)
    }

    fun setBoolean(prefName: Int, value: Boolean) {
        sharedPreferences.edit().putBoolean(context.resources.getString(prefName), value).apply()
    }

    fun getString(prefName: Int): String? {
        return sharedPreferences.getString(context.resources.getString(prefName), "")
    }

    fun getStringDefault(prefName: Int, defaultValue: String?): String? {
        return sharedPreferences.getString(context.resources.getString(prefName), defaultValue)
    }

    fun setString(prefName: Int, value: String?) {
        sharedPreferences.edit().putString(context.resources.getString(prefName), value).apply()
    }

    fun setString(prefName: String, value: String?) {
        sharedPreferences.edit().putString(prefName, value).apply()
    }

    fun getInteger(prefName: Int): Int {
        return sharedPreferences.getInt(context.resources.getString(prefName), 0)
    }

    fun getIntegerDefault(prefName: Int, defaultValue: Int): Int {
        return sharedPreferences.getInt(context.resources.getString(prefName), defaultValue)
    }

    fun setInteger(prefName: Int, value: Int) {
        sharedPreferences.edit().putInt(context.resources.getString(prefName), value).apply()
    }


    fun getLong(prefName: Int): Long {
        return sharedPreferences.getLong(context.resources.getString(prefName), 0L)
    }

    fun getLongDefault(prefName: Int, defaultValue: Long): Long {
        return sharedPreferences.getLong(context.resources.getString(prefName), defaultValue)
    }

    fun setLong(prefName: Int, value: Long) {
        sharedPreferences.edit().putLong(context.resources.getString(prefName), value).apply()
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}