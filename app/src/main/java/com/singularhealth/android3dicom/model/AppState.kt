package com.singularhealth.android3dicom.model

import com.singularhealth.android3dicom.utilities.KeystorePinHandler
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.singularhealth.android3dicom.network.NetworkClient
import com.singularhealth.android3dicom.view.ViewRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private lateinit var navController: NavHostController

        private val _dataStore = dataStore
        val dataStore = _dataStore

        private lateinit var currentUser: UserModel
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

        fun navigateTo(viewRoute: ViewRoute) {
            navController.navigate(viewRoute.toString())
        }

        fun navigateBack() {
            navController.navigateUp()
        }

        fun getAvailableCredits(): Int = currentUser.creditBalance

        suspend fun login(
            email: String,
            password: String,
            callback: (Boolean) -> Unit,
        ) {
            callback(networkClient.loginUser(email, password))
        }

        fun logout() {
            appStateScope.launch {
                dataStore.getInstance().edit { preferences ->
                    preferences.remove(stringPreferencesKey("access_token"))
                    preferences[booleanPreferencesKey("is_logged_in")] = false
                }
                Log.d("LoginViewModel", "User logged out")
            }
        }

        fun getScans(onResult: (List<ScanModel>) -> Unit) {
            appStateScope.launch { onResult(networkClient.fetchScans()) }
        }

        fun shareScan(email: String) {
            appStateScope.launch { networkClient.shareScan(currentScan.id, listOf(email)) }
        }
    }
