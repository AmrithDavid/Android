// Package declaration for the app
@file:Suppress("ktlint:standard:no-wildcard-imports", "ktlint:standard:function-naming")

package com.singularhealth.android3dicom.view

// Importing necessary libraries and components
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.view.components.NavigationBar
import com.singularhealth.android3dicom.view.components.ScanCard
import com.singularhealth.android3dicom.viewmodel.ScanViewModel

// ScanLibraryActivity is the main entry point of the app
class ScanLibraryActivity : ComponentActivity() {
    // onCreate is called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        // Override onCreate to set up the activity's user interface and initial state
        // savedInstanceState: Bundle? parameter contains the activity's previously saved state, if available

        // Call the superclass implementation to complete the creation of the activity
        // This ensures all the basic setup from the parent class is performed
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display, allowing app to draw behind system UI and apply the same colour as the top bar to integrate it visually
        enableEdgeToEdge()
        // Method provided by ComponentActivity to set the content of the activity using Jetpack Compose
        setContent {
            // Composable to apply the custom theme to the entire app
            Android3DicomTheme {
                // Scaffold composable provides a basic material design layout structure
                // Takes several parameters with a last being a lambda function so it can be defined outside of parameter list (trailing lambda syntax)
                // In Kotlin, when a function's last parameter is a lambda it can be placed
                // All Scaffolds require a content lambda to define the content of the main area of the screen (between the top and possible bottom bar)
                ScanScreen()
            }
        }
    }
}

@Composable
fun ScanScreen(viewModel: ScanViewModel = viewModel()) {
    val greeting by viewModel.greeting.collectAsState()
    val patientCards by viewModel.patientCards.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { NavigationBar(greeting = greeting) },
        containerColor = MaterialTheme.colorScheme.background,
        // This content lambda defines the main content area
        // innerPadding is not a parameter of Scaffold but a parameter provided by Scaffold to its content lambda
        // Scaffold provides this lambda with a PaddingValues object (named innerPadding here)
        // This innerPadding contains the padding values needed to avoid overlapping with Scaffold elements like top bar
    ) { innerPadding ->
        // here the innerPadding is applied to the CardList via its modifier (main area content)
        CardList(modifier = Modifier.padding(innerPadding), patientCards = patientCards)
    }
}

// CardList displays a list of ScanCard items
@Composable
fun CardList(
    modifier: Modifier = Modifier,
    patientCards: List<PatientCardData>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Create EmptyCard items for each patient card
        items(patientCards.size) { index ->
            ScanCard(patientCardData = patientCards[index])
        }
    }
}

// Visualise the ScanScreen
@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    Android3DicomTheme {
        ScanScreen()
    }
}
