package com.willklep.remindcurve

import java.util.Date
import java.util.concurrent.TimeUnit

data class Task(
    var id: String? = null,
    var topic: String = "",
    var className: String = "",
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var dates: List<Date> = emptyList()
) {
    fun generateDates(): List<Date> {
        val daysBetween = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time).toInt()
        val halfDaysBetween = daysBetween / 2
        val days = mutableListOf<Date>()

        for (i in 0..halfDaysBetween) {
            val date = Date(startDate.time + TimeUnit.DAYS.toMillis(i.toLong()))
            days.add(date)
        }

        for (i in 1..halfDaysBetween) {
            val date = Date(startDate.time + TimeUnit.DAYS.toMillis((halfDaysBetween + i).toLong()))
            days.add(date)
        }

        return days
    }
}