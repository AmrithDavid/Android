package com.singularhealth.android3dicom.viewmodel

import androidx.lifecycle.ViewModel
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.view.ViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel
    @Inject
    constructor(
        private val appState: AppState,
    ) : ViewModel() {
        private val _cardData =
            MutableStateFlow(
                PatientCardData(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                ),
            )
        val cardData: StateFlow<PatientCardData> = _cardData.asStateFlow()

        fun onViewImages() {
            appState.navigateTo(ViewRoute.IMAGE_DETAIL)
        }

        fun onDownloadPdf() {
        }

        fun onBack() {
            appState.navigateBack()
        }
    }
