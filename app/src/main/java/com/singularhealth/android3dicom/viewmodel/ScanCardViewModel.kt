package com.singularhealth.android3dicom.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanCardViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        lateinit var data: PatientCardData
        var showMoreInfoDialog = mutableStateOf(false)
        var showDeleteDialog = mutableStateOf(false)

        fun onImages() {
            data.scanData?.let { appState.setCurrentScan(it) }
            appState.navigateTo(ViewRoute.IMAGE_DETAIL)
        }

        fun onReport() {
            data.scanData?.let { appState.setCurrentScan(it) }
            appState.navigateTo(ViewRoute.REPORT)
        }

        fun onShare() {
            data.scanData?.let { appState.setCurrentScan(it) }
            appState.navigateTo(ViewRoute.SHARE)
        }

        fun onMoreInfo() {
            // Logic to handle "More info" action
            Log.d("ScanCardViewModel", "More info clicked")
            showMoreInfoDialog.value = true
        }

        // Function to close the More Info dialog
        fun onCloseMoreInfoDialog() {
            showMoreInfoDialog.value = false
        }

        fun onDelete() {
            // Logic to handle "Delete" action
            Log.d("ScanCardViewModel", "Delete clicked")
            showDeleteDialog.value = true
        }

        // Function to close the More Info dialog
        fun onCloseDeleteDialog() {
            showDeleteDialog.value = false
        }
    }
