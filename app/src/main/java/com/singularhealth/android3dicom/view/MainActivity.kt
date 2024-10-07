package com.singularhealth.android3dicom.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.utilities.BiometricUtils

class MainActivity : AppCompatActivity() {
    private val startForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result: ActivityResult ->
            BiometricUtils.onEnrolAttempt(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BiometricUtils.startForResult = startForResult
        enableEdgeToEdge()
        setContent {
            Android3DicomTheme {
                NavigationGraph()
            }
        }
    }
}
