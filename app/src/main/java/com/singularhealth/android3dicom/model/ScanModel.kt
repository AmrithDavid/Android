package com.singularhealth.android3dicom.model

data class ScanModel(
    val id: String,
    val title: String,
    val patientName: String,
    val date: String,
    val patientID: String,
    val modality: String,
    val expiresIn: String,
    val imageName: String,
    val threeDImage: String,
    val transverseImages: List<String>,
    val sagittalImages: List<String>,
    val coronalImages: List<String>
)