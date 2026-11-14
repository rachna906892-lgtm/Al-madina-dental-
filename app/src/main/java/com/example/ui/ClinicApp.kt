package com.example.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppointmentConfirmation
import com.example.data.ClinicRepository
import com.example.data.ClinicTab
import com.example.ui.screens.DoctorAndReviewsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TreatmentsScreen
import com.example.ui.screens.VisitAndBookScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicApp(
    viewModel: ClinicViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.refreshClinicStatus()
    }

    // Back press on secondary tabs navigates back to Home cleanly
    BackHandler(enabled = uiState.selectedTab != ClinicTab.HOME) {
        viewModel.selectTab(ClinicTab.HOME)
    }

    val openMapsAction: () -> Unit = {
        val encodedQuery = Uri.encode("${ClinicRepository.CLINIC_NAME}, ${ClinicRepository.FULL_ADDRESS}")
        val geoIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$encodedQuery"))
        try {
            context.startActivity(geoIntent)
        } catch (_: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedQuery")
            )
            try {
                context.startActivity(webIntent)
            } catch (_: ActivityNotFoundException) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("No maps app found to open directions.")
                }
            }
        }
    }

    val copyAddressAction: () -> Unit = {
        clipboardManager.setText(AnnotatedString(ClinicRepository.FULL_ADDRESS))
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Clinic address copied to clipboard.")
        }
    }

    val copyPhoneAction: () -> Unit = {
        clipboardManager.setText(AnnotatedString(ClinicRepository.PHONE_DISPLAY))
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Phone number copied to clipboard.")
        }
    }

    val callClinicAction: () -> Unit = {
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ClinicRepository.PHONE_DIAL}"))
        try {
            context.startActivity(dialIntent)
        } catch (_: ActivityNotFoundException) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("No phone app found to place the call.")
            }
        }
    }

    val shareConfirmationAction: (AppointmentConfirmation) -> Unit = { confirmation ->
        val shareText = buildString {
            appendLine("Al Madina Dental Clinic — Appointment Request")
            appendLine("Ref: ${confirmation.referenceId}")
            appendLine("Patient: ${confirmation.patientName} (${confirmation.patientGroup.label})")
            appendLine("Phone: ${confirmation.phoneNumber}")
            appendLine("Preferred Day: ${confirmation.preferredDay}")
            appendLine("Session: ${confirmation.sessionSlot.label} (${confirmation.sessionSlot.timeRange})")
            appendLine("Concern: ${confirmation.treatmentConcern}")
            if (confirmation.notes.isNotBlank()) {
                appendLine("Notes: ${confirmation.notes}")
            }
            appendLine("Address: ${ClinicRepository.FULL_ADDRESS}")
        }
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Appointment Request - ${ClinicRepository.CLINIC_NAME}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share Appointment Request"))
    }

    val shareClinicAction: () -> Unit = {
        val mapsQuery = Uri.encode("${ClinicRepository.CLINIC_NAME}, ${ClinicRepository.FULL_ADDRESS}")
        val mapsUrl = "https://www.google.com/maps/search/?api=1&query=$mapsQuery"
        val shareText = buildString {
            appendLine("${ClinicRepository.CLINIC_NAME} — ${ClinicRepository.DOCTOR_NAME}")
            appendLine("Gentle, Hygienic & Painless Dental Care (RCT, Extraction, General & Pediatric Dentistry)")
            appendLine("Phone: ${ClinicRepository.PHONE_DISPLAY}")
            appendLine("Timings: Mon–Sat 11:00 AM–2:00 PM & 5:00 PM–10:00 PM (Sun Closed)")
            appendLine("Address: ${ClinicRepository.FULL_ADDRESS}")
            appendLine("Google Maps: $mapsUrl")
        }
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, ClinicRepository.CLINIC_NAME)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share ${ClinicRepository.CLINIC_NAME}"))
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when (uiState.selectedTab) {
                                ClinicTab.HOME -> ClinicRepository.CLINIC_NAME
                                ClinicTab.TREATMENTS -> "Dental Services"
                                ClinicTab.DOCTOR_REVIEWS -> "Dr. Saba & Reviews"
                                ClinicTab.VISIT_BOOK -> "Visit & Book"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = shareClinicAction,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("topbar_share_clinic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = "Share clinic details and map link"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    windowInsets = WindowInsets.statusBars
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        ClinicTab.entries.forEach { tab ->
                            val selected = uiState.selectedTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = tabIcon(tab, selected),
                                        contentDescription = tab.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.testTag("nav_tab_${tab.route}")
                            )
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        ClinicTab.entries.forEach { tab ->
                            val selected = uiState.selectedTab == tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = tabIcon(tab, selected),
                                        contentDescription = tab.label
                                    )
                                },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag("rail_tab_${tab.route}")
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(modifier = Modifier.widthIn(max = 880.dp).fillMaxSize()) {
                        when (uiState.selectedTab) {
                            ClinicTab.HOME -> HomeScreen(
                                clinicStatus = uiState.clinicStatus,
                                featuredReview = uiState.reviews.first(),
                                isWideScreen = isWideScreen,
                                onBookVisitClick = { viewModel.selectTab(ClinicTab.VISIT_BOOK) },
                                onDirectionsClick = openMapsAction,
                                onCallNowClick = callClinicAction,
                                onCopyAddressClick = copyAddressAction,
                                onCopyPhoneClick = copyPhoneAction,
                                onShareClinicClick = shareClinicAction,
                                onViewDoctorClick = { viewModel.selectTab(ClinicTab.DOCTOR_REVIEWS) },
                                onViewAllTreatmentsClick = { viewModel.selectTab(ClinicTab.TREATMENTS) },
                                onTreatmentSelected = { item ->
                                    viewModel.openTreatmentDetail(item.id)
                                },
                                onViewScheduleClick = { viewModel.selectTab(ClinicTab.VISIT_BOOK) }
                            )

                            ClinicTab.TREATMENTS -> TreatmentsScreen(
                                selectedCategory = uiState.selectedTreatmentCategory,
                                expandedTreatmentId = uiState.expandedTreatmentId,
                                onSelectCategory = viewModel::selectTreatmentCategory,
                                onToggleExpand = viewModel::toggleTreatmentExpanded,
                                onBookTreatment = viewModel::bookForTreatment
                            )

                            ClinicTab.DOCTOR_REVIEWS -> DoctorAndReviewsScreen(
                                reviews = uiState.reviews,
                                selectedTag = uiState.selectedReviewTag,
                                showReviewSheet = uiState.showReviewSheet,
                                onSelectTag = viewModel::selectReviewTag,
                                onBookWithDrSabaClick = { viewModel.selectTab(ClinicTab.VISIT_BOOK) },
                                onOpenReviewSheet = { viewModel.setShowReviewSheet(true) },
                                onDismissReviewSheet = { viewModel.setShowReviewSheet(false) },
                                onSubmitReview = viewModel::addPatientReview
                            )

                            ClinicTab.VISIT_BOOK -> VisitAndBookScreen(
                                uiState = uiState,
                                onCallNowClick = callClinicAction,
                                onOpenMapsClick = openMapsAction,
                                onCopyAddressClick = copyAddressAction,
                                onCopyPhoneClick = copyPhoneAction,
                                onShareClinicClick = shareClinicAction,
                                onPatientNameChange = viewModel::updatePatientName,
                                onPhoneChange = viewModel::updatePhoneNumber,
                                onPatientGroupSelect = viewModel::selectPatientGroup,
                                onDaySelect = viewModel::selectPreferredDay,
                                onSessionSelect = viewModel::selectSessionSlot,
                                onTreatmentConcernSelect = viewModel::selectTreatmentConcern,
                                onNotesChange = viewModel::updateNotes,
                                onSubmitAppointment = viewModel::submitAppointmentRequest,
                                onShareConfirmation = shareConfirmationAction,
                                onResetAppointment = viewModel::resetAppointmentForm
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun tabIcon(tab: ClinicTab, selected: Boolean): ImageVector = when (tab) {
    ClinicTab.HOME -> if (selected) Icons.Rounded.Home else Icons.Outlined.Home
    ClinicTab.TREATMENTS -> if (selected) Icons.Rounded.MedicalServices else Icons.Outlined.MedicalServices
    ClinicTab.DOCTOR_REVIEWS -> if (selected) Icons.Rounded.Person else Icons.Outlined.Person
    ClinicTab.VISIT_BOOK -> if (selected) Icons.Rounded.CalendarMonth else Icons.Outlined.CalendarMonth
}
