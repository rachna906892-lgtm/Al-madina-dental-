package com.example.data

import java.util.Calendar

enum class ClinicTab(val route: String, val label: String) {
    HOME("home", "Home"),
    TREATMENTS("services", "Services"),
    DOCTOR_REVIEWS("doctor_reviews", "Dr. Saba"),
    VISIT_BOOK("visit_book", "Visit & Book")
}

enum class TreatmentCategory(val label: String) {
    ALL("All Services"),
    PAINLESS("RCT & Extraction"),
    GENERAL("General Dentistry"),
    PEDIATRIC_WOMEN("Pediatric & Women"),
    RESTORATIVE("Restorative")
}

data class TreatmentItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: TreatmentCategory,
    val badge: String,
    val description: String,
    val drSabaExpertise: String,
    val equipmentUsed: String,
    val comfortHighlights: List<String>,
    val durationEstimate: String,
    val feeNote: String
)

data class ModernEquipmentItem(
    val id: String,
    val name: String,
    val purpose: String
)

data class HygienePillar(
    val id: String,
    val title: String,
    val detail: String
)

enum class ReviewTag(val label: String) {
    ALL("All Reviews"),
    PAINLESS("Painless RCT & Extraction"),
    WOMEN_KIDS("Women & Children"),
    HYGIENE("Hygiene & Sterilization"),
    REASONABLE("Reasonable Charges")
}

data class PatientReview(
    val id: String,
    val author: String,
    val patientContext: String,
    val rating: Int,
    val quote: String,
    val tags: List<ReviewTag>,
    val verifiedVisit: Boolean = true
)

data class ClinicDayHours(
    val dayOfWeekCalendar: Int,
    val dayName: String,
    val isClosed: Boolean,
    val morningSlot: String,
    val eveningSlot: String
)

data class ClinicStatus(
    val isOpen: Boolean,
    val headline: String,
    val subtext: String,
    val currentDayName: String
)

enum class PatientGroup(val label: String) {
    FEMALE("Adult Female"),
    CHILD("Child / Pediatric"),
    MALE("Adult Male"),
    SENIOR("Senior Citizen")
}

enum class SessionSlot(val label: String, val timeRange: String) {
    MORNING("Morning Session", "11:00 AM – 2:00 PM"),
    EVENING("Evening Session", "5:00 PM – 10:00 PM")
}

data class AppointmentConfirmation(
    val referenceId: String,
    val patientName: String,
    val phoneNumber: String,
    val patientGroup: PatientGroup,
    val preferredDay: String,
    val sessionSlot: SessionSlot,
    val treatmentConcern: String,
    val notes: String
)

object ClinicHoursEvaluator {
    fun evaluate(calendar: Calendar = Calendar.getInstance()): ClinicStatus {
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val totalMinutes = hour * 60 + minute
        val dayName = when (day) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Today"
        }

        if (day == Calendar.SUNDAY) {
            return ClinicStatus(
                isOpen = false,
                headline = "Closed Today (Sunday)",
                subtext = "Opens Monday at 11:00 AM",
                currentDayName = dayName
            )
        }

        val morningStart = 11 * 60
        val morningEnd = 14 * 60
        val eveningStart = 17 * 60
        val eveningEnd = 22 * 60

        return when {
            totalMinutes in morningStart until morningEnd -> ClinicStatus(
                isOpen = true,
                headline = "Open Now",
                subtext = "Morning Session until 2:00 PM",
                currentDayName = dayName
            )
            totalMinutes in morningEnd until eveningStart -> ClinicStatus(
                isOpen = false,
                headline = "Afternoon Break",
                subtext = "Evening Session opens at 5:00 PM",
                currentDayName = dayName
            )
            totalMinutes in eveningStart until eveningEnd -> ClinicStatus(
                isOpen = true,
                headline = "Open Now",
                subtext = "Evening Session until 10:00 PM",
                currentDayName = dayName
            )
            totalMinutes < morningStart -> ClinicStatus(
                isOpen = false,
                headline = "Opens at 11:00 AM",
                subtext = "Morning 11:00 AM – 2:00 PM • Evening 5:00 PM – 10:00 PM",
                currentDayName = dayName
            )
            else -> {
                val nextOpen = if (day == Calendar.SATURDAY) "Monday at 11:00 AM" else "Tomorrow at 11:00 AM"
                ClinicStatus(
                    isOpen = false,
                    headline = "Closed for Today",
                    subtext = "Opens $nextOpen",
                    currentDayName = dayName
                )
            }
        }
    }
}

object ClinicRepository {
    const val CLINIC_NAME = "Al Madina Dental Clinic"
    const val DOCTOR_NAME = "Dr. Saba"
    const val PHONE_DISPLAY = "089370 79471"
    const val PHONE_DIAL = "08937079471"
    const val FULL_ADDRESS =
        "Main market, Jhadhjhooda Mazaar, Faiq Enclave Main Rd, Khushboo Enclave, Bareilly, Uttar Pradesh 243006"
    const val SHORT_ADDRESS = "Faiq Enclave Main Rd, Khushboo Enclave, Bareilly"
    const val LANDMARK_NOTE = "Main Market, near Jhadhjhooda Mazaar • Bareilly 243006"

    val weeklySchedule: List<ClinicDayHours> = listOf(
        ClinicDayHours(Calendar.MONDAY, "Monday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.TUESDAY, "Tuesday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.WEDNESDAY, "Wednesday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.THURSDAY, "Thursday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.FRIDAY, "Friday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.SATURDAY, "Saturday", false, "11:00 AM – 2:00 PM", "5:00 PM – 10:00 PM"),
        ClinicDayHours(Calendar.SUNDAY, "Sunday", true, "Closed", "Closed")
    )

    val openDaysList: List<String> = weeklySchedule.filter { !it.isClosed }.map { it.dayName }

    val treatments: List<TreatmentItem> = listOf(
        TreatmentItem(
            id = "rct",
            title = "Painless Root Canal Treatment (RCT)",
            subtitle = "Save infected or aching natural teeth comfortably",
            category = TreatmentCategory.PAINLESS,
            badge = "Dr. Saba's Signature",
            description = "Root canal therapy relieves severe toothache and preserves your natural tooth without discomfort. Every canal is cleaned, shaped, and sealed with strict infection control.",
            drSabaExpertise = "Dr. Saba is widely praised for her calm, painless RCT technique—listening carefully, explaining each sitting, and working with highly skilled procedural hands.",
            equipmentUsed = "Modern rotary endodontic handpiece, precision apex locator & sterile sealed endo files",
            comfortHighlights = listOf(
                "Careful local numbing before starting so you feel zero sharp pain",
                "Step-by-step explanation during every sitting",
                "Strict sterile pouch instruments & infection-free sealing"
            ),
            durationEstimate = "30–45 mins per sitting",
            feeNote = "Reasonable & transparent charges explained upfront"
        ),
        TreatmentItem(
            id = "extraction",
            title = "Painless Tooth Extraction",
            subtitle = "Gentle, atraumatic removal for decayed & wisdom teeth",
            category = TreatmentCategory.PAINLESS,
            badge = "Painless Expertise",
            description = "When a damaged, broken, or impacted tooth cannot be saved, we ensure a smooth, anxiety-free extraction with gentle tissue handling and clear recovery guidance.",
            drSabaExpertise = "Patients consistently highlight Dr. Saba's expertise in painless tooth extraction—checking numbness patiently and performing smooth removals with minimal trauma.",
            equipmentUsed = "Atraumatic surgical elevators, precision extraction forceps & autoclave-sterilized kits",
            comfortHighlights = listOf(
                "Double-checked anesthesia comfort before extraction",
                "Clean, hygienic clinical setup with protective barriers",
                "Clear home-care instructions for rapid, comfortable healing"
            ),
            durationEstimate = "20–35 mins",
            feeNote = "Affordable extraction fees with no hidden charges"
        ),
        TreatmentItem(
            id = "general_dentistry",
            title = "General Dentistry & Preventive Checkups",
            subtitle = "Complete oral exams, ultrasonic scaling & gum care",
            category = TreatmentCategory.GENERAL,
            badge = "General Dentistry",
            description = "Routine dental checkups, cavity detection, ultrasonic tartar removal, stain polishing, and gum care to keep your teeth strong and prevent painful emergencies.",
            drSabaExpertise = "Dr. Saba listens to every symptom unhurriedly, gives honest conservative advice, and prioritizes saving natural teeth whenever possible.",
            equipmentUsed = "Piezo ultrasonic scaler, high-illumination dental chair unit & intraoral mirrors",
            comfortHighlights = listOf(
                "Gentle ultrasonic cleaning without enamel scratching",
                "Complete tooth & gum assessment with clear explanation",
                "Practical daily hygiene advice tailored to your habits"
            ),
            durationEstimate = "20–30 mins",
            feeNote = "Reasonable consultation & cleaning charges"
        ),
        TreatmentItem(
            id = "pediatric_care",
            title = "Pediatric Dental Care (Children)",
            subtitle = "Gentle, fear-free dentistry for kids & growing smiles",
            category = TreatmentCategory.PEDIATRIC_WOMEN,
            badge = "Child-Friendly",
            description = "Dedicated care for milk teeth, early cavity prevention, gentle child fillings, painless baby-tooth extractions, and habit guidance in a warm, welcoming atmosphere.",
            drSabaExpertise = "Highly recommended by Bareilly families for children—Dr. Saba's polite, reassuring voice helps kids feel safe and cooperative without dental fear.",
            equipmentUsed = "Pediatric-sized dental instruments, gentle slow-speed handpieces & tooth-colored sealants",
            comfortHighlights = listOf(
                "Patient, friendly 'tell-show-do' approach for anxious kids",
                "Protective preventive care for milk & permanent teeth",
                "Parents guided clearly at every step of the visit"
            ),
            durationEstimate = "20–30 mins",
            feeNote = "Family-friendly & reasonable pediatric charges"
        ),
        TreatmentItem(
            id = "womens_care",
            title = "Women's Comfort-First Dental Care",
            subtitle = "Trusted, polite female dentist in a respectful setting",
            category = TreatmentCategory.PEDIATRIC_WOMEN,
            badge = "Recommended for Women",
            description = "Thoughtful, private, and comfortable dental consultations and treatments designed so female patients feel completely at ease discussing any dental concern.",
            drSabaExpertise = "As a qualified, gentle, and polite female dentist, Dr. Saba is the preferred choice for women across Khushboo Enclave and nearby areas.",
            equipmentUsed = "Modern ergonomic dental chair, hygienic disposable barriers & sterile sealed kits",
            comfortHighlights = listOf(
                "Unhurried listening & respectful clinical privacy",
                "Safe guidance for pregnancy-safe dental care & routine checkups",
                "Calm, spotless clinic environment"
            ),
            durationEstimate = "20–30 mins",
            feeNote = "Transparent, reasonable treatment plans"
        ),
        TreatmentItem(
            id = "restoration",
            title = "Tooth-Colored Fillings & Dental Crowns",
            subtitle = "Natural aesthetic restoration for decayed or post-RCT teeth",
            category = TreatmentCategory.RESTORATIVE,
            badge = "Modern Restorations",
            description = "Seamless composite fillings for cavities and durable dental crowns (caps) that protect RCT-treated teeth and restore natural biting strength and smile aesthetics.",
            drSabaExpertise = "Dr. Saba matches tooth shades carefully and verifies bite comfort with great attention to detail for long-lasting results.",
            equipmentUsed = "High-intensity LED curing light, micromotor handpiece & shade-matched composite resins",
            comfortHighlights = listOf(
                "Natural tooth-colored fillings that blend invisibly",
                "Protective zirconia, porcelain, or metal-ceramic crown options",
                "Detailed bite check for smooth chewing comfort"
            ),
            durationEstimate = "25–40 mins",
            feeNote = "Multiple durable material choices at fair prices"
        )
    )

    val modernEquipment: List<ModernEquipmentItem> = listOf(
        ModernEquipmentItem(
            id = "eq_endo",
            name = "Rotary Endodontics & Apex Locator",
            purpose = "Enables faster, quieter, and highly accurate painless Root Canal Treatments."
        ),
        ModernEquipmentItem(
            id = "eq_scaler",
            name = "Ultrasonic Piezo Scaler",
            purpose = "Gently lifts plaque and tartar with cool water irrigation for comfortable cleanings."
        ),
        ModernEquipmentItem(
            id = "eq_autoclave",
            name = "Autoclave & UV Sterilizer Cabinet",
            purpose = "Hospital-grade steam sterilization and sealed storage for 100% infection control."
        ),
        ModernEquipmentItem(
            id = "eq_chair",
            name = "Ergonomic Dental Chair & LED Curing",
            purpose = "Smooth patient positioning, shadow-free clinical lighting, and instant composite curing."
        )
    )

    val hygienePillars: List<HygienePillar> = listOf(
        HygienePillar(
            id = "autoclave",
            title = "Hospital-Grade Sterilization",
            detail = "Every reusable instrument goes through ultrasonic cleaning and sealed autoclave steam sterilization."
        ),
        HygienePillar(
            id = "sealed_pouches",
            title = "Sealed Instrument Pouches",
            detail = "Sterilized pouches are opened fresh right in front of each patient for complete peace of mind."
        ),
        HygienePillar(
            id = "ppe",
            title = "Strict Protective Measures",
            detail = "Fresh disposable gloves, masks, suction tips, and surface disinfection protocols between every visit."
        ),
        HygienePillar(
            id = "modern_eq",
            title = "Modern Clinical Equipment",
            detail = "Well-maintained dental chair, modern rotary endodontics, and clean lighting for safe, precise care."
        )
    )

    val initialReviews: List<PatientReview> = listOf(
        PatientReview(
            id = "rev_1",
            author = "Verified Local Patient",
            patientContext = "Family & Women's Care • Bareilly",
            rating = 5,
            quote = "Al Madina Dental Clinic is no doubt best dental care in area with reasonable charges. Facility is hygienic and equipped with modern equipments. Doctor is very experienced and polite. Gentle and polite female dentist whom we can trust. Clean and hygienic setup. She is well experienced and has expertise in painless tooth extraction and RCT. I will give this clinic a 5 star and highly recommend it especially for females and children.",
            tags = listOf(ReviewTag.PAINLESS, ReviewTag.WOMEN_KIDS, ReviewTag.HYGIENE, ReviewTag.REASONABLE)
        ),
        PatientReview(
            id = "rev_2",
            author = "Satisfied RCT & Dental Patient",
            patientContext = "Procedure & Hygiene Review • Bareilly",
            rating = 5,
            quote = "Excellent dental care by qualified and experienced dentist with highly skilled procedural hands, also best protective measures for infection control. Dr Saba is an excellent dentist who listens to the patient carefully and explains each procedure and works on patients with great attention. I am fully satisfied with her treatment.",
            tags = listOf(ReviewTag.PAINLESS, ReviewTag.HYGIENE)
        ),
        PatientReview(
            id = "rev_3",
            author = "Khushboo Enclave Resident",
            patientContext = "Painless Extraction Visit",
            rating = 5,
            quote = "Very calm and clean environment right in Khushboo Enclave. Dr. Saba explained every step of my tooth extraction beforehand so I felt zero fear, and the charges were very reasonable.",
            tags = listOf(ReviewTag.PAINLESS, ReviewTag.REASONABLE)
        )
    )
}
