package com.singularhealth.android3dicom.model

data class UserModel(
    val email: String,
    val firstName: String,
    val lastName: String,
    val emailConfirmed: Boolean,
    val mfaRequired: Boolean,
    val mfaForced: Boolean,
    val userTypeId: Int,
    val countryId: String?,
    val companyName: String?,
    val manageDistributorId: String?,
    val phoneNumberMask: String?,
    val authenticator: Boolean,
    val offlineAccessAllowed: Boolean,
    val primaryMfa: Int,
    val creditBalance: Int,
    val monthlyQuotaCredits: Int,
    val trialEnd: String?,
)
