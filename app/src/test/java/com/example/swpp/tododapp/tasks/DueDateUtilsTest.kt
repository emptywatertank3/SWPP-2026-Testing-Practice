package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedTodayFutureAndWithoutDate_returnsCLEAR() {
        val today = LocalDate.of(2026, 10, 14)

        assertEquals("CLEAR", dueDateLabel(today, today, true))
        assertEquals("CLEAR", dueDateLabel(LocalDate.of(2026, 10, 16), today, true))
        assertEquals("CLEAR", dueDateLabel(null, today, true))
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmptyString() {
        assertEquals("", dueDateLabel(null, LocalDate.of(2026, 10, 14), false))
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        val today = LocalDate.of(2026, 10, 14)

        assertEquals("D-Day", dueDateLabel(today, today, false))
    }

    @Test
    fun dueDateLabel_dueIn30DaysAcrossMonth_returnsD30() {
        assertEquals(
            "D-30",
            dueDateLabel(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 1, 31), false)
        )
    }

    @Test
    fun dueDateLabel_overdueBy30DaysAcrossMonth_returnsDPlus30() {
        assertEquals(
            "D+30",
            dueDateLabel(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 3, 2), false)
        )
    }

    @Test
    fun dueDateLabel_yesterdayTodayAndTomorrow_returnsBoundaryLabels() {
        val today = LocalDate.of(2026, 10, 14)

        assertEquals("D+1", dueDateLabel(today.minusDays(1), today, false))
        assertEquals("D-Day", dueDateLabel(today, today, false))
        assertEquals("D-1", dueDateLabel(today.plusDays(1), today, false))
    }

    @Test
    fun dueDateLabel_crossesMonthAndYearBoundaries_countsCalendarDays() {
        assertEquals(
            "D-1",
            dueDateLabel(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 31), false)
        )
        assertEquals(
            "D-1",
            dueDateLabel(LocalDate.of(2027, 1, 1), LocalDate.of(2026, 12, 31), false)
        )
        assertEquals(
            "D-30",
            dueDateLabel(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 1, 31), false)
        )
    }

    @Test
    fun dueDateLabel_onAndAcrossLeapDay_countsCalendarDays() {
        assertEquals(
            "D-1",
            dueDateLabel(LocalDate.of(2028, 2, 29), LocalDate.of(2028, 2, 28), false)
        )
        assertEquals(
            "D-2",
            dueDateLabel(LocalDate.of(2028, 3, 1), LocalDate.of(2028, 2, 28), false)
        )
        assertEquals(
            "D-1",
            dueDateLabel(LocalDate.of(2028, 3, 1), LocalDate.of(2028, 2, 29), false)
        )
    }
}
