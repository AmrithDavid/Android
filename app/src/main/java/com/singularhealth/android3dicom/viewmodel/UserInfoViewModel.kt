package com.singularhealth.android3dicom.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.UserModel
import com.singularhealth.android3dicom.network.ISingularHealthRestService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class UserInfoViewModel(
    private val context: Context,
) : ViewModel() {
    private val singularHealthRestService: ISingularHealthRestService
    private var _userInfo: UserModel? = null

    init { // Initialises Retrofit instance with a base URL, JSON converter and logging intercepter
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://api.singular.health/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient
                        .Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            },
                        ).build(),
                ).build()
// then creates the ApiService
        singularHealthRestService = retrofit.create(ISingularHealthRestService::class.java)
    }

    fun fetchUserInfo(onResult: (Result<UserModel>) -> Unit) {
        viewModelScope.launch {
            try {
                val token =
                    context.dataStore.data.first()[stringPreferencesKey("access_token")]
                        ?: throw Exception("No access token found")

                val response = singularHealthRestService.getUserInfo("Bearer $token")
                _userInfo = response

                Log.d("UserInfoViewModel", "User Info Retrieved: $response")

                onResult(Result.success(response))
            } catch (e: Exception) {
                Log.e("UserInfoViewModel", "Error fetching user info", e)
                onResult(Result.failure(e))
            }
        }
    }

    fun hasUserInfo(): Boolean = _userInfo != null

    fun getUserInfo(): UserModel? = _userInfo
}

class UserInfoViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
