package dev.mergim.primemosque.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HijriRemindersTest {

    private fun h(month: Int, day: Int) = HijriDate(day = day, month = month, year = 1448)

    private fun keys(today: HijriDate, tomorrow: HijriDate, afterAsr: Boolean = false, afterMaghrib: Boolean = false) =
        hijriNotices(today, tomorrow, afterAsr, afterMaghrib).map { it.key }

    @Test
    fun bajramDays() {
        assertEquals(BajramKind.FITR to 1, bajramDay(h(10, 1)))
        assertEquals(BajramKind.FITR to 3, bajramDay(h(10, 3)))
        assertNull(bajramDay(h(10, 4)))
        assertNull(bajramDay(h(12, 9)))
        assertEquals(BajramKind.ADHA to 1, bajramDay(h(12, 10)))
        assertEquals(BajramKind.ADHA to 4, bajramDay(h(12, 13)))
        assertNull(bajramDay(h(12, 14)))
    }

    @Test
    fun noVoluntaryFastOnBajramOrTeshrik() {
        assertFalse(voluntaryFastAllowed(h(10, 1)))
        assertTrue(voluntaryFastAllowed(h(10, 2)))
        assertTrue(voluntaryFastAllowed(h(12, 9)))
        (10..13).forEach { assertFalse("12/$it", voluntaryFastAllowed(h(12, it))) }
        assertTrue(voluntaryFastAllowed(h(12, 14)))
    }

    @Test
    fun tenDaysOfDhulHijjaAndArafah() {
        assertEquals(listOf(NoticeKey.DHUL_HIJJA_DAYS), keys(h(12, 1), h(12, 2)))
        assertEquals(listOf(NoticeKey.DHUL_HIJJA_DAYS), keys(h(12, 8), h(12, 9)))
        assertEquals(
            listOf(NoticeKey.DHUL_HIJJA_DAYS, NoticeKey.FAST_ARAFAH),
            keys(h(12, 8), h(12, 9), afterAsr = true),
        )
        assertEquals(listOf(NoticeKey.ARAFAH_DAY), keys(h(12, 9), h(12, 10), afterAsr = true))
        assertEquals(emptyList<NoticeKey>(), keys(h(12, 10), h(12, 11), afterAsr = true))
    }

    @Test
    fun tasuaAndAshuraEves() {
        assertEquals(listOf(NoticeKey.FAST_TASUA), keys(h(1, 8), h(1, 9), afterAsr = true))
        assertEquals(listOf(NoticeKey.FAST_ASHURA), keys(h(1, 9), h(1, 10), afterAsr = true))
        assertEquals(emptyList<NoticeKey>(), keys(h(1, 9), h(1, 10)))
    }

    @Test
    fun sixDaysOfShawwalStartAfterBajram() {
        assertEquals(emptyList<NoticeKey>(), keys(h(10, 1), h(10, 2)))
        assertEquals(listOf(NoticeKey.SHAWWAL_FAST), keys(h(10, 2), h(10, 3)))
        assertEquals(listOf(NoticeKey.SHAWWAL_FAST), keys(h(10, 29), h(11, 1)))
    }

    @Test
    fun lastTenNightsBeginAtMaghribOnThe20th() {
        assertEquals(emptyList<NoticeKey>(), keys(h(9, 20), h(9, 21), afterAsr = true))
        assertEquals(
            listOf(NoticeKey.LAST_TEN_NIGHTS),
            keys(h(9, 20), h(9, 21), afterAsr = true, afterMaghrib = true),
        )
        assertEquals(listOf(NoticeKey.LAST_TEN_NIGHTS), keys(h(9, 27), h(9, 28)))
    }
}
