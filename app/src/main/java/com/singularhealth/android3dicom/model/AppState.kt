package com.singularhealth.android3dicom.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginPreferenceOption {
    NONE,
    BIOMETRIC,
    PIN,
}

class AppState
    @Inject
    constructor(
        dataStore: IDataStoreRepository,
    ) {
        private val appStateScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private val _dataStore = dataStore

        companion object {
            const val LOGIN_PREF = ""
        }

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
    }
