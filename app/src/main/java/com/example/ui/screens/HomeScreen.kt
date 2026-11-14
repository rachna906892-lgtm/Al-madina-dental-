package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.CleanHands
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.ClinicRepository
import com.example.data.ClinicStatus
import com.example.data.PatientReview
import com.example.data.TreatmentCategory
import com.example.data.TreatmentItem
import com.example.ui.components.ClinicStatusBadge
import com.example.ui.components.ContactUsSectionCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StarRatingRow
import com.example.ui.components.VerifiedBadge

@Composable
fun HomeScreen(
    clinicStatus: ClinicStatus,
    featuredReview: PatientReview,
    isWideScreen: Boolean,
    onBookVisitClick: () -> Unit,
    onDirectionsClick: () -> Unit,
    onCallNowClick: () -> Unit,
    onCopyAddressClick: () -> Unit,
    onCopyPhoneClick: () -> Unit,
    onShareClinicClick: () -> Unit,
    onViewDoctorClick: () -> Unit,
    onViewAllTreatmentsClick: () -> Unit,
    onTreatmentSelected: (TreatmentItem) -> Unit,
    onViewScheduleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            HeroBannerCard(
                clinicStatus = clinicStatus,
                isWideScreen = isWideScreen,
                onBookVisitClick = onBookVisitClick,
                onDirectionsClick = onDirectionsClick
            )
        }

        item {
            TrustHighlightsGrid(isWideScreen = isWideScreen)
        }

        item {
            DoctorSabaSpotlightCard(
                onViewDoctorClick = onViewDoctorClick
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    SectionHeader(
                        eyebrow = "Specialized Dental Care",
                        title = "Our Dental Services",
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = onViewAllTreatmentsClick,
                        modifier = Modifier.testTag("home_all_treatments_button")
                    ) {
                        Text("All Services")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ClinicRepository.treatments.take(4), key = { it.id }) { item ->
                        FeaturedTreatmentTile(
                            treatment = item,
                            onClick = { onTreatmentSelected(item) }
                        )
                    }
                }
            }
        }

        item {
            PatientPraiseCard(
                review = featuredReview,
                onReadMoreClick = onViewDoctorClick
            )
        }

        item {
            QuickTimingsAndLocationCard(
                clinicStatus = clinicStatus,
                onViewScheduleClick = onViewScheduleClick,
                onDirectionsClick = onDirectionsClick
            )
        }

        item {
            ContactUsSectionCard(
                onCallNowClick = onCallNowClick,
                onOpenMapsClick = onDirectionsClick,
                onCopyAddressClick = onCopyAddressClick,
                onCopyPhoneClick = onCopyPhoneClick,
                onShareClinicClick = onShareClinicClick,
                callButtonTestTag = "home_call_now_button"
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HeroBannerCard(
    clinicStatus: ClinicStatus,
    isWideScreen: Boolean,
    onBookVisitClick: () -> Unit,
    onDirectionsClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isWideScreen) 340.dp else 390.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_clinic_hero),
                contentDescription = "Al Madina Dental Clinic interior",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xCC062E2B),
                                Color(0x990A3F3A),
                                Color(0xF0052623)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClinicStatusBadge(
                        status = clinicStatus,
                        onDarkSurface = true,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFFBE9D7).copy(alpha = 0.96f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = Color(0xFFB56C35),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "5.0",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF3E1F08)
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "KHUSHBOO ENCLAVE • BAREILLY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB4ECE4)
                    )
                    Text(
                        text = ClinicRepository.CLINIC_NAME,
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Text(
                        text = "Gentle, hygienic & painless dental care by Dr. Saba — trusted by families, women & children.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE3F2EF)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onBookVisitClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD0EFEA),
                                contentColor = Color(0xFF05332F)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("hero_book_visit_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Book Visit", maxLines = 1)
                        }
                        FilledTonalButton(
                            onClick = onDirectionsClick,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White.copy(alpha = 0.18f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("hero_directions_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Directions,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Directions", maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrustHighlightsGrid(isWideScreen: Boolean) {
    val pillars = listOf(
        Triple(Icons.Rounded.HealthAndSafety, "Painless Care", "Expert RCT & atraumatic extraction"),
        Triple(Icons.Rounded.FamilyRestroom, "Women & Kids", "Polite, trusted female dentist"),
        Triple(Icons.Rounded.CleanHands, "Sterile Setup", "Modern equipment & infection control"),
        Triple(Icons.Outlined.Payments, "Reasonable Fees", "Transparent & affordable charges")
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (isWideScreen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pillars.forEach { (icon, title, desc) ->
                    TrustTile(
                        icon = icon,
                        title = title,
                        subtitle = desc,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TrustTile(
                    icon = pillars[0].first,
                    title = pillars[0].second,
                    subtitle = pillars[0].third,
                    modifier = Modifier.weight(1f)
                )
                TrustTile(
                    icon = pillars[1].first,
                    title = pillars[1].second,
                    subtitle = pillars[1].third,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TrustTile(
                    icon = pillars[2].first,
                    title = pillars[2].second,
                    subtitle = pillars[2].third,
                    modifier = Modifier.weight(1f)
                )
                TrustTile(
                    icon = pillars[3].first,
                    title = pillars[3].second,
                    subtitle = pillars[3].third,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TrustTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DoctorSabaSpotlightCard(onViewDoctorClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dr_saba),
                    contentDescription = "Dr. Saba, Lead Dentist at Al Madina Dental Clinic",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(78.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "MEET YOUR DENTIST",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = ClinicRepository.DOCTOR_NAME,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Qualified, Polite & Experienced Female Dentist",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Dr. Saba listens to every patient carefully, explains each procedure clearly beforehand, and works with gentle, highly skilled hands—making every visit reassuring for women, children, and families.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            FilledTonalButton(
                onClick = onViewDoctorClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("home_doctor_cta")
            ) {
                Text("View Dr. Saba's Profile & Patient Reviews")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun FeaturedTreatmentTile(
    treatment: TreatmentItem,
    onClick: () -> Unit
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        modifier = Modifier
            .width(248.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .testTag("featured_treatment_${treatment.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Text(
                    text = treatment.badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Text(
                text = treatment.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = treatment.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MedicalServices,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = treatment.durationEstimate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PatientPraiseCard(
    review: PatientReview,
    onReadMoreClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRatingRow(rating = review.rating, starSize = 18)
                VerifiedBadge()
            }
            Icon(
                imageVector = Icons.Rounded.FormatQuote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "\"${review.quote}\"",
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = review.author,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = review.patientContext,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(onClick = onReadMoreClick) {
                    Text("Read Reviews")
                }
            }
        }
    }
}

@Composable
private fun QuickTimingsAndLocationCard(
    clinicStatus: ClinicStatus,
    onViewScheduleClick: () -> Unit,
    onDirectionsClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    text = "Clinic Timings & Address",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Mon – Sat: 11:00 AM – 2:00 PM  •  5:00 PM – 10:00 PM",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sunday: Closed  •  Current status: ${clinicStatus.headline}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = ClinicRepository.FULL_ADDRESS,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onViewScheduleClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("home_schedule_button")
                ) {
                    Text("Schedule & Book")
                }
                FilledTonalButton(
                    onClick = onDirectionsClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                ) {
                    Text("Open Maps")
                }
            }
        }
    }
}
