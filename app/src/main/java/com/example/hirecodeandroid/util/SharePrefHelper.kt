package com.example.hirecodeandroid.util

import android.content.Context
import android.content.SharedPreferences

class SharePrefHelper(private val context: Context) {
    companion object {
        const val SHARED_PREF_NAME = "HIREC0D3"
        const val KEY_EMAIL = "EMAIL_HC"
        const val KEY_PASSWORD = "PASSWORD_HC"
        const val KEY_LOGIN = "LOGIN"
        const val AC_LEVEL = "AC_LEVEL"
        const val AC_NAME = "AC_NAME"
        const val TOKEN = "TOKEN"
        const val AC_ID = "ACID"
        const val ENG_ID = "ENG_ID"
        const val COM_ID = "COM_ID"
        const val ENG_NAME = "ENG_NAME"
        const val ENG_ID_CLICKED = "ENG_ID_CLICKED"
        const val PROJECT_ID_SELECTED = "PROJECT_ID_SELECTED"
        const val JOB_TYPE = "JOB_TYPE"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()


    fun put(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }


    //    BOOLEAN
    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    //    INTEGER
    fun getInteger(key: String): Int {
        return sharedPref.getInt(key, 0)
    }

    fun put(key: String, value: Int) {
        editor.putInt(key, value)
            .apply()
    }

    //    CLEAR
    fun clear() {
        editor.clear()
            .apply()
    }


}