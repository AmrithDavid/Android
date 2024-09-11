// Package declaration for the app
package com.example.dma_swa_001

// Importing necessary libraries and components
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dma_swa_001.ui.theme.DividerColor
import com.example.dma_swa_001.ui.theme.SubheadingColor
import com.example.dma_swa_001.ui.theme.Dmaswa001Theme
import com.example.dma_swa_001.viewmodel.ScanViewModel
import com.example.dma_swa_001.viewmodel.PatientCard
import androidx.compose.ui.platform.LocalContext



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
            Dmaswa001Theme {
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
        topBar = { TopNavigationBar(greeting = greeting) },
        containerColor = MaterialTheme.colorScheme.background
        // This content lambda defines the main content area
        // innerPadding is not a parameter of Scaffold but a parameter provided by Scaffold to its content lambda
        // Scaffold provides this lambda with a PaddingValues object (named innerPadding here)
        // This innerPadding contains the padding values needed to avoid overlapping with Scaffold elements like top bar
    ) { innerPadding ->
        // here the innerPadding is applied to the CardList via its modifier (main area content)
        CardList(modifier = Modifier.padding(innerPadding), patientCards = patientCards)
    }
}

// TopNavigationBar is a custom composable function for the app's top bar
@Composable
fun TopNavigationBar(greeting: String) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = statusBarHeight)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Row for menu icon, greeting text, and logomark
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu icon and greeting text
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(27.dp))

                Text(
                    text = greeting,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Logomark
            Image(
                painter = painterResource(id = R.drawable.img_logomark),
                contentDescription = "Logomark",
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row for search bar and filter icon
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Search TextField
            TextField(
                value = "",
                onValueChange = { /* TODO: Handle search input */ },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E1FF).copy(alpha = 0.3f)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        "Search",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_voice),
                        contentDescription = "Voice Search",
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Filter icon
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_on),
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E1FF).copy(alpha = 0.3f))
                    .padding(12.dp)
            )
        }
    }
}

// CardList is a composable function that displays a list of EmptyCard items
@Composable
fun CardList(modifier: Modifier = Modifier, patientCards: List<PatientCard>) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Create EmptyCard items for each patient card
        items(patientCards.size) { index ->
            EmptyCard(patientCard = patientCards[index])
        }
    }
}

// EmptyCard is a composable function that represents a single card in the list
@Composable
fun EmptyCard(patientCard: PatientCard) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(patientCard.imageName, "drawable", context.packageName)

    // Card composable for the main container
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Column to hold all card content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row for patient image and details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Patient image
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Patient Image",
                    modifier = Modifier
                        .width(80.dp)
                        .height(125.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit
                )

                // Column for patient details
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)) {
                    Text(
                        text = "Study Description",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patientCard.patientName,
                        style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Patient details (Date, ID, Modality, Expiration)
                    Row {
                        Text("Date: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCard.date, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Patient ID: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCard.patientId, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Modality: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCard.modality, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                    Row {
                        Text("Expires in: ", style = MaterialTheme.typography.labelMedium.copy(color = SubheadingColor))
                        Text(patientCard.expiresIn, style = MaterialTheme.typography.bodySmall.copy(color = SubheadingColor))
                    }
                }

                // More options icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Row for action buttons (Images, Report, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonData = listOf(
                    Triple("Images", R.drawable.ic_radiology, "Radiology Icon"),
                    Triple("Report", R.drawable.ic_report, "Report Icon"),
                    Triple("Share", R.drawable.ic_share, "Share Icon")
                )
                buttonData.forEach { (text, iconRes, contentDescription) ->
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(9.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = contentDescription,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                                maxLines = 1,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// Preview function to visualise the ScanScreen
@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    Dmaswa001Theme {
        ScanScreen()
    }
}


