package com.singularhealth.android3dicom.model

data class PatientCardData(
    val patientName: String,
    val date: String,
    val patientId: String,
    val modality: String,
    val expiresIn: String,
    val fileName: String,
)
