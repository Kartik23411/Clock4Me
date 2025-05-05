package com.example.clock4me.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clock4me.presentation.AlarmViewModel
import com.example.clock4me.presentation.AlarmsListScreen
import com.example.clock4me.presentation.CreateAlarmScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val alarmViewModel: AlarmViewModel = viewModel()
    val alarmsListState by alarmViewModel.alarmsListState.observeAsState()
    val alarmCreationState = alarmViewModel.alarmCreationState

    NavHost(navController = navController, startDestination = Screen.AlarmsList.route) {
        composable(
            route = Screen.AlarmsList.route,
        ) {
            alarmsListState?.let {
                AlarmsListScreen(
                    modifier = modifier,
                    alarmActions = alarmViewModel,
                    alarmsListState = it,
                    navigateToCreateAlarm = { navController.navigate(Screen.CreateAlarm.route) },
                )
            }
        }
        composable(Screen.CreateAlarm.route) {
            CreateAlarmScreen(
                modifier = modifier,
                alarmActions = alarmViewModel,
                alarmCreationState = alarmCreationState,
                navigateToAlarmsList = { navController.navigate(Screen.AlarmsList.route) },
            )
        }
    }
}