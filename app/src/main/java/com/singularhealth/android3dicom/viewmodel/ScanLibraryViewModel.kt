package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.PatientCardData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScanLibraryViewModel : ViewModel() {
    private val _greeting = MutableStateFlow("Hello Sam")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _patientCards = MutableStateFlow<List<PatientCardData>>(emptyList())
    val patientCards: StateFlow<List<PatientCardData>> = _patientCards.asStateFlow()

    private var _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

    private val _isSideMenuVisible = MutableStateFlow(false)
    val isSideMenuVisible: StateFlow<Boolean> = _isSideMenuVisible.asStateFlow()

    init {
        viewModelScope.launch {
            // Simulate data loading
//            delay(2000) // Simulate a 2-second load time

            loadPatientCards()

            _dataLoaded.value = true
        }
    }

    private fun loadPatientCards() {
        // Toggle between these two lines to test empty and non-empty states
        // _patientCards.value = emptyList() // Uncomment this line to test EmptyStateView
        _patientCards.value = generateDummyData() // Uncomment this line to test CardList
    }

    private fun generateDummyData(): List<PatientCardData> =
        listOf(
            PatientCardData(
                patientName = "Sam Kellahan",
                date = "2024-09-10",
                patientId = "123456789",
                modality = "CT",
                expiresIn = "7 days",
                fileName = "img_logomark",
            ),
            PatientCardData(
                patientName = "Luna Shin",
                date = "2024-09-10",
                patientId = "123488272",
                modality = "Xray",
                expiresIn = "7 days",
                fileName = "patient_image",
            ),
            PatientCardData(
                patientName = "testing name",
                date = "2024-09-10",
                patientId = "01010110",
                modality = "CT",
                expiresIn = "7 days",
                fileName = "patient_image",
            ),
            PatientCardData(
                patientName = "choco",
                date = "1997-09-01",
                patientId = "19970901",
                modality = "3D",
                expiresIn = "7 days",
                fileName = "patient_image",
            )

        )
//        List(4) {
//            PatientCardData(
//                patientName = "Sam Kellahan",
//                date = "2024-09-10",
//                patientId = "123456789",
//                modality = "CT",
//                expiresIn = "7 days",
//                imageName = "patient_image",
//            )
//        }



    fun updateGreeting(name: String) {
        _greeting.value = "Hello $name"
    }

    fun isDataLoaded(): Boolean = _dataLoaded.value

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
