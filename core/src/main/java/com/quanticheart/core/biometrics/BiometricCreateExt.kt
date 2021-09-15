package com.quanticheart.core.biometrics

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.quanticheart.core.R

private const val TAG = "BiometricPromptUtils"

fun AppCompatActivity.createBiometricPrompt(
    processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(this)

    val callback = object : BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errCode, errString)
            Log.d(TAG, "errCode is $errCode and errString is: $errString")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Log.d(TAG, "User biometric rejected.")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Log.d(TAG, "Authentication was successful")
            processSuccess(result)
        }
    }
    return BiometricPrompt(this, executor, callback)
}

fun AppCompatActivity.createPromptInfo(): BiometricPrompt.PromptInfo =
    BiometricPrompt.PromptInfo.Builder().apply {
        setTitle(getString(R.string.prompt_info_title))
        setSubtitle(getString(R.string.prompt_info_subtitle))
        setDescription(getString(R.string.prompt_info_description))
        setConfirmationRequired(false)
        setNegativeButtonText(getString(R.string.prompt_info_use_app_password))
    }.build()