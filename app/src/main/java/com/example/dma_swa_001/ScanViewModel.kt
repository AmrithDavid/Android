package com.example.dma_swa_001.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PatientCard(
    val patientName: String,
    val date: String,
    val patientId: String,
    val modality: String,
    val expiresIn: String,
    val imageName: String
)

class ScanViewModel : ViewModel() {
    private val _greeting = MutableStateFlow("Hello Sam")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _patientCards = MutableStateFlow<List<PatientCard>>(emptyList())
    val patientCards: StateFlow<List<PatientCard>> = _patientCards.asStateFlow()

    init {
        // Initialise with placeholder data
        _patientCards.value = List(4) {
            PatientCard(
                patientName = "Sam Kellahan",
                date = "2024-09-10",
                patientId = "123456789",
                modality = "CT",
                expiresIn = "7 days",
                imageName = "patient_image"
            )
        }
    }

    fun updateGreeting(name: String) {
        _greeting.value = "Hello $name"
    }

    // will methods to fetch real data via res APIs
}

