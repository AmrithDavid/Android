package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.PatientCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanViewModel : ViewModel() {
    private val _greeting = MutableStateFlow("Hello Sam")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _patientCards = MutableStateFlow<List<PatientCardData>>(emptyList())
    val patientCards: StateFlow<List<PatientCardData>> = _patientCards.asStateFlow()

    init {
        // Initialise with placeholder data
        _patientCards.value =
            List(4) {
                PatientCardData(
                    patientName = "Sam Kellahan",
                    date = "2024-09-10",
                    patientId = "123456789",
                    modality = "CT",
                    expiresIn = "7 days",
                    imageName = "patient_image",
                )
            }
    }

    fun updateGreeting(name: String) {
        _greeting.value = "Hello $name"
    }

    // will methods to fetch real data via res APIs
}
