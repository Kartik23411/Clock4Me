package com.example.clock4me.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.text.isDigitsOnly
import com.example.clock4me.data.model.Alarm
import com.example.clock4me.util.GlobalProperties.dateTimeFormatter
import java.time.LocalDateTime
import java.time.LocalTime

fun String?.parseInt(): Int {
    return if (this.isNullOrEmpty()) 0 else this.toInt()
}

fun String.checkNumberPicker(maxNumber: Int): Boolean {
    return this.length <= 2 && this.isDigitsOnly() && this.parseInt() <= maxNumber
}

fun Alarm.checkDate(): String {
    val currentTime = LocalDateTime.now()
    val alarmTime = LocalDateTime.of(
        currentTime.toLocalDate(),
        LocalTime.of(this.hour.parseInt(), this.minute.parseInt())
    )

    return when {
        currentTime.isBefore(alarmTime) -> "Today-${currentTime.format(dateTimeFormatter)}"
        else -> "Tomorrow-${alarmTime.plusDays(1).format(dateTimeFormatter)}"
    }
}

fun Class<out BroadcastReceiver>?.setIntentAction(
    actionName: String,
    requestCode: Int,
    context: Context,
): PendingIntent {
    val broadcastIntent =
        Intent(context, this).apply {
            action = actionName
        }
    return PendingIntent.getBroadcast(
        context,
        requestCode,
        broadcastIntent,
        GlobalProperties.pendingIntentFlags,
    )
}
