package com.singularhealth.android3dicom.model

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

class AppState
    @Inject
    constructor(
        dataStore: IDataStoreRepository,
    ) {
        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        @Suppress("ktlint:standard:backing-property-naming")
        private val _dataStore = dataStore

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
            // Replace with equivalent logic but more concise
            var storedLoginPref = runBlocking { _dataStore.getString(::_loginPreference.name) }
            if (storedLoginPref != null) {
                var opt = LoginPreferenceOption from storedLoginPref
                if (opt != null) _loginPreference = opt
            }
        }
    }
