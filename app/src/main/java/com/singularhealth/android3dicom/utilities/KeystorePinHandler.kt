package com.singularhealth.android3dicom.utilities

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object KeystorePinHandler {
    private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private const val KEY_ALIAS = "PinEncryptionKey"
    private const val ENCRYPTED_PIN_PREF = "encrypted_pin"
    private const val IV_PREF = "encryption_iv"

    private var sharedPreferences: SharedPreferences? = null

    fun initialise(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("pin_prefs", Context.MODE_PRIVATE)
            if (!isKeyInitialized()) {
                generateKey()
            }
        }
    }

    fun setPin(pin: String) {
        checkInitialization()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val encryptedPin = cipher.doFinal(pin.toByteArray(Charsets.UTF_8))
        val encodedPin = Base64.encodeToString(encryptedPin, Base64.DEFAULT)
        val iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT)

        sharedPreferences?.edit()?.apply {
            putString(ENCRYPTED_PIN_PREF, encodedPin)
            putString(IV_PREF, iv)
            apply()
        }
    }

    fun verifyPin(pin: String): Boolean {
        checkInitialization()
        val encryptedPin = sharedPreferences?.getString(ENCRYPTED_PIN_PREF, null) ?: return false
        val iv = sharedPreferences?.getString(IV_PREF, null) ?: return false

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val ivSpec = GCMParameterSpec(128, Base64.decode(iv, Base64.DEFAULT))
        cipher.init(Cipher.DECRYPT_MODE, getKey(), ivSpec)

        val decryptedPin = cipher.doFinal(Base64.decode(encryptedPin, Base64.DEFAULT))
        return String(decryptedPin, Charsets.UTF_8) == pin
    }

    fun isPinSet(): Boolean {
        checkInitialization()
        return sharedPreferences?.contains(ENCRYPTED_PIN_PREF) ?: false
    }

    private fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val keyGenParameterSpec =
            KeyGenParameterSpec
                .Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun isKeyInitialized(): Boolean {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        return keyStore.containsAlias(KEY_ALIAS)
    }

    private fun checkInitialization() {
        if (sharedPreferences == null) {
            throw IllegalStateException("KeystorePinHandler is not initialized. Call initialise() first.")
        }
    }
}
