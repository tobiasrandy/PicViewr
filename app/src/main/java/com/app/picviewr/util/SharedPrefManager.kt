package com.app.picviewr.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PICVIEWR_DATA, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun saveLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun remove(key: String) {
        editor.remove(key)
        editor.apply()
    }

    fun clearData() {
        editor.clear().apply()
    }

    companion object {
        const val PICVIEWR_DATA = "PICVIEWR_DATA"
        const val LOGIN_KEY = "LOGIN_KEY"
        const val AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY"
        const val EMAIL_KEY = "EMAIL_KEY"
        const val NAME_KEY = "NAME_KEY"

        private var instance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (instance == null) {
                instance = SharedPrefManager(context.applicationContext)
            }
            return instance!!
        }
    }
}