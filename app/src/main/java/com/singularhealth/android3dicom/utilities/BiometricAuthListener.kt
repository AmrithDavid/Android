package com.singularhealth.android3dicom.utilities

import androidx.biometric.BiometricPrompt

interface BiometricAuthListener {
    fun onBiometricAuthenticateError(
        error: Int,
        errMsg: String,
    )

    fun onAuthenticationFailed()

    fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult)
}
