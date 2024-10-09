// SessionUtils.kt
package com.ahmedapps.moviesapp.utils

import android.content.Context

fun isLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("is_logged_in", false)
}
