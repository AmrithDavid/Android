package com.singularhealth.android3dicom.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.singularhealth.android3dicom.network.NetworkClient
import com.singularhealth.android3dicom.utilities.BiometricAuthListener
import com.singularhealth.android3dicom.utilities.BiometricUtils
import com.singularhealth.android3dicom.utilities.KeystorePinHandler
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

enum class LoginPreferenceOption {
    NONE,
    BIOMETRIC,
    PIN,
    ;

    companion object {
        // Returns the enum option matching a given string
        infix fun from(name: String): LoginPreferenceOption? = LoginPreferenceOption.values().firstOrNull { it.toString() == name }
    }
}

sealed class PinState {
    object Initial : PinState()

    object Loading : PinState()

    object Valid : PinState()

    object Success : PinState()

    data class Error(
        val errorState: ErrorState,
    ) : PinState()
}

enum class ErrorState {
    None,
    PinTooShort,
    PinsDoNotMatch,
    IncorrectPin,
    SaveFailed,
}

class AppState
    @Inject
    constructor(
        @ApplicationContext context: Context,
        dataStore: IDataStoreRepository,
        private var networkClient: NetworkClient,
    ) {
        private val LOG_TAG = "AppState"

        // Core app control objects
        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private lateinit var navController: NavHostController
        private val _dataStore = dataStore
        val dataStore = _dataStore

        // Authentication token
        var accessToken: String? = null

        // Cache of current data
        private var currentUser: UserModel? = null
        private lateinit var currentScan: ScanModel

        // Biometric authentication objects
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: PromptInfo
        private lateinit var promptActivity: FragmentActivity
        private lateinit var biometricManager: BiometricManager

        // Login preference properties
        private var _loginPreference = LoginPreferenceOption.NONE
        var loginPreference: LoginPreferenceOption
            get() {
                // value first retrieved from repo on AppState init
                return _loginPreference
            }
            set(value) {
                _loginPreference = value
                appStateScope.launch {
                    _dataStore.setString(::_loginPreference.name, value.toString())
                }
                println("appstate: saved login preference")
            }

        // Listeners
        private var onScansReceived: ((List<ScanModel>) -> Unit)? = null
        private var onBiometricAuthSuccess: (() -> Unit)? = null
        private var onUserDataReceived: ((UserModel) -> Unit)? = null

        init {
            // Check for existing login preference stored in settings and loads if available
            val storedLoginPref = runBlocking { _dataStore.getString(::_loginPreference.name) }
            if (storedLoginPref != null) {
                val opt = LoginPreferenceOption.from(storedLoginPref)
                if (opt != null) _loginPreference = opt
            }
        }

        fun isPinSet(): Boolean =
            try {
                KeystorePinHandler.isPinSet()
            } catch (e: IllegalStateException) {
                // KeystorePinHandler is not initialised, assume no PIN is set
                false
            }

        fun setNavController(navController: NavHostController) {
            this.navController = navController
        }

        fun isLoggedIn(): Boolean = currentUser != null

        fun setCurrentScan(scanModel: ScanModel) {
            currentScan = scanModel
        }

        fun navigateTo(viewRoute: ViewRoute) {
            navController.navigate(viewRoute.toString())
        }

        fun navigateBack() {
            navController.navigateUp()
        }

        fun getAvailableCredits(): Int? = currentUser?.creditBalance

        suspend fun login(
            email: String,
            password: String,
            callback: (Boolean) -> Unit,
        ) {
            accessToken = networkClient.loginUser(email, password)
            if (accessToken != null) {
                networkClient.fetchUserInfo(accessToken!!)?.let { currentUser = it }
                getScans { onScansReceived?.invoke(it) }
            }

            val userInfo = networkClient.fetchUserInfo(accessToken!!)
            currentUser = userInfo
            currentUser?.let { onUserDataReceived?.invoke(it) }
            callback(accessToken != null)
        }

        fun renewStoredLogin() {
            appStateScope.launch {
                try {
                    val username =
                        dataStore.getInstance().data.first()[stringPreferencesKey("username")]
                            ?: throw Exception("No username found")
                    val password =
                        dataStore.getInstance().data.first()[stringPreferencesKey("password")]
                            ?: throw Exception("No password found")

                    login(username, password) {
                        if (!it) throw Exception("Renew login from stored credentials failed")
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "${e.message}")
                }
            }
        }

        fun logout() {
            appStateScope.launch {
                dataStore.getInstance().edit { preferences ->
                    preferences.remove(stringPreferencesKey("username"))
                    preferences.remove(stringPreferencesKey("password"))
                    preferences[booleanPreferencesKey("is_logged_in")] = false
                }
                Log.d(LOG_TAG, "User logged out")
            }
            currentUser = null
        }

        fun getScans(onResult: (List<ScanModel>) -> Unit) {
            appStateScope.launch { onResult(networkClient.fetchScans(accessToken)) }
        }

        fun shareScan(email: String) {
            appStateScope.launch { networkClient.shareScan(accessToken, currentScan.id, listOf(email)) }
        }

        fun setOnScansReceivedListener(listener: (List<ScanModel>) -> Unit) {
            onScansReceived = listener
        }

        fun setOnUserDataReceivedListener(listener: (UserModel) -> Unit) {
            onUserDataReceived = listener
        }

        fun getCurrentUserName(): String {
            return currentUser?.firstName ?: "Guest" // Provide a default value
        }

        fun initialiseBiometricPrompt(
            context: Context,
            onAuthSuccess: () -> Unit,
            onAuthError: (code: Int, msg: String) -> Unit,
        ) {
            promptActivity = context as FragmentActivity
            biometricManager = BiometricManager.from(context)
            onBiometricAuthSuccess = onAuthSuccess
            val callback =
                makeBiometricCallbacks(
                    onSuccess = {
                        onAuthSuccess()
                    },
                    onError = { errorCode, errorString ->
                        onAuthError(errorCode, errorString)
                    },
                )
            biometricPrompt = BiometricUtils.initBiometricPrompt(promptActivity, callback) {}
            promptInfo =
                BiometricUtils.createPromptInfo(
                    title = "Biometric Login for 3Dicom",
                    description = "Log in using your biometric credential",
                    negativeText = "Cancel",
                )
            println("LoginSetupView: Biometric prompt initialized")
        }

        fun promptBiometricLogin() {
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
                    BiometricUtils.startForResult.launch(enrollIntent)
                }
                else -> {
                    val errMsg = "Biometric authentication not available"
                    Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
                }
            }
            biometricPrompt?.authenticate(promptInfo!!)
        }

        private fun makeBiometricCallbacks(
            onSuccess: () -> Unit,
            onError: (Int, String) -> Unit,
        ): BiometricAuthListener =
            object : BiometricAuthListener {
                override fun onBiometricAuthenticateError(
                    error: Int,
                    errMsg: String,
                ) {
                    onError(error, errMsg)
                }

                override fun onAuthenticationFailed() {
                    // Handle failed authentication
                }

                override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
            }

        private fun onEnrolAttempt(result: ActivityResult) {
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("ENROL_INTENT", "Biometric enrolment succeeded")
                onBiometricAuthSuccess?.invoke()
            } else {
                val errMsg = "The biometric enrolment failed with code: + ${result.resultCode}"
                Log.e("ENROL_INTENT", errMsg)
                Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
            }
        }

        fun initialiseBiometricPrompt(
            context: Context,
            onAuthSuccess: () -> Unit,
            onAuthError: (code: Int, msg: String) -> Unit,
        ) {
            promptActivity = context as FragmentActivity
            biometricManager = BiometricManager.from(context)
            onBiometricAuthSuccess = onAuthSuccess
            val callback =
                makeBiometricCallbacks(
                    onSuccess = {
                        onAuthSuccess()
                    },
                    onError = { errorCode, errorString ->
                        onAuthError(errorCode, errorString)
                    },
                )
            biometricPrompt = BiometricUtils.initBiometricPrompt(promptActivity, callback) {}
            promptInfo =
                BiometricUtils.createPromptInfo(
                    title = "Biometric Login for 3Dicom",
                    description = "Log in using your biometric credential",
                    negativeText = "Cancel",
                )
            println("LoginSetupView: Biometric prompt initialized")
        }

        fun promptBiometricLogin() {
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
                    BiometricUtils.startForResult.launch(enrollIntent)
                }
                else -> {
                    val errMsg = "Biometric authentication not available"
                    Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
                }
            }
            biometricPrompt?.authenticate(promptInfo!!)
        }

        private fun makeBiometricCallbacks(
            onSuccess: () -> Unit,
            onError: (Int, String) -> Unit,
        ): BiometricAuthListener =
            object : BiometricAuthListener {
                override fun onBiometricAuthenticateError(
                    error: Int,
                    errMsg: String,
                ) {
                    onError(error, errMsg)
                }

                override fun onAuthenticationFailed() {
                    // Handle failed authentication
                }

                override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
            }

        private fun onEnrolAttempt(result: ActivityResult) {
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("ENROL_INTENT", "Biometric enrolment succeeded")
                onBiometricAuthSuccess?.invoke()
            } else {
                val errMsg = "The biometric enrolment failed with code: + ${result.resultCode}"
                Log.e("ENROL_INTENT", errMsg)
                Toast.makeText(promptActivity, errMsg, Toast.LENGTH_LONG).show()
            }
        }
    }
