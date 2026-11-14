package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.PrecisionManufacturing
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.ClinicRepository
import com.example.data.PatientReview
import com.example.data.ReviewTag
import com.example.ui.components.HighlightPill
import com.example.ui.components.SectionHeader
import com.example.ui.components.StarRatingRow
import com.example.ui.components.VerifiedBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAndReviewsScreen(
    reviews: List<PatientReview>,
    selectedTag: ReviewTag,
    showReviewSheet: Boolean,
    onSelectTag: (ReviewTag) -> Unit,
    onBookWithDrSabaClick: () -> Unit,
    onOpenReviewSheet: () -> Unit,
    onDismissReviewSheet: () -> Unit,
    onSubmitReview: (author: String, context: String, rating: Int, quote: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredReviews = if (selectedTag == ReviewTag.ALL) {
        reviews
    } else {
        reviews.filter { it.tags.contains(selectedTag) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            DoctorProfileHeroCard(onBookClick = onBookWithDrSabaClick)
        }

        item {
            CarePhilosophyStrip()
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    SectionHeader(
                        eyebrow = "5.0 ★ Patient Trust",
                        title = "Patient Reviews",
                        modifier = Modifier.weight(1f)
                    )
                    FilledTonalButton(
                        onClick = onOpenReviewSheet,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("open_review_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Write Review")
                    }
                }

                RatingOverviewBanner(reviewCount = reviews.size)
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ReviewTag.entries) { tag ->
                    FilterChip(
                        selected = tag == selectedTag,
                        onClick = { onSelectTag(tag) },
                        label = { Text(tag.label) },
                        modifier = Modifier.testTag("review_filter_${tag.name}")
                    )
                }
            }
        }

        items(filteredReviews, key = { it.id }) { review ->
            ReviewDetailCard(review = review)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showReviewSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = onDismissReviewSheet,
            sheetState = sheetState
        ) {
            AddReviewSheetContent(
                onDismiss = onDismissReviewSheet,
                onSubmit = onSubmitReview
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DoctorProfileHeroCard(onBookClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dr_saba),
                    contentDescription = "Dr. Saba, Lead Dentist",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "LEAD DENTIST",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = ClinicRepository.DOCTOR_NAME,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Qualified, Gentle & Experienced Female Dentist",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Known across Khushboo Enclave and Bareilly for her calm, polite manner and highly skilled procedural hands. Dr. Saba listens carefully to every concern, explains each step of treatment transparently, and specializes in painless Root Canal Treatment (RCT) and gentle tooth extractions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HighlightPill(
                    icon = Icons.Rounded.Favorite,
                    label = "Trusted for Women & Kids"
                )
                HighlightPill(
                    icon = Icons.Rounded.PrecisionManufacturing,
                    label = "Painless RCT & Extraction",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Button(
                onClick = onBookClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("doctor_book_consultation_button")
            ) {
                Text("Book Consultation with Dr. Saba")
            }
        }
    }
}

@Composable
private fun CarePhilosophyStrip() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        PhilosophyRow(
            icon = Icons.Rounded.Hearing,
            title = "Listens Carefully",
            description = "Unhurried consultations where your comfort and symptoms always come first."
        )
        PhilosophyRow(
            icon = Icons.Outlined.Forum,
            title = "Explains Every Procedure",
            description = "Clear, reassuring walkthrough before any dental work begins—no surprises."
        )
    }
}

@Composable
private fun PhilosophyRow(
    icon: ImageVector,
    title: String,
    description: String
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RatingOverviewBanner(reviewCount: Int) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.65f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "5.0",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    StarRatingRow(rating = 5, starSize = 18)
                    Text(
                        text = "Based on $reviewCount patient testimonials",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewDetailCard(review: PatientReview) {
    OutlinedCard(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRatingRow(rating = review.rating, starSize = 16)
                if (review.verifiedVisit) {
                    VerifiedBadge()
                }
            }
            Text(
                text = "\"${review.quote}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
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
        }
    }
}

@Composable
private fun AddReviewSheetContent(
    onDismiss: () -> Unit,
    onSubmit: (author: String, context: String, rating: Int, quote: String) -> Unit
) {
    var author by remember { mutableStateOf("") }
    var visitType by remember { mutableStateOf("Dental Care • Bareilly") }
    var rating by remember { mutableIntStateOf(5) }
    var quote by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Share Your Clinic Experience",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Your Rating:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            (1..5).forEach { star ->
                IconButton(
                    onClick = { rating = star },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = if (star <= rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                        contentDescription = "Rate $star stars",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Your Name (Optional)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("review_author_input")
        )

        OutlinedTextField(
            value = visitType,
            onValueChange = { visitType = it },
            label = { Text("Treatment / Visit Context") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quote,
            onValueChange = {
                quote = it
                errorMsg = null
            },
            label = { Text("How was your visit with Dr. Saba?") },
            minLines = 3,
            maxLines = 5,
            isError = errorMsg != null,
            supportingText = errorMsg?.let { { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("review_quote_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilledTonalButton(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (quote.trim().length < 6) {
                        errorMsg = "Please write a few words about your visit."
                    } else {
                        onSubmit(author, visitType, rating, quote)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
                    .testTag("submit_review_button")
            ) {
                Text("Post Review")
            }
        }
    }
}
