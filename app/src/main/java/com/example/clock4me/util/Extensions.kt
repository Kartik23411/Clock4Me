package com.example.clock4me.util

import com.example.clock4me.data.Alarm
import com.example.clock4me.util.GlobalProperties.dateTimeFormatter
import java.time.LocalDateTime
import java.time.LocalTime

fun String?.parseInt(): Int {
    return if (this.isNullOrEmpty()) 0 else this.toInt()
}

fun Alarm.checkDate(): String {
    val currentTime = LocalDateTime.now()
    val alarmTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(this.hour.parseInt(), this.minute.parseInt()))

    return when {
        currentTime.isBefore(alarmTime) -> "Today-${currentTime.format(dateTimeFormatter)}"
        else -> "Tomorrow-${alarmTime.plusDays(1).format(dateTimeFormatter)}"
    }
}