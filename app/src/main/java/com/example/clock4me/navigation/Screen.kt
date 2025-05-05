package com.example.clock4me.navigation

sealed class Screen(val route: String) {
    object AlarmsList : Screen("AlarmsList")
    object CreateAlarm : Screen("CreateAlarm")
}