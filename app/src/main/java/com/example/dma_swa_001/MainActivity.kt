package com.example.dma_swa_001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.dma_swa_001.ui.theme.Dmaswa001Theme

// MainActivity class - entry point of the app
class MainActivity : ComponentActivity() {
    // This declaration and call of onCreate followed by this call works together by first overrding to allow customisation and pass activity's previous state if any as a parameter
    override fun onCreate(savedInstanceState: Bundle?) {
        // Calling the default super class implementation of onCreate provides the necessary setup
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Set the content using Jetpack Compose. Main layout consists of Scaffold with a topnavbar + cardlist
        setContent { // Function to define UI
            Dmaswa001Theme { // Theme
                Scaffold( // Composable that implements the layout structure
                    modifier = Modifier.fillMaxSize(), // Scaffold fills screen
                    topBar = { TopNavigationBar() }, // top app bar set as definition in TopNavigationBar
                    containerColor = Color(0xFFFFFFFF) // Background colour is white
                    // innerPadding and CardList are still parameters even though they are not in parameter list - traling lambda syntax (function like structure)
                ) { innerPadding -> // contains amount of space taken up by system UI (status bar). not defined explicity scaffold knows already.
                    // Display a list of cards with padding
                    CardList(modifier = Modifier.padding(innerPadding)) //  applying the padding to cardlist so that it doesnt overlap with system UI
                }
            }
        }
    }
}

@Composable
fun TopNavigationBar() {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = emptyList()
    )

    // Load custom font
    val plusJakartaSans = GoogleFont("Plus Jakarta Sans")
    val plusJakartaSansFamily = FontFamily(
        Font(googleFont = plusJakartaSans, fontProvider = provider)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 128.dp)
            .statusBarsPadding()
            .background(Color(0xFF2E3176))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(27.dp))

            // Greeting text with custom font
            Text(
                text = "Hello Sam",
                style = TextStyle(
                    fontFamily = plusJakartaSansFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.15.sp,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = { /* TODO: Handle search input */ },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E1FF).copy(alpha = 0.3f)),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            "Search",
                            color = Color.White.copy(alpha = 0.5f),
                            style = TextStyle(
                                fontFamily = plusJakartaSansFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.sp
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = plusJakartaSansFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp,
                        color = Color.White
                    ),
                    singleLine = true
                )

                // Mic icon inside the TextField
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "Voice Search",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Filter icon
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E1FF).copy(alpha = 0.3f))
                    .padding(12.dp)
            )
        }
    }
}

// Composable to create a list of empty cards
@Composable
fun CardList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(4) {
            EmptyCard() // Render an empty card
        }
    }
}

// Composable to create an empty card
@Composable
fun EmptyCard() {
    val plusJakartaSans = FontFamily(
        Font(
            googleFont = GoogleFont("Plus Jakarta Sans"),
            fontProvider = GoogleFont.Provider(
                providerAuthority = "com.google.android.gms.fonts",
                providerPackage = "com.google.android.gms",
                certificates = emptyList()
            )
        )
    )

    val buttonColor = Color(0xFF2E3176)
    val titleColor = Color(0xFF1D1D1F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(227.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Title
            Text(
                text = "Study Description",
                style = TextStyle(
                    fontFamily = plusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    letterSpacing = 0.sp,
                    color = titleColor
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 121.dp, top = 18.dp, end = 106.dp)
            )

            // Buttons row at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonTexts = listOf("Images", "Report", "Share")
                buttonTexts.forEach { text ->
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFFFFFFF)
                        ),
                        border = BorderStroke(3.dp, buttonColor),
                        shape = RoundedCornerShape(9.dp),
                        contentPadding = PaddingValues(start = 36.dp, end = 12.dp)
                    ) {
                        Text(
                            text = text,
                            style = TextStyle(
                                fontFamily = plusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                letterSpacing = 0.12.sp,
                                color = buttonColor
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// Preview function to visualise the CardList
@Preview(showBackground = true)
@Composable
fun CardListPreview() {
    Dmaswa001Theme {
        Scaffold(
            topBar = { TopNavigationBar() },
            containerColor = Color(0xFFFFFFFF)
        ) { innerPadding ->
            CardList(modifier = Modifier.padding(innerPadding))
        }
    }
}


