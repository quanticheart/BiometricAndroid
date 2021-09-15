package com.quanticheart.core.biometrics

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.quanticheart.core.R

internal const val PREFS_BIOMETRIC_KEY = "ciphertext_wrapper"

/**
 * Session
 */
fun AppCompatActivity.biometricSession(
    session: String,
    finishAfter: Boolean = false
) {
    biometricDialogForSaveData(PREFS_BIOMETRIC_KEY, session, finishAfter)
}

fun AppCompatActivity.biometricGetSession(callback: (String?) -> Unit) {
    biometricDialogForGetData(PREFS_BIOMETRIC_KEY, callback)
}

fun AppCompatActivity.biometricRemoveSession(callback: (Boolean) -> Unit) {
    biometricRemoveData(PREFS_BIOMETRIC_KEY, callback)
}

/**
 * Prompt For Insert Data
 */
fun AppCompatActivity.biometricDialogForSaveData(
    keyName: String,
    dataForProtectWithBiometric: String,
    finishAfter: Boolean = false
) {
    val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
    if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
        val secretKeyName = getString(R.string.secret_key_name)
        val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
        val biometricPrompt =
            createBiometricPrompt {
                encryptAndStoreServerToken(keyName, dataForProtectWithBiometric, it, finishAfter)
            }
        val promptInfo = createPromptInfo()
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }
}

private fun AppCompatActivity.encryptAndStoreServerToken(
    keyName: String,
    dataForSaveInBiometric: String,
    authResult: BiometricPrompt.AuthenticationResult,
    finishAfter: Boolean
) {
    authResult.cryptoObject?.cipher?.apply {
        Log.e("TESTES", "The token from server is $dataForSaveInBiometric")
        val encryptedServerTokenWrapper =
            cryptographyManager.encryptData(dataForSaveInBiometric, this)
        cryptographyManager.persistBiometricToSharedPrefs(
            applicationContext,
            keyName,
            encryptedServerTokenWrapper
        )
    }

    if (finishAfter)
        finish()
}

/**
 * Prompt for get Data
 */
fun AppCompatActivity.biometricDialogForGetData(
    keyName: String,
    callback: (String?) -> Unit
) {
    cryptographyManager.getBiometricFromSharedPrefs(applicationContext, keyName)
        ?.let { textWrapper ->
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            val biometricPrompt =
                createBiometricPrompt {
                    decryptServerTokenFromStorage(keyName, it, callback)
                }
            val promptInfo = createPromptInfo()
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
}

private fun AppCompatActivity.decryptServerTokenFromStorage(
    keyName: String,
    authResult: BiometricPrompt.AuthenticationResult,
    callback: (String?) -> Unit
) {
    cryptographyManager.getBiometricFromSharedPrefs(applicationContext, keyName)
        ?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext = cryptographyManager.decryptData(textWrapper.ciphertext, it)
                // Now that you have the token, you can query server for everything else
                // the only reason we call this fakeToken is because we didn't really get it from
                // the server. In your case, you will have gotten it from the server the first time
                // and therefore, it's a real token.
                callback(plaintext)
            }
        } ?: run { callback(null) }
}

/**
 * Verify Biometric
 */
fun AppCompatActivity.biometricRemoveData(
    keyName: String,
    callback: (Boolean) -> Unit
) {
    cryptographyManager.getBiometricFromSharedPrefs(applicationContext, keyName)
        ?.let { textWrapper ->
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            val biometricPrompt =
                createBiometricPrompt {
                    removeTokenFromStorage(keyName, callback)
                }
            val promptInfo = createPromptInfo()
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
}

private fun AppCompatActivity.removeTokenFromStorage(
    keyName: String,
    callback: (Boolean) -> Unit
) {
    val removed = cryptographyManager.clearBiometricFromSharedPrefs(applicationContext, keyName)
    callback(removed)
}
