package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.model.ScanModel
import com.singularhealth.android3dicom.utilities.CacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltViewModel
class ScanLibraryViewModel
    @Inject
    constructor(
        private val appState: AppState,
        private val cacheManager: CacheManager,
    ) : ViewModel() {
        private val _greeting = MutableStateFlow("Hello Sam")
        val greeting: StateFlow<String> = _greeting.asStateFlow()

        private val _patientCards = MutableStateFlow<List<PatientCardData>>(emptyList())
        val patientCards: StateFlow<List<PatientCardData>> = _patientCards.asStateFlow()

        private var _dataLoaded = MutableStateFlow(false)
        val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

        private val _isSideMenuVisible = MutableStateFlow(false)
        val isSideMenuVisible: StateFlow<Boolean> = _isSideMenuVisible.asStateFlow()

        private var _isBiometricLoginActive = MutableStateFlow(false)
        val isBiometricLoginActive: StateFlow<Boolean> = _isBiometricLoginActive.asStateFlow()

        private val _isClearingCache = MutableStateFlow(false)
        val isClearingCache: StateFlow<Boolean> = _isClearingCache.asStateFlow()

        init {
            viewModelScope.launch {
                loadData()
                _dataLoaded.value = true
            }
        }

        fun loadData() {
            _isBiometricLoginActive.value = appState.loginPreference == LoginPreferenceOption.BIOMETRIC
            loadPatientCards()
        }

        private fun loadPatientCards() {
            // _patientCards.value = generateDummyData()
            // Register for update from AppState when new scans are available
            appState.setOnScansReceivedListener { processScanData(it) }
            // Load scans manually in case they are already available
            if (appState.isLoggedIn()) {
                appState.getScans { processScanData(it) }
            }
        }

        private fun processScanData(scanList: List<ScanModel>) {
            var cards: MutableList<PatientCardData> = mutableListOf()
            scanList.forEach {
                cards.add(
                    PatientCardData(
                        patientName = it.patientName,
                        date = it.date,
                        patientId = it.patientID,
                        modality = it.modality,
                        expiresIn = it.expiresIn,
                        fileName = it.imageName,
                        scanData = it,
                    ),
                )
            }
            _patientCards.value = cards
        }

        private fun generateDummyData(): List<PatientCardData> =
            listOf(
                PatientCardData(
                    patientName = "Sam Kellahan",
                    date = "2024-09-10",
                    patientId = "123456789",
                    modality = "CT",
                    expiresIn = "7 days",
                    fileName = "patient_image",
                    scanData = null,
                ),
                PatientCardData(
                    patientName = "Luna Shin",
                    date = "2024-09-10",
                    patientId = "123488272",
                    modality = "Xray",
                    expiresIn = "7 days",
                    fileName = "patient_image",
                    scanData = null,
                ),
                PatientCardData(
                    patientName = "testing name",
                    date = "2024-09-10",
                    patientId = "01010110",
                    modality = "CT",
                    expiresIn = "7 days",
                    fileName = "patient_image",
                    scanData = null,
                ),
                PatientCardData(
                    patientName = "choco",
                    date = "1997-09-01",
                    patientId = "19970901",
                    modality = "3D",
                    expiresIn = "7 days",
                    fileName = "patient_image",
                    scanData = null,
                ),
            )

        fun updateGreeting(name: String) {
            _greeting.value = "Hello $name"
        }

        fun isDataLoaded(): Boolean = _dataLoaded.value

        fun toggleSideMenu() {
            _isSideMenuVisible.value = !_isSideMenuVisible.value
        }

        fun onHomeClick() {
            // Implement home action
            toggleSideMenu()
        }

        fun onClearCacheClick() {
            viewModelScope.launch {
                _isClearingCache.value = true
                cacheManager.clearCache()
                _isClearingCache.value = false
                toggleSideMenu()
            }
        }

        fun onBiometricClick() {
            // Implement biometric action
            _isBiometricLoginActive.value = !_isBiometricLoginActive.value
            appState.loginPreference =
                if (_isBiometricLoginActive.value) LoginPreferenceOption.BIOMETRIC else LoginPreferenceOption.PIN
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
