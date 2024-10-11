package com.singularhealth.android3dicom.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricUtils {
    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var promptActivity: FragmentActivity
    private lateinit var onSuccess: () -> Unit

    lateinit var startForResult: ActivityResultLauncher<Intent>

    /**
     * Checks whether the device is capable of biometric authentication.
     *
     * @param context The context to retrieve biometric capability information.
     * @return A constant indicating the biometric capability status. Possible values:
     *         [BiometricManager.BIOMETRIC_SUCCESS] - The device supports biometric authentication.
     *         [BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE] - The device does not have biometric hardware.
     *         [BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE] - Biometric hardware is currently unavailable.
     *         [BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED] - No biometric credentials are enrolled.
     *         [BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED] - A security vulnerability has been discovered.
     *         [BiometricManager.BIOMETRIC_STATUS_UNKNOWN] - Unable to determine whether the user can authenticate.
     *         [BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED] - The device does not support the required features.
     */
    private fun hasBiometricCapability(context: Context): Int =
        BiometricManager
            .from(context)
            .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL or BIOMETRIC_WEAK)

    /**
     * Checks whether the device is ready for biometric authentication.
     *
     * @param context The context to check biometric readiness.
     * @return `true` if the device is ready for biometric authentication, otherwise `false`.
     */
    fun isBiometricReady(context: Context): Boolean = hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

    /**
     * Initializes a [BiometricPrompt] instance for biometric authentication.
     *
     * @param activity The [FragmentActivity] context.
     * @param listener The listener for biometric authentication events.
     * @param successListener A callback function to be called on successful authentication.
     * @return Configured [BiometricPrompt] instance.
     */
    fun initBiometricPrompt(
        activity: FragmentActivity,
        listener: BiometricAuthListener,
        successListener: () -> Unit,
    ): BiometricPrompt {
        promptActivity = activity
        biometricManager = BiometricManager.from(activity)
        onSuccess = successListener
        val executor = ContextCompat.getMainExecutor(activity)
        val callback =
            object : BiometricPrompt.AuthenticationCallback() {
                /**
                 * Called when an error occurs during authentication.
                 *
                 * @param errorCode The error code representing the type of error.
                 * @param errString A human-readable error message.
                 */
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    listener.onBiometricAuthenticateError(errorCode, errString.toString())
                }

                /**
                 * Called when authentication fails.
                 */
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    listener.onAuthenticationFailed()
                }

                /**
                 * Called when authentication is successful.
                 *
                 * @param result The authentication result containing additional information.
                 */
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener.onBiometricAuthenticateSuccess(result)
                }
            }
        biometricPrompt = BiometricPrompt(activity, executor, callback)
        return biometricPrompt
    }

    /**
     * Initiates the biometric authentication process.
     * Handles different scenarios such as biometric availability, hardware errors, and enrollment status.
     */
    fun authenticate() {
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                val errMsg = "No biometric features available on this device."
                Log.e("MY_APP_TAG", errMsg)
                Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                val errMsg = "Biometric features are currently unavailable."
                Log.e("MY_APP_TAG", errMsg)
                Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("MY_APP_TAG", "Biometric features require enrolment.")
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                startForResult.launch(enrollIntent)
            }
            else -> {
                val errMsg = "Biometric authentication not available"
                Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Handles the result of a biometric enrollment attempt.
     *
     * @param result The [ActivityResult] containing the result of the enrollment attempt.
     */
    fun onEnrolAttempt(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("ENROL_INTENT", "Returned result from activity")
            onSuccess()
        } else {
            val errMsg = "The biometric enrolment failed with code: + ${result.resultCode}"
            Log.e("ENROL_INTENT", errMsg)
            Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Creates a BiometricPrompt.PromptInfo for displaying the biometric prompt.
     *
     * @param title The title to be displayed in the biometric prompt.
     * @param description The description to be displayed in the biometric prompt.
     * @param negativeText The text for the negative button in the biometric prompt.
     * @return Configured [BiometricPrompt.PromptInfo] instance.
     */
    fun createPromptInfo(
        title: String,
        description: String,
        negativeText: String,
    ): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo
            .Builder()
            .setTitle(title)
            .setDescription(description)
            .setNegativeButtonText(negativeText)
            .setConfirmationRequired(false)
            .build()
}
