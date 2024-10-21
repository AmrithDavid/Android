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
        enum class WindowingPreset {
            CUSTOM,
            BONES,
            BRAIN,
            LIVER,
            LUNGS,
            MUSCLE,
        }

        enum class SlicerView {
            TRANSVERSE,
            SAGITTAL,
            CORONAL,
        }

        enum class SliderLimit {
            UPPER,
            LOWER,
        }

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

        fun onNextImage() {
            Log.d("ImageDetailViewModel", "Next image pressed")
        }

        fun onPrevImage() {
            Log.d("ImageDetailViewModel", "Previous image pressed")
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
        fun onBrightnessSliderUpdate(value: Float) {
            Log.d("ImageDetailViewModel", "Brightness slider updated with value: $value")
        }

        fun onContrastSliderUpdate(value: Float) {
            Log.d("ImageDetailViewModel", "Contrast slider updated with value: $value")
        }

        fun onOpacitySliderUpdate(value: Float) {
            Log.d("ImageDetailViewModel", "Opacity slider updated with value: $value")
        }

        fun onSlicerSliderUpdate(
            value: Float,
            option: SlicerView,
            limit: SliderLimit,
        ) {
            when (option) {
                SlicerView.TRANSVERSE -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                    }
                }
                SlicerView.SAGITTAL -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                    }
                }
                SlicerView.CORONAL -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Slicer slider updated for $option view with value: $value")
                        }
                    }
                }
            }
        }

        fun onWindowingSliderUpdate(
            value: Float,
            preset: WindowingPreset,
            limit: SliderLimit,
        ) {
            when (preset) {
                WindowingPreset.CUSTOM -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
                WindowingPreset.BONES -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
                WindowingPreset.BRAIN -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
                WindowingPreset.LIVER -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
                WindowingPreset.LUNGS -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
                WindowingPreset.MUSCLE -> {
                    when (limit) {
                        SliderLimit.UPPER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                        SliderLimit.LOWER -> {
                            Log.d("ImageDetailViewModel", "Windowing slider updated for $preset preset with value: $value")
                        }
                    }
                }
            }
        }
    }
