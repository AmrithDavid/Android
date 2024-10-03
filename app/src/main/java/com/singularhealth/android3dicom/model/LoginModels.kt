package com.singularhealth.android3dicom.model

data class LoginRequest(
    val Email: String,
    val Password: String,
)

data class LoginResponse(
    val access_token: String?,
)
