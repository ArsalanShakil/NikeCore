package com.example.nikecore.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val ONBOARING_PREF = "onboardingprefs"
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("myprefs", 0)
    }

    fun getIsFirstStart(context: Context): Boolean {
        return getPrefs(context).getBoolean(ONBOARING_PREF, false)
    }


    fun setIsFirstStart(context: Context, value: Boolean?) {
        if (value != null) {
            getPrefs(context).edit().putBoolean(ONBOARING_PREF, value).apply()
        }
    }
}