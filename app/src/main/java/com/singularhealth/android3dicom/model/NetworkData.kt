package com.singularhealth.android3dicom.model

import com.google.gson.annotations.SerializedName

data class FetchCardResponseData(
    @SerializedName("Storage")
    val storage: Storage,
    @SerializedName("Items")
    val items: List<FetchCardItem>,
)

data class Storage(
    @SerializedName("UsedSpace")
    val usedSpace: Long, // Use Long for large values
    @SerializedName("TotalSpace")
    val totalSpace: Long, // Use Long for large values
)

data class FetchCardItem(
    @SerializedName("Own")
    val own: Boolean,
    @SerializedName("Sharing")
    val sharing: Boolean,
    @SerializedName("IsFolder")
    val isFolder: Boolean,
    @SerializedName("LifeDuration")
    val lifeDuration: Any?, // Use Any? or nullable type if it can be different types
    @SerializedName("StorageType")
    val storageType: Any?, // Use Any? for nullable fields
    @SerializedName("UpdatedAt")
    val updatedAt: String?, // Assuming ISO 8601 date format
    @SerializedName("SharedAt")
    val sharedAt: String?, // Assuming ISO 8601 date format
    @SerializedName("Expires")
    val expires: String?, // Assuming ISO 8601 date format
    @SerializedName("RecentPayment")
    val recentPayment: Any?, // Use Any? for nullable fields
    @SerializedName("OwnerEmail")
    val ownerEmail: String,
    @SerializedName("Id")
    val id: String,
    @SerializedName("Size")
    val size: Long, // Use Long for large file sizes
    @SerializedName("FileName")
    val fileName: String,
    @SerializedName("Fingerprint")
    val fingerprint: String,
    @SerializedName("Nickname")
    val nickname: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("FileMeta")
    val fileMeta: Any?, // Use Any? for nullable fields
    @SerializedName("CreatedAt")
    val createdAt: String, // Assuming ISO 8601 date format
)
