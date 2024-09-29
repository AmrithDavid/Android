package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.PatientCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanLibraryViewModel : ViewModel() {
    private val _greeting = MutableStateFlow("Hello Sam")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _patientCards = MutableStateFlow<List<PatientCardData>>(emptyList())
    val patientCards: StateFlow<List<PatientCardData>> = _patientCards.asStateFlow()

    private val _isSideMenuVisible = MutableStateFlow(false)
    val isSideMenuVisible: StateFlow<Boolean> = _isSideMenuVisible.asStateFlow()

    init {
        // Initialize with placeholder data
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

    fun toggleSideMenu() {
        _isSideMenuVisible.value = !_isSideMenuVisible.value
    }

    // Add methods to handle side menu actions
    fun onHomeClick() {
        // Implement home action
        toggleSideMenu()
    }

    fun onClearCacheClick() {
        // Implement clear cache action
        toggleSideMenu()
    }

    fun onBiometricClick() {
        // Implement biometric action
        toggleSideMenu()
    }

    fun onAboutClick() {
        // Implement about action
        toggleSideMenu()
    }

    fun onSupportClick() {
        // Implement support action
        toggleSideMenu()
    }

    fun onLogoutClick() {
        // Implement logout action
        toggleSideMenu()
    }
}
