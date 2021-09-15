package com.quanticheart.core

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

const val SHARED_PREFS_FILENAME_BIOMETRIC = "biometric_prefs"
const val SHARED_PREFS_MASTER = "biometric_prefs_alias"

/**
 * Default [SharedPreferences]
 */
val Context.prefs: SharedPreferences
    get() = EncryptedSharedPreferences.create(
        SHARED_PREFS_FILENAME_BIOMETRIC,
        SHARED_PREFS_MASTER,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


fun Context.startSession(user: String) {
    val editor: SharedPreferences.Editor = prefs.edit()
    editor.putString("login", user)
    editor.apply()
}

fun Context.finishSession() {
    val editor: SharedPreferences.Editor = prefs.edit()
    editor.remove("login")
    editor.apply()
}

fun Context.verifySession() = prefs.getString("login", null) != null

fun Context.session() = prefs.getString("login", null)
