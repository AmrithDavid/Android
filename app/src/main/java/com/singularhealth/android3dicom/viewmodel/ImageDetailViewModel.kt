package com.singularhealth.android3dicom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel
    @Inject
    constructor(
        private var appState: AppState,
    ) : ViewModel() {
        private val _isInitialLoading = MutableStateFlow(true)
        val isInitialLoading: StateFlow<Boolean> = _isInitialLoading.asStateFlow()

        init {
            loadInitialView()
        }

        private fun loadInitialView() {
            viewModelScope.launch {
                delay(2000) // simulating initial loading
                _isInitialLoading.value = false
            }
        }

        fun onBack() {
            appState.navigateBack()
        }

        // Options menu callback functions
        fun onShare() {
            appState.navigateTo(ViewRoute.SHARE)
        }

        fun onMoreInfo() {
            Log.d("ImageDetailViewModel", "More info UI not implemented yet")
        }

        fun onDelete() {
            Log.d("ImageDetailViewModel", "Delete UI not implemented yet")
        }

        fun onReport() {
            appState.navigateTo(ViewRoute.REPORT)
        }

        fun onSupport() {
            Log.d("ImageDetailViewModel", "Support UI not implemented yet")
        }

        // Top bar tab callback functions
        fun on3DClick() {
            Log.d("ImageDetailViewModel", "Selected 3D tab")
        }

        fun onTransverseClick() {
            Log.d("ImageDetailViewModel", "Selected Transverse tab")
        }

        fun onSagittalClick() {
            Log.d("ImageDetailViewModel", "Selected Sagittal tab")
        }

        fun onCoronalClick() {
            Log.d("ImageDetailViewModel", "Selected Coronal tab")
        }

        // Bottom panel controls callback functions
    }
