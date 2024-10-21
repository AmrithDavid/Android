package com.singularhealth.android3dicom.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("Email") val email: String,
    @SerializedName("FirstName") val firstName: String,
    @SerializedName("LastName") val lastName: String,
    @SerializedName("EmailConfirmed") val emailConfirmed: Boolean,
    @SerializedName("MfaRequired") val mfaRequired: Boolean,
    @SerializedName("MfaForced") val mfaForced: Boolean,
    @SerializedName("UserTypeId") val userTypeId: Int,
    @SerializedName("CountryId") val countryId: String,
    @SerializedName("CompanyName") val companyName: String,
    @SerializedName("ManageDistributorId") val manageDistributorId: String?,
    @SerializedName("PhoneNumberMask") val phoneNumberMask: String?,
    @SerializedName("Authenticator") val authenticator: Boolean,
    @SerializedName("OfflineAccessAllowed") val offlineAccessAllowed: Boolean,
    @SerializedName("PrimaryMfa") val primaryMfa: Int,
    @SerializedName("CreditBalance") val creditBalance: Int,
    @SerializedName("MonthlyQuotaCredits") val monthlyQuotaCredits: Int,
    @SerializedName("TrialEnd") val trialEnd: String?,
)
