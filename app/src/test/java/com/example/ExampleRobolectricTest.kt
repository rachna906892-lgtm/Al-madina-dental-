package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ClinicHoursEvaluator
import com.example.ui.ClinicViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name matches Al Madina Dental`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Al Madina Dental", appName)
    }

    @Test
    fun `clinic hours evaluator detects Sunday closed and weekday sessions`() {
        val sunday = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
        }
        val sundayStatus = ClinicHoursEvaluator.evaluate(sunday)
        assertFalse(sundayStatus.isOpen)
        assertTrue(sundayStatus.headline.contains("Closed"))

        val mondayMorning = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 30)
        }
        assertTrue(ClinicHoursEvaluator.evaluate(mondayMorning).isOpen)

        val mondayBreak = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 0)
        }
        assertFalse(ClinicHoursEvaluator.evaluate(mondayBreak).isOpen)

        val mondayEvening = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
        }
        assertTrue(ClinicHoursEvaluator.evaluate(mondayEvening).isOpen)
    }

    @Test
    fun `appointment submission validates and generates confirmation pass`() {
        val vm = ClinicViewModel()
        vm.updatePatientName("Ayesha Khan")
        vm.updatePhoneNumber("9876543210")
        vm.submitAppointmentRequest()

        val state = vm.uiState.value
        assertNull(state.formError)
        assertNotNull(state.confirmation)
        assertEquals("Ayesha Khan", state.confirmation?.patientName)
    }

    @Test
    fun `services catalog covers required treatments Dr Saba expertise and modern equipment`() {
        val titles = com.example.data.ClinicRepository.treatments.map { it.title }
        assertTrue(titles.any { it.contains("Root Canal", ignoreCase = true) })
        assertTrue(titles.any { it.contains("Tooth Extraction", ignoreCase = true) })
        assertTrue(titles.any { it.contains("General Dentistry", ignoreCase = true) })
        assertTrue(titles.any { it.contains("Pediatric Dental Care", ignoreCase = true) })
        assertTrue(com.example.data.ClinicRepository.treatments.all {
            it.drSabaExpertise.isNotBlank() && it.equipmentUsed.isNotBlank()
        })
        assertTrue(com.example.data.ClinicRepository.modernEquipment.isNotEmpty())
    }

    @Test
    fun `contact info contains clinic phone number and Bareilly address`() {
        assertEquals("089370 79471", com.example.data.ClinicRepository.PHONE_DISPLAY)
        assertEquals("08937079471", com.example.data.ClinicRepository.PHONE_DIAL)
        assertTrue(com.example.data.ClinicRepository.FULL_ADDRESS.contains("Khushboo Enclave"))
        assertTrue(com.example.data.ClinicRepository.FULL_ADDRESS.contains("243006"))
    }
}
