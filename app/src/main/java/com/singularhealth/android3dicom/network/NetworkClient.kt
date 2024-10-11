package com.singularhealth.android3dicom.network

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.singularhealth.android3dicom.model.IDataStoreRepository
import com.singularhealth.android3dicom.model.LoginRequest
import com.singularhealth.android3dicom.model.ScanModel
import com.singularhealth.android3dicom.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class NetworkClient
    @Inject
    constructor(
        dataStore: IDataStoreRepository,
    ) {
        private val clientScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        private var dataStore: IDataStoreRepository
        private var singularHealthRestService: ISingularHealthRestService

        private val LOG_TAG = "NetworkClient"
        private val BASE_URL = "https://api.singular.health/"
        private val DELETE_SUCCESS = 204

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
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            this.dataStore = dataStore
            singularHealthRestService = retrofit.create(ISingularHealthRestService::class.java)
        }

        suspend fun loginUser(
            email: String,
            password: String,
        ): Boolean =
            withContext(clientScope.coroutineContext) {
                try {
                    val loginRequest = LoginRequest(email, password)
                    Log.d(LOG_TAG, "Sending login request: $loginRequest")
                    val response = singularHealthRestService.login(loginRequest)
                    Log.d(LOG_TAG, "Received response: $response")

                    if (response.access_token != null) {
                        // Save the access token
                        dataStore.getInstance().edit { preferences ->
                            preferences[stringPreferencesKey("username")] = email
                            preferences[stringPreferencesKey("password")] = password
                            preferences[stringPreferencesKey("access_token")] = response.access_token
                            preferences[booleanPreferencesKey("is_logged_in")] = true
                        }
                        Log.d(LOG_TAG, "Login successful, token saved")
                        true
                    } else {
                        Log.e(LOG_TAG, "Login failed: No access token in response")
                        false
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Error during login", e)
                    when (e) {
                        is IOException -> Log.e(LOG_TAG, "Network error: ${e.message}")
                        is HttpException -> {
                            val errorBody = e.response()?.errorBody()?.string()
                            Log.e(LOG_TAG, "HTTP error ${e.code()}: $errorBody")
                        }
                        else -> Log.e(LOG_TAG, "Unexpected error: ${e.message}")
                    }
                    false
                }
            }

        fun fetchUserInfo(onResult: (Result<UserModel>) -> Unit) {
            clientScope.launch {
                try {
                    val token =
                        dataStore.getInstance().data.first()[stringPreferencesKey("access_token")]
                            ?: throw Exception("No access token found")

                    val response = singularHealthRestService.getUserInfo("Bearer $token")
                    // _userInfo = response

                    Log.d("UserInfoViewModel", "User Info Retrieved: $response")

                    onResult(Result.success(response))
                } catch (e: Exception) {
                    Log.e("UserInfoViewModel", "Error fetching user info", e)
                    onResult(Result.failure(e))
                }
            }
        }

        suspend fun fetchScans(): List<ScanModel> {
            try {
                val token =
                    getAccessToken()
                        ?: throw Exception("No access token found for share scan request")
                val response = singularHealthRestService.fetchScans(token)
                Log.d(LOG_TAG, response.body().toString())
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())
                false
            }
            return listOf(ScanModel("", "", "", "", "", "", "", "", "", listOf(""), listOf(""), listOf("")))
        }

        suspend fun shareScan(
            scanId: String,
            emailList: List<String>,
        ): Boolean =
            try {
                val token =
                    getAccessToken()
                        ?: throw Exception("No access token found for share scan request")
                val response = singularHealthRestService.shareScan(token, scanId, emailList)
                response.code() == DELETE_SUCCESS
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())
                false
            }

        suspend fun deleteScanFromServer(scanId: String): Boolean =
            try {
                val token =
                    getAccessToken()
                        ?: throw Exception("No access token found for delete scan request")
                val response = singularHealthRestService.deleteScan(token, scanId)
                response.code() == DELETE_SUCCESS
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())
                false
            }

        private fun getAccessToken(): String? {
            val accessTokenKey = stringPreferencesKey("access_token")
            val preferences =
                runBlocking {
                    dataStore.getInstance().data.first()
                }
            println("Access Token: $accessTokenKey")
            return preferences[accessTokenKey]
        }
    }
