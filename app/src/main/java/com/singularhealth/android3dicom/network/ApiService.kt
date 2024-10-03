package com.singularhealth.android3dicom.network

import com.singularhealth.android3dicom.model.LoginRequest
import com.singularhealth.android3dicom.model.LoginResponse
import com.singularhealth.android3dicom.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("api/Me/AccessToken")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @GET("api/Me")
    @Headers(
        "Accept: application/json",
        "Swagger-Origin: https://api.singular.health",
    )
    suspend fun getUserInfo(
        @Header("Authorization") token: String,
    ): UserModel
}
