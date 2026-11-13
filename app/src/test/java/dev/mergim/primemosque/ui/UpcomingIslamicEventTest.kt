package dev.mergim.primemosque.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

/** Dates checked against the Umm al-Qura calendar and the BIK 2026 takvim. */
class UpcomingIslamicEventTest {

    private fun event(today: String, offset: Int = 0) =
        upcomingIslamicEvent(LocalDate.parse(today), offset)

    @Test
    fun ramadanStartFromHijriCalendar() {
        assertEquals(UpcomingEvent("ramadan_start", LocalDate.parse("2026-02-18")), event("2026-02-01"))
    }

    @Test
    fun correctionMatchesLocalMoonSighting() {
        // BIK began Ramadan 1447 on 19 February 2026, a day after Umm al-Qura.
        assertEquals(UpcomingEvent("ramadan_start", LocalDate.parse("2026-02-19")), event("2026-02-01", -1))
    }

    @Test
    fun eidsAndNewYearMatchTheTakvim() {
        assertEquals(UpcomingEvent("eid_al_fitr", LocalDate.parse("2026-03-20")), event("2026-03-16"))
        assertEquals(UpcomingEvent("eid_al_adha", LocalDate.parse("2026-05-27")), event("2026-05-01"))
        assertEquals(UpcomingEvent("islamic_new_year", LocalDate.parse("2026-06-16")), event("2026-06-01"))
    }

    @Test
    fun keepsWorkingAfterTheTakvimYear() {
        assertEquals(UpcomingEvent("ramadan_start", LocalDate.parse("2027-02-08")), event("2027-01-20"))
        assertEquals(UpcomingEvent("eid_al_adha", LocalDate.parse("2027-05-16")), event("2027-05-01"))
    }

    @Test
    fun theDayItselfStillCounts() {
        assertEquals(UpcomingEvent("eid_al_fitr", LocalDate.parse("2026-03-20")), event("2026-03-20"))
    }

    @Test
    fun nothingWhenNoEventWithinAMonth() {
        assertNull(event("2026-11-13"))
    }
}
