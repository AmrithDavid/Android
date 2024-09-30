package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.PatientCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanViewModel : ViewModel() { // inherits from ViewModel class
    private val _greeting = MutableStateFlow("Hello Sam") // private MutableStateFlow that can be modified within the ViewModel. Initialised with Hello Sam.
    val greeting: StateFlow<String> = _greeting.asStateFlow() // public StateFlow exposed to observers (like UI)

    private val _patientCards = MutableStateFlow<List<PatientCardData>>(emptyList()) // Initialised with empty list
    val patientCards: StateFlow<List<PatientCardData>> = _patientCards.asStateFlow()

    init {
        // Initialise with placeholder data
        _patientCards.value =
            List(4) {
                // List constructor takes 2 variables - size of list (4) and lambda function that defines now to create each element of the list
                // Populates _patientCards with a list of 4 identical objects
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

    // will use methods to fetch real data via rest APIs
}
