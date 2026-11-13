package dev.mergim.primemosque.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

/**
 * The bundled takvim carries 2026's clock changes (29 March, 25 October);
 * other years change on other dates and must not be an hour out around them.
 */
class TakvimDstShiftTest {

    private val zone = ZoneId.of("Europe/Belgrade")

    private fun shift(date: String, takvimYear: Int = 2026): Int {
        val d = LocalDate.parse(date)
        return takvimDstShiftMinutes(d, takvimYear, d.dayOfMonth, zone)
    }

    @Test
    fun takvimYearItselfIsLeftAlone() {
        listOf("2026-01-15", "2026-03-28", "2026-03-29", "2026-07-01", "2026-10-24", "2026-10-25")
            .forEach { assertEquals(it, 0, shift(it)) }
    }

    @Test
    fun earlierSpringChangeMovesTimesForward() {
        // 2027 changes on 28 March, a day before the takvim's 29 March.
        assertEquals(60, shift("2027-03-28"))
        assertEquals(0, shift("2027-03-27"))
        assertEquals(0, shift("2027-03-29"))
    }

    @Test
    fun laterAutumnChangeKeepsSummerTime() {
        // 2027 stays on summer time until 31 October; the takvim switched
        // back on 25 October, so 25-30 October need the hour back.
        (25..30).forEach { day -> assertEquals("Oct $day", 60, shift("2027-10-%02d".format(day))) }
        assertEquals(0, shift("2027-10-31"))
        assertEquals(0, shift("2027-10-24"))
    }

    @Test
    fun laterSpringChangeMovesTimesBack() {
        // 2031 changes on 30 March, after the takvim's 29 March.
        assertEquals(-60, shift("2031-03-29"))
        assertEquals(0, shift("2031-03-30"))
    }

    @Test
    fun leapDayUsesTheEntryItFallsBackTo() {
        assertEquals(0, takvimDstShiftMinutes(LocalDate.parse("2028-02-29"), 2026, 28, zone))
    }

    @Test
    fun unknownTakvimYearChangesNothing() {
        assertEquals(0, shift("2027-03-28", takvimYear = 0))
    }
}
