package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.network.ISingularHealthRestService
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        private val singularHealthRestService: ISingularHealthRestService
        private val dataStore = appState.dataStore

        /*private val _isLoginSuccess = MutableLiveData<Boolean>()
        val isLoginSuccess: LiveData<Boolean> get() = _isLoginSuccess*/

        // var isLoading by mutableStateOf(false)
        // var errorMessage by mutableStateOf(String)

        private val _isLoading = MutableStateFlow<Boolean>(false)
        val isLoading: StateFlow<Boolean> get() = _isLoading

        private val _errorMessage = MutableStateFlow<String>("")
        val errorMessage: StateFlow<String> get() = _errorMessage

        private val _email = MutableStateFlow<String>("")
        val email: StateFlow<String> get() = _email

        private val _password = MutableStateFlow<String>("")
        val password: StateFlow<String> get() = _password

        companion object {
            // Set this to true to work on the login UI
            private const val FORCE_LOGIN_SCREEN = false
        }

        private val _isDebugMode = MutableStateFlow(FORCE_LOGIN_SCREEN)
        val isDebugMode: StateFlow<Boolean> = _isDebugMode.asStateFlow()

        val isLoggedIn: StateFlow<Boolean> =
            dataStore
                .getInstance()
                .data
                .map { preferences ->
                    // Only check logged in state if not in debug mode
                    if (_isDebugMode.value) {
                        false
                    } else {
                        preferences[booleanPreferencesKey("is_logged_in")] ?: false
                    }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

        init {
            val loggingInterceptor =
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

            val client =
                OkHttpClient
                    .Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl("https://api.singular.health/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            singularHealthRestService = retrofit.create(ISingularHealthRestService::class.java)
        }

        /*fun loginUser(
            email: String,
            password: String,
        ) = appState.login(email, password) { onLoginComplete() }*/

    /*withContext(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(email, password)
            Log.d("LoginViewModel", "Sending login request: $loginRequest")
            val response = singularHealthRestService.login(loginRequest)
            Log.d("LoginViewModel", "Received response: $response")

            if (response.access_token != null) {
                // Save the access token
                dataStore.getInstance().edit { preferences ->
                    preferences[stringPreferencesKey("username")] = email
                    preferences[stringPreferencesKey("password")] = password
                    preferences[stringPreferencesKey("access_token")] = response.access_token
                    preferences[booleanPreferencesKey("is_logged_in")] = true
                }
                Log.d("LoginViewModel", "Login successful, token saved")
                true
            } else {
                Log.e("LoginViewModel", "Login failed: No access token in response")
                false
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error during login", e)
            when (e) {
                is IOException -> Log.e("LoginViewModel", "Network error: ${e.message}")
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("LoginViewModel", "HTTP error ${e.code()}: $errorBody")
                }
                else -> Log.e("LoginViewModel", "Unexpected error: ${e.message}")
            }
            false
        }
    }*/

        suspend fun logout() {
            dataStore.getInstance().edit { preferences ->
                preferences.remove(stringPreferencesKey("access_token"))
                preferences[booleanPreferencesKey("is_logged_in")] = false
            }
            Log.d("LoginViewModel", "User logged out")
        }

        fun logoutUser() {
            viewModelScope.launch {
                logout()
            }
        }

        private fun onLoginComplete(isSuccess: Boolean) {
            _isLoading.value = false
            if (isSuccess) {
                if (appState.loginPreference == LoginPreferenceOption.NONE) {
                    appState.navigateTo(ViewRoute.LOGIN_SETUP)
                } else {
                    appState.navigateTo(ViewRoute.SCAN_LIBRARY)
                }
            } else {
                _errorMessage.value = "Login failed"
            }
        }

        fun onLoginPressed() {
            _errorMessage.value = ""
            _isLoading.value = true
            // val success = viewModel.loginUser(email, password)
            // loginUser(email, password)
            viewModelScope.launch {
                appState.login(email.value, password.value) { onLoginComplete(it) }
            }
        }

        fun onEmailChanged(value: String) {
            _email.value = value
        }

        fun onPasswordChanged(value: String) {
            _password.value = value
        }

        fun toggleDebugMode() {
            _isDebugMode.value = !_isDebugMode.value
            Log.d("LoginViewModel", "Debug mode toggled. New value: ${_isDebugMode.value}")
        }
    }

/*class LoginViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
