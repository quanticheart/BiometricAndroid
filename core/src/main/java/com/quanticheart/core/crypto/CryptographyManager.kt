package com.quanticheart.core.crypto

import android.content.Context
import javax.crypto.Cipher

/**
 * Handles encryption and decryption
 */
interface CryptographyManager {

    fun getInitializedCipherForEncryption(keyName: String): Cipher

    fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray): Cipher

    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    fun encryptData(plaintext: String, cipher: Cipher): CiphertextWrapper

    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    fun decryptData(ciphertext: ByteArray, cipher: Cipher): String

    fun persistBiometricToSharedPrefs(
        context: Context,
        keyName: String,
        ciphertextWrapper: CiphertextWrapper
    )

    fun getBiometricFromSharedPrefs(
        context: Context,
        keyName: String
    ): CiphertextWrapper?

    fun clearBiometricFromSharedPrefs(
        context: Context,
        keyName: String
    ): Boolean

}
