package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.AppointmentConfirmation
import com.example.data.ClinicHoursEvaluator
import com.example.data.ClinicRepository
import com.example.data.ClinicStatus
import com.example.data.ClinicTab
import com.example.data.PatientGroup
import com.example.data.PatientReview
import com.example.data.ReviewTag
import com.example.data.SessionSlot
import com.example.data.TreatmentCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import kotlin.random.Random

data class ClinicUiState(
    val selectedTab: ClinicTab = ClinicTab.HOME,
    val clinicStatus: ClinicStatus = ClinicHoursEvaluator.evaluate(),
    val selectedTreatmentCategory: TreatmentCategory = TreatmentCategory.ALL,
    val expandedTreatmentId: String? = "rct",
    val selectedReviewTag: ReviewTag = ReviewTag.ALL,
    val reviews: List<PatientReview> = ClinicRepository.initialReviews,
    val showReviewSheet: Boolean = false,
    // Appointment inquiry state
    val patientName: String = "",
    val phoneNumber: String = "",
    val selectedPatientGroup: PatientGroup = PatientGroup.FEMALE,
    val preferredDay: String = defaultPreferredDay(),
    val selectedSession: SessionSlot = SessionSlot.EVENING,
    val selectedTreatmentConcern: String = ClinicRepository.treatments.first().title,
    val notes: String = "",
    val formError: String? = null,
    val confirmation: AppointmentConfirmation? = null
)

private fun defaultPreferredDay(): String {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val match = ClinicRepository.weeklySchedule.firstOrNull {
        it.dayOfWeekCalendar == today && !it.isClosed
    }
    return match?.dayName ?: "Monday"
}

class ClinicViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClinicUiState())
    val uiState: StateFlow<ClinicUiState> = _uiState.asStateFlow()

    fun refreshClinicStatus() {
        _uiState.update { it.copy(clinicStatus = ClinicHoursEvaluator.evaluate()) }
    }

    fun selectTab(tab: ClinicTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun openTreatmentDetail(treatmentId: String, category: TreatmentCategory = TreatmentCategory.ALL) {
        _uiState.update {
            it.copy(
                selectedTab = ClinicTab.TREATMENTS,
                selectedTreatmentCategory = category,
                expandedTreatmentId = treatmentId
            )
        }
    }

    fun selectTreatmentCategory(category: TreatmentCategory) {
        _uiState.update { it.copy(selectedTreatmentCategory = category) }
    }

    fun toggleTreatmentExpanded(treatmentId: String) {
        _uiState.update { state ->
            state.copy(
                expandedTreatmentId = if (state.expandedTreatmentId == treatmentId) null else treatmentId
            )
        }
    }

    fun bookForTreatment(treatmentTitle: String) {
        _uiState.update {
            it.copy(
                selectedTab = ClinicTab.VISIT_BOOK,
                selectedTreatmentConcern = treatmentTitle,
                confirmation = null,
                formError = null
            )
        }
    }

    fun selectReviewTag(tag: ReviewTag) {
        _uiState.update { it.copy(selectedReviewTag = tag) }
    }

    fun setShowReviewSheet(show: Boolean) {
        _uiState.update { it.copy(showReviewSheet = show) }
    }

    fun addPatientReview(author: String, context: String, rating: Int, quote: String) {
        val cleanAuthor = author.trim().ifEmpty { "Verified Patient" }
        val cleanContext = context.trim().ifEmpty { "Clinic Visit • Bareilly" }
        val cleanQuote = quote.trim()
        if (cleanQuote.isEmpty()) return

        val newReview = PatientReview(
            id = "rev_${System.currentTimeMillis()}",
            author = cleanAuthor,
            patientContext = cleanContext,
            rating = rating.coerceIn(1, 5),
            quote = cleanQuote,
            tags = listOf(ReviewTag.WOMEN_KIDS, ReviewTag.HYGIENE),
            verifiedVisit = true
        )
        _uiState.update {
            it.copy(
                reviews = listOf(newReview) + it.reviews,
                selectedReviewTag = ReviewTag.ALL,
                showReviewSheet = false
            )
        }
    }

    fun updatePatientName(name: String) {
        _uiState.update { it.copy(patientName = name, formError = null) }
    }

    fun updatePhoneNumber(phone: String) {
        val digitsOnly = phone.filter { it.isDigit() || it == '+' || it == ' ' }.take(15)
        _uiState.update { it.copy(phoneNumber = digitsOnly, formError = null) }
    }

    fun selectPatientGroup(group: PatientGroup) {
        _uiState.update { it.copy(selectedPatientGroup = group) }
    }

    fun selectPreferredDay(day: String) {
        _uiState.update { it.copy(preferredDay = day) }
    }

    fun selectSessionSlot(slot: SessionSlot) {
        _uiState.update { it.copy(selectedSession = slot) }
    }

    fun selectTreatmentConcern(concern: String) {
        _uiState.update { it.copy(selectedTreatmentConcern = concern) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun submitAppointmentRequest() {
        val current = _uiState.value
        val trimmedName = current.patientName.trim()
        val digitsCount = current.phoneNumber.count { it.isDigit() }

        if (trimmedName.length < 2) {
            _uiState.update { it.copy(formError = "Please enter the patient's full name.") }
            return
        }
        if (digitsCount < 10) {
            _uiState.update { it.copy(formError = "Please enter a valid 10-digit mobile number.") }
            return
        }

        val refCode = "AMD-${Random.nextInt(1000, 9999)}"
        val confirmation = AppointmentConfirmation(
            referenceId = refCode,
            patientName = trimmedName,
            phoneNumber = current.phoneNumber.trim(),
            patientGroup = current.selectedPatientGroup,
            preferredDay = current.preferredDay,
            sessionSlot = current.selectedSession,
            treatmentConcern = current.selectedTreatmentConcern,
            notes = current.notes.trim()
        )
        _uiState.update {
            it.copy(
                confirmation = confirmation,
                formError = null
            )
        }
    }

    fun resetAppointmentForm() {
        _uiState.update {
            it.copy(
                confirmation = null,
                formError = null,
                notes = ""
            )
        }
    }
}
