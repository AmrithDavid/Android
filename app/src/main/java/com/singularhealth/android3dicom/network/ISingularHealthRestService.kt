package com.singularhealth.android3dicom.network

import com.singularhealth.android3dicom.model.FetchCardResponseData
import com.singularhealth.android3dicom.model.LoginRequest
import com.singularhealth.android3dicom.model.LoginResponse
import com.singularhealth.android3dicom.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ISingularHealthRestService {
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
    ): Response<UserModel>

    @PUT("api/Mftp/V2/Share")
    @Headers(
        "Content-Type: application/json",
    )
    suspend fun shareScan(
        @Header("Authorization") token: String,
        @Query("scanId") id: String,
        @Body emails: List<String>,
    ): Response<Unit>

    @GET("api/Mftp/V2/Simple?activeOnly=true")
    suspend fun fetchScans(
        @Header("Authorization") token: String,
    ): Response<FetchCardResponseData>

    @DELETE("/api/Mftp/V2?scanId={id}")
    suspend fun deleteScan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<Unit>
}
