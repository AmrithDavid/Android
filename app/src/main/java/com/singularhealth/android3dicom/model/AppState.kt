package com.singularhealth.android3dicom.model

import com.singularhealth.android3dicom.utilities.KeystorePinHandler
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
    ) {
        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        @Suppress("ktlint:standard:backing-property-naming")
        private val _dataStore = dataStore

        private var _loginPreference = LoginPreferenceOption.NONE
        val dataStore = _dataStore
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
    }
