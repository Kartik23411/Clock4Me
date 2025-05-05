package com.example.clock4me.presentation

import com.example.clock4me.data.Alarm

interface AlarmActions {
    fun updateAlarmCreationState(alarm: Alarm) {}
    fun update(alarm: Alarm) {}
    fun remove(alarm: Alarm) {}
    fun save() {}
    fun clear() {}
}