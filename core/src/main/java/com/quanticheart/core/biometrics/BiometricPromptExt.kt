package com.quanticheart.core.biometrics

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import com.quanticheart.core.crypto.CryptographyManager
import com.quanticheart.core.crypto.CryptographyManagerImpl

val cryptographyManager: CryptographyManager = CryptographyManagerImpl()

val AppCompatActivity.biometrics
    get() = cryptographyManager.getBiometricFromSharedPrefs(applicationContext, PREFS_BIOMETRIC_KEY)

fun AppCompatActivity.removeBiometricData(keyName: String) {
    cryptographyManager.clearBiometricFromSharedPrefs(applicationContext, keyName)
}

fun Activity.deviceHaveBiometricsHardware(callback: () -> Unit) {
    val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
    if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
        callback()
    }
}
