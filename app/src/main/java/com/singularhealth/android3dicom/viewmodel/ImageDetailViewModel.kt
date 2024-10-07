package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageDetailViewModel : ViewModel() {
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

    fun on3DClick() {
        // Display 3D scan image
        println("clicked 3D")
    }

    fun onTransverseClick() {
        // Display Transverse scan image
        println("clicked Transverse")
    }

    fun onSagittalClick() {
        // Display Sagittal scan image
        println("clicked Sagittal")
    }

    fun onCoronalClick() {
        // Display Coronal scan image
        println("clicked Coronal")
    }
}
