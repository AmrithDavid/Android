package com.singularhealth.android3dicom.model

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.singularhealth.android3dicom.network.NetworkClient
import com.singularhealth.android3dicom.utilities.KeystorePinHandler
import com.singularhealth.android3dicom.view.ViewRoute
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
        dataStore: IDataStoreRepository,
        private var networkClient: NetworkClient,
    ) {
        private val LOG_TAG = "AppState"

        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private lateinit var navController: NavHostController

        private val _dataStore = dataStore
        val dataStore = _dataStore

        var accessToken: String? = null

        private var currentUser: UserModel? = null
        private lateinit var currentScan: ScanModel

        private var _loginPreference = LoginPreferenceOption.NONE
        var loginPreference: LoginPreferenceOption
            get() {
                // retrieve value from repo
                return _loginPreference
            }
            set(value) {
                _loginPreference = value
                appStateScope.launch {
                    _dataStore.setString(::_loginPreference.name, value.toString())
                }
            }

        private var onScansReceived: ((List<ScanModel>) -> Unit)? = null

        init {
            // Checks for existing login preference stored in settings and loads if available
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
    }
