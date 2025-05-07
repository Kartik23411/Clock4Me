package com.example.clock4me

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clock4Me.R
import com.example.clock4me.data.model.Alarm
import com.example.clock4me.navigation.Navigation
import com.example.clock4me.navigation.Screen
import com.example.clock4me.presentation.AlarmViewModel
import com.example.clock4me.ui.theme.Clock4MeTheme
import com.example.clock4me.util.components.CheckExactAlarmPermission
import com.example.clock4me.util.components.RequestNotificationPermission
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Clock4MeTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val sdkVersion = Build.VERSION.SDK_INT
                DisposableEffect(systemUiController, useDarkIcons) {
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons,
                    )
                    onDispose {}
                }
                if (sdkVersion == Build.VERSION_CODES.S || sdkVersion == Build.VERSION_CODES.S_V2) {
                    CheckExactAlarmPermission()
                } else if (sdkVersion >= Build.VERSION_CODES.TIRAMISU) {
                    RequestNotificationPermission()
                }
                ClockApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun ClockApp() {
    val navController = rememberNavController()
    var isFloatButtonVisible by rememberSaveable { (mutableStateOf(true)) }
    var isBottomBarVisible by rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val alarmViewModel: AlarmViewModel = viewModel()

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        this.launch {
            isFloatButtonVisible = when (navBackStackEntry?.destination?.route) {
                Screen.AlarmsList.route -> true
                else -> false
            }
            isBottomBarVisible = when (navBackStackEntry?.destination?.route) {
                Screen.CreateAlarm.route -> false
                else -> true
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFloatButtonVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                        testTag = "AddAlarm"
                    },
                    text = { Text(text = stringResource(id = R.string.add_alarm)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddAlarm,
                            contentDescription = null,
                        )
                    },
                    onClick = {
                        alarmViewModel.updateAlarmCreationState(Alarm())
                        navController.navigate(Screen.CreateAlarm.route)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                )
            }
        },
        content = {
            Navigation(navController = navController, modifier = Modifier.padding(it))
        },
    )
}