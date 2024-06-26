package com.emrecura.easyshop.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
    }

    // Save the auth token and its expiration time
    fun saveAuthToken(token: String, expireTimeMillis: Long) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            putLong("auth_token_expire_time", expireTimeMillis)
            apply()
        }
    }

    // Fetch the auth token
    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    // Check if the token is still valid
    fun isTokenValid(): Boolean {
        val expireTimeMillis = sharedPreferences.getLong("auth_token_expire_time", -1)
        if (expireTimeMillis == -1L) {
            return false // Token is considered invalid if expiration time is not available
        }
        val currentTimeMillis = System.currentTimeMillis()
        return currentTimeMillis < expireTimeMillis
    }

    // Clear the auth token and its expiration time
    fun clearAuthToken() {
        with(sharedPreferences.edit()) {
            remove("auth_token")
            remove("auth_token_expire_time")
            apply()
        }
    }

    // Save the user ID
    fun saveUserId(userId: Long) {
        with(sharedPreferences.edit()) {
            putLong("user_id", userId)
            apply()
        }
    }

    // Fetch the user ID
    fun fetchUserId(): Long {
        return sharedPreferences.getLong("user_id", -1)
    }

    // Clear the user ID
    fun clearUserId() {
        with(sharedPreferences.edit()) {
            remove("user_id")
            apply()
        }
    }
}
