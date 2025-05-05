package com.example.clock4me.util

import android.app.PendingIntent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object GlobalProperties {

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE,MMMdd")
    val nextDay: LocalDateTime = LocalDateTime.now().plus(1, ChronoUnit.DAYS)

    const val pendingIntentFlags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
}