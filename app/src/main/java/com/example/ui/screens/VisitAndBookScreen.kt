package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.AppointmentConfirmation
import com.example.data.ClinicDayHours
import com.example.data.ClinicRepository
import com.example.data.ClinicStatus
import com.example.data.PatientGroup
import com.example.data.SessionSlot
import com.example.ui.ClinicUiState
import com.example.ui.components.ClinicStatusBadge
import com.example.ui.components.ContactUsSectionCard
import com.example.ui.components.SectionHeader

@Composable
fun VisitAndBookScreen(
    uiState: ClinicUiState,
    onCallNowClick: () -> Unit,
    onOpenMapsClick: () -> Unit,
    onCopyAddressClick: () -> Unit,
    onCopyPhoneClick: () -> Unit,
    onShareClinicClick: () -> Unit,
    onPatientNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPatientGroupSelect: (PatientGroup) -> Unit,
    onDaySelect: (String) -> Unit,
    onSessionSelect: (SessionSlot) -> Unit,
    onTreatmentConcernSelect: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmitAppointment: () -> Unit,
    onShareConfirmation: (AppointmentConfirmation) -> Unit,
    onResetAppointment: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SectionHeader(
                eyebrow = "Khushboo Enclave • Bareilly",
                title = "Visit Hours & Contact",
                subtitle = "Walk in during clinic hours, call us directly, or prepare your visit pass below."
            )
        }

        item {
            ContactUsSectionCard(
                onCallNowClick = onCallNowClick,
                onOpenMapsClick = onOpenMapsClick,
                onCopyAddressClick = onCopyAddressClick,
                onCopyPhoneClick = onCopyPhoneClick,
                onShareClinicClick = onShareClinicClick,
                callButtonTestTag = "contact_call_now_button"
            )
        }

        item {
            WeeklyHoursCard(
                clinicStatus = uiState.clinicStatus,
                schedule = ClinicRepository.weeklySchedule
            )
        }

        item {
            val confirmation = uiState.confirmation
            if (confirmation != null) {
                AppointmentPassCard(
                    confirmation = confirmation,
                    onShareClick = { onShareConfirmation(confirmation) },
                    onBookAnotherClick = onResetAppointment
                )
            } else {
                AppointmentRequestFormCard(
                    uiState = uiState,
                    onPatientNameChange = onPatientNameChange,
                    onPhoneChange = onPhoneChange,
                    onPatientGroupSelect = onPatientGroupSelect,
                    onDaySelect = onDaySelect,
                    onSessionSelect = onSessionSelect,
                    onTreatmentConcernSelect = onTreatmentConcernSelect,
                    onNotesChange = onNotesChange,
                    onSubmitClick = onSubmitAppointment
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WeeklyHoursCard(
    clinicStatus: ClinicStatus,
    schedule: List<ClinicDayHours>
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Weekly Clinic Timings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            ClinicStatusBadge(status = clinicStatus)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                schedule.forEachIndexed { index, dayHours ->
                    val isToday = dayHours.dayName.equals(clinicStatus.currentDayName, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isToday) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerLowest
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = dayHours.dayName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (dayHours.isClosed) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                                if (isToday) {
                                    Text(
                                        text = "(Today)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (dayHours.isClosed) {
                                Text(
                                    text = "Closed",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = dayHours.morningSlot,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = dayHours.eveningSlot,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    if (index == schedule.lastIndex - 1) {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppointmentRequestFormCard(
    uiState: ClinicUiState,
    onPatientNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPatientGroupSelect: (PatientGroup) -> Unit,
    onDaySelect: (String) -> Unit,
    onSessionSelect: (SessionSlot) -> Unit,
    onTreatmentConcernSelect: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Prepare Appointment Request",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Select your preferred session (Mon–Sat) with Dr. Saba to generate a ready-to-share visit pass.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = uiState.patientName,
                onValueChange = onPatientNameChange,
                label = { Text("Patient Full Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("patient_name_input")
            )

            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = onPhoneChange,
                label = { Text("Mobile Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phone_input")
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Patient Category",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PatientGroup.entries.forEach { group ->
                        FilterChip(
                            selected = uiState.selectedPatientGroup == group,
                            onClick = { onPatientGroupSelect(group) },
                            label = { Text(group.label) },
                            modifier = Modifier.testTag("group_chip_${group.name}")
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Preferred Day (Closed Sundays)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ClinicRepository.openDaysList) { dayName ->
                        FilterChip(
                            selected = uiState.preferredDay == dayName,
                            onClick = { onDaySelect(dayName) },
                            label = { Text(dayName) },
                            modifier = Modifier.testTag("day_chip_$dayName")
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Preferred Clinic Session",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SessionSlot.entries.forEach { slot ->
                        FilterChip(
                            selected = uiState.selectedSession == slot,
                            onClick = { onSessionSelect(slot) },
                            label = { Text("${slot.label} (${slot.timeRange})") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("session_chip_${slot.name}")
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Dental Concern / Treatment",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ClinicRepository.treatments) { treatment ->
                        FilterChip(
                            selected = uiState.selectedTreatmentConcern == treatment.title,
                            onClick = { onTreatmentConcernSelect(treatment.title) },
                            label = { Text(treatment.title) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = onNotesChange,
                label = { Text("Symptoms or Special Note (Optional)") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.formError != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = uiState.formError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }

            Button(
                onClick = onSubmitClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("submit_appointment_button")
            ) {
                Text("Generate Visit Request Pass")
            }
        }
    }
}

@Composable
private fun AppointmentPassCard(
    confirmation: AppointmentConfirmation,
    onShareClick: () -> Unit,
    onBookAnotherClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("appointment_confirmation_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = "Visit Request Ready",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Ref: ${confirmation.referenceId}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f))

            PassDetailRow("Patient", "${confirmation.patientName} (${confirmation.patientGroup.label})")
            PassDetailRow("Phone", confirmation.phoneNumber)
            PassDetailRow("Preferred Day", confirmation.preferredDay)
            PassDetailRow("Session", "${confirmation.sessionSlot.label} • ${confirmation.sessionSlot.timeRange}")
            PassDetailRow("Concern", confirmation.treatmentConcern)
            if (confirmation.notes.isNotBlank()) {
                PassDetailRow("Notes", confirmation.notes)
            }
            PassDetailRow("Clinic", "${ClinicRepository.CLINIC_NAME}, ${ClinicRepository.SHORT_ADDRESS}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onShareClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("share_appointment_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Pass")
                }
                FilledTonalButton(
                    onClick = onBookAnotherClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("reset_appointment_button")
                ) {
                    Text("Edit / New")
                }
            }
        }
    }
}

@Composable
private fun PassDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
            modifier = Modifier.width(108.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}
