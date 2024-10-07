package com.singularhealth.android3dicom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.data.CacheManager
import com.singularhealth.android3dicom.data.CacheManagerImpl
import com.singularhealth.android3dicom.model.PatientCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModelimport javax.inject.Inject

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
                logCacheSize()
            }
        }

        fun loadData() {
            _isBiometricLoginActive.value = appState.loginPreference == LoginPreferenceOption.BIOMETRIC
            loadPatientCards()
        }

        private fun loadPatientCards() {
            _patientCards.value = generateDummyData()
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
                Log.d(TAG, "Starting cache clearing process")

                // Add test files (optional, for testing purposes)
                if (cacheManager is CacheManagerImpl) {
                    cacheManager.addTestCacheFiles()
                }

                val initialCacheSize = cacheManager.getCacheSize()
                Log.d(TAG, "Initial cache size: ${formatSize(initialCacheSize)}")

                val clearedSize = cacheManager.clearCache()

                val finalCacheSize = cacheManager.getCacheSize()
                Log.d(TAG, "Cache clearing completed")
                Log.d(TAG, "Cleared cache size: ${formatSize(clearedSize)}")
                Log.d(TAG, "Final cache size: ${formatSize(finalCacheSize)}")

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

        private suspend fun logCacheSize() {
            val size = cacheManager.getCacheSize()
            Log.d(TAG, "Current cache size: ${formatSize(size)}")
        }

        private fun formatSize(size: Long): String {
            if (size < 1024) return "$size B"
            val units = listOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
        }

        companion object {
            private const val TAG = "ScanLibraryViewModel"
        }
    }
