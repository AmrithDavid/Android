@file:Suppress("ktlint:standard:no-wildcard-imports", "ktlint:standard:function-naming")

package com.singularhealth.android3dicom.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.view.components.*
import com.singularhealth.android3dicom.view.components.NavigationBar
import com.singularhealth.android3dicom.view.components.ScanCard
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
import com.singularhealth.android3dicom.viewmodel.LoginViewModelFactory
import com.singularhealth.android3dicom.viewmodel.ScanLibraryViewModel

class ScanLibraryActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val startForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("MY_APP_TAG", "Returned result from activity")
            }
        }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(
                this@ScanLibraryActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence,
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@ScanLibraryActivity, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(this@ScanLibraryActivity, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                        // TODO: Navigate to main screen or update state to show main content
                        // should probably be a view-model or app-state level call
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(this@ScanLibraryActivity, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                },
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupBiometricPrompt()
        promptInfo =
            BiometricPrompt.PromptInfo
                .Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()

        setContent {
            /*Android3DicomTheme {
                NavigationSetup()
            }*/

            // ------- Test code for DMA-26
            var showLoginSetup by remember { mutableStateOf(true) }

            if (showLoginSetup) {
                /*LoginSetupView(
                    onBackClick = { /* TODO: Handle back click if needed */ },
                    onBiometricLoginClick = {
                        val biometricManager = BiometricManager.from(this@ScanLibraryActivity)
                        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                            BiometricManager.BIOMETRIC_SUCCESS -> {
                                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                                val biometricFragment = BiometricFragment()
                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_view, biometricFragment)
                                    .commit()
                                biometricFragment.authenticate()
                                // biometricPrompt.authenticate(promptInfo)
                            }
                            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                                Log.e("MY_APP_TAG", "Biometric features require enrolment.")
                                val enrollIntent =
                                    Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                        putExtra(
                                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                                BiometricManager.Authenticators.DEVICE_CREDENTIAL,
                                        )
                                    }

                                startForResult.launch(enrollIntent)
                            }
                            else -> {
                                Toast
                                    .makeText(
                                        this@ScanLibraryActivity,
                                        "Biometric authentication not available",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                            }
                        }
                    },
                )*/
            } else {
                ScanScreen(viewModel(), rememberNavController(), remember { mutableStateOf("") })
            }
        }
    }
}

@Composable
fun NavigationSetup() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val searchQuery = remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "scanScreen" else "login",
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("scanScreen") {
                        popUpTo("login") { inclusive = true }
                    }
                },
            )
        }
        composable("scanScreen") {
            ScanScreen(
                navController = navController,
                searchQuery = searchQuery,
            )
        }
        composable("mainImageMenu") {
            MainImageMenu(navController = navController)
        }
        composable("shareView") {
            ShareView(navController = navController)
        }
    }

    // Pass a lambda to update the search query
    SideEffect {
        searchQuery.value = searchQuery.value
    }
}

@Composable
fun ScanScreen(
    viewModel: ScanLibraryViewModel = viewModel(),
    navController: NavController,
    searchQuery: MutableState<String>,
) {
    val greeting by viewModel.greeting.collectAsState()
    val patientCards by viewModel.patientCards.collectAsState()
    val isSideMenuVisible by viewModel.isSideMenuVisible.collectAsState()

    // Control system UI color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                if (isSideMenuVisible) {
                    Color.Transparent.toArgb()
                } else {
                    DarkBlue.toArgb()
                }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isSideMenuVisible
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NavigationBar(
                greeting = greeting,
                onMenuClick = { viewModel.toggleSideMenu() },
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery ->
                    searchQuery.value = newQuery.value // Update the search query state
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        val filteredCards =
            patientCards.filter { card ->
                card.patientName.contains(searchQuery.value, ignoreCase = true) ||
                    card.patientId.contains(searchQuery.value, ignoreCase = true) ||
                    card.modality.contains(searchQuery.value, ignoreCase = true) ||
                    card.fileName.contains(searchQuery.value, ignoreCase = true)
            }

        if (filteredCards.isNotEmpty()) {
            CardList(
                modifier = Modifier.padding(innerPadding),
                patientCards = filteredCards,
                onImageButtonClick = { navController.navigate("mainImageMenu") },
            )
        } else {
            EmptyStateView()
        }
    }
    // Semi-transparent overlay
    if (isSideMenuVisible) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0x52000000)),
            // #00000052
        )
    }

    // SideMenu
    if (isSideMenuVisible) {
        ScanLibraryMenu(
            onCloseMenu = { viewModel.toggleSideMenu() },
            onHomeClick = { viewModel.onHomeClick() },
            onClearCacheClick = { viewModel.onClearCacheClick() },
            onBiometricClick = { viewModel.onBiometricClick() },
            onAboutClick = { viewModel.onAboutClick() },
            onSupportClick = { viewModel.onSupportClick() },
            onLogoutClick = { viewModel.onLogoutClick() },
        )
    }
}

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    patientCards: List<PatientCardData>,
    onImageButtonClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(patientCards.size) { index ->
            ScanCard(
                patientCardData = patientCards[index],
                onImageButtonClick = onImageButtonClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    Android3DicomTheme {
        val navController = rememberNavController()
        val searchQuery = remember { mutableStateOf("") }

        ScanScreen(
            navController = navController,
            searchQuery = searchQuery,
        )
    }
}
