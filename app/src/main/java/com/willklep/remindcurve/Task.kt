package com.willklep.remindcurve

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Task(
    var id: String? = null,
    var topic: String = "",
    var className: String = "",
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),
    var dates: List<LocalDate> = emptyList()
) {
    fun generateDates(): List<LocalDate> {
        val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
        val halfDaysBetween = daysBetween / 2
        val days = mutableListOf<LocalDate>()

        for (i in 0..halfDaysBetween) {
            days.add(startDate.plusDays(i))
        }

        for (i in 1..halfDaysBetween) {
            days.add(startDate.plusDays(halfDaysBetween + i))
        }

        return days
    }
}