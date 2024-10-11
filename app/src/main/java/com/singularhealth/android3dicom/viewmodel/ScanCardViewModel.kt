package com.singularhealth.android3dicom.viewmodel

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
    }
