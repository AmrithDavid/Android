package com.example.dma_swa_001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.example.dma_swa_001.ui.theme.Dmaswa001Theme

class MainActivity : ComponentActivity() { // Declaration of MainActivity class which inherits from ComponentActivity (part of Jetpack Compose)
    override fun onCreate(savedInstanceState: Bundle?) { // onCreate method is overridden to allow for custom UI initialisation
        super.onCreate(savedInstanceState) // calls the parent implementation of OnCreate
        enableEdgeToEdge() // Enables edge to edge display
        setContent { // Used to define the Composable UI hierarchy
            Dmaswa001Theme { // Custom theme that wraps the entire UI, applying consistent theming
                Scaffold( // Composable that is the top level container. modifier, topBar and containerColor are parameters of the Scaffold class and are provided as arguments here when Scaffold is called.
                    modifier = Modifier.fillMaxSize(), // A new modifier function is created with fillMaxSize to make Scaffold fill the screen
                    topBar = { TopNavigationBar() }, // topbar is a parameter of the Scaffold composable and in this case the TopNavigationBar compos able is passed to be used as the top bar content
                    containerColor = Color(0xFFFFFFFF) // Sets the background color of the entire Scaffold to white
                ) { innerPadding -> // innerPadding is provided to Scaffold's content to prevent overlapping
                    CardList(modifier = Modifier.padding(innerPadding)) // CardList composable is called and innerPadding is applied to make sure CardList content doesn't overlap with other UI elements (Scaffold elements and system UI elements).
                }
            }
        }
    }
}

@Composable
fun TopNavigationBar() { // Composable function for the top navigation bar
    Surface( // starts the call to the Surface composable function
        modifier = Modifier // Parameter of surface that's assigned a Modifier object
            .fillMaxWidth() // Makes the bar fill the full width of the screen
            .height(128.dp) // Sets the height to 128dp as specified
            .statusBarsPadding(), // Adds padding to account for the status bar, ensuring the bar starts below it
        color = Color(0xFF2E3176), // Sets the background color as specified in the UI information
        shadowElevation = 4.dp // Adds a shadow effect to create depth
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = "", // Placeholder for search text, should use a state variable in real implementation
                onValueChange = { /* TODO: Handle search input */ },
                modifier = Modifier
                    .align(Alignment.BottomStart) // aligns the textfield to the bottom-start of the Box
                    .padding(start = 16.dp, bottom = 16.dp, end = 80.dp) // Applies padding as specified
                    .height(56.dp) // Sets the height of the TextField
                    .fillMaxWidth() // Makes the TextField fill the available width
                    .clip(RoundedCornerShape(8.dp)) // Applies rounded corners to the TextField
                    .alpha(0.7f), // Sets the opacity of the TextField to 70%
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF0F1FF), // Sets the background color when not focused
                    focusedContainerColor = Color(0xFFF0F1FF), // Sets the background color when focused
                    unfocusedPlaceholderColor = Color.White, // Sets the placeholder text color when not focused
                    focusedPlaceholderColor = Color.White // Sets the placeholder text color when focused
                ),
                placeholder = {
                    Text(
                        "Search",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                },
                leadingIcon = { // adds a search icon to the left of the textfield
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                },
                textStyle = LocalTextStyle.current.copy(lineHeight = 24.sp, color = Color.White), // Sets the text style for input text
                singleLine = true // Restricts the TextField to a single line
            )
        }
    }
}

@Composable
fun CardList(modifier: Modifier = Modifier) {
    LazyColumn( // Efficiently renders only visible items - only creates cards that can be visible
        modifier = modifier, // allows the cardList to receive a modifier from its parent and then Passes the modifier received by CardList to LazyColumn, allowing the parent composable to modify the child (LazyColumn)
        contentPadding = PaddingValues(16.dp), // adds 16dp padding around all of LazyColumn
        verticalArrangement = Arrangement.spacedBy(16.dp) // Adds 16dp spacing between each item in the list
    ) {
        items(4) { index -> // Declares that LazyColumn should be prepared to display up to 4 items
            EmptyCard() // Calls EmptyCard composable to create a single card
        }
    }
}

@Composable
fun EmptyCard() { // Composable that defines an empty card
    Card( // Card composable
        modifier = Modifier // Sets up chain of modifiers for the Card
            .fillMaxWidth() // makes the card fill the max available width of the parent container
            .height(227.dp) // sets a fixed height of 227dp
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF) // sets the background color to white (#FFFFFF)
        ),
        shape = RoundedCornerShape(8.dp) // Sets the border radius to 8dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(171.dp)) // Creates space at the top of the card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(3) {
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .weight(1f) // makes each button take up equal space
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFFFFFFF) // Sets button background to white
                        ),
                        border = BorderStroke(2.dp, Color(0xFF2E3176)), // Adds a 2dp border with specified color
                        shape = RoundedCornerShape(4.dp) // Rounds the corners of the button
                    ) {
                        // Button content
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardListPreview() { // Preview function for the CardList
    Dmaswa001Theme {
        Scaffold(
            topBar = { TopNavigationBar() }, // Includes the TopNavigationBar in the preview
            containerColor = Color(0xFFFFFFFF) // Sets the background color of the entire Scaffold to white in the preview
        ) { innerPadding ->
            CardList(modifier = Modifier.padding(innerPadding)) // Applies innerPadding to CardList in the preview
        }
    }
}
