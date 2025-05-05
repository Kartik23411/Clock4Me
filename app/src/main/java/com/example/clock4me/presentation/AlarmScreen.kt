package com.example.clock4me.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AlarmOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.clock4me.R
import com.example.clock4me.data.Alarm
import com.example.clock4me.util.checkDate
import com.example.clock4me.util.components.ClockAppBar
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun AlarmsListScreen(
    modifier: Modifier = Modifier,
    alarmsListState: List<Alarm>,
    alarmActions: AlarmActions,
    navigateToCreateAlarm: () -> Unit = {},
) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize(),
            ) {
                AlarmsListAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    clear = { alarmActions.clear() },
                )
                AlarmsList(
                    alarmsListSate = alarmsListState,
                    alarmActions = alarmActions,
                    navigateToCreateAlarm = navigateToCreateAlarm,
                )
            }
        }
    }
}

@Composable
private fun AlarmsListAppBar(
    modifier: Modifier = Modifier,
    clear: () -> Unit,
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    ClockAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.alarm),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.clear_alarms)) },
                    onClick = {
                        clear()
                        showMenu = false
                    },
                )
            }
        },
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun AlarmsList(
    modifier: Modifier = Modifier,
    alarmActions: AlarmActions,
    alarmsListSate: List<Alarm>,
    navigateToCreateAlarm: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    BoxWithConstraints(modifier = modifier) {
        LazyColumn(
            state = scrollState,
        ) {
            items(items = alarmsListSate, key = { alarmItem -> alarmItem.id }) { item ->
                var isScheduled by rememberSaveable { mutableStateOf(item.isScheduled) }
                val removeAlarm = SwipeAction(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inversePrimary,
                        )
                    },
                    isUndo = false,
                    background = MaterialTheme.colorScheme.error,
                    onSwipe = { alarmActions.remove(alarm = item) },
                )
                SwipeableActionsBox(
                    endActions = listOf(removeAlarm),
                    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
                    swipeThreshold = maxWidth / 2,
                ) {
                    Alarm(
                        alarm = item,
                        onScheduledChange = {
                            isScheduled = it
                            val description = if (item.description.any { char -> char.isDigit() }) item.checkDate() else item.description
                            alarmActions.update(
                                item.copy(
                                    isScheduled = isScheduled,
                                    description = description,
                                ),
                            )
                        },
                        updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
                        navigateToCreateAlarm = navigateToCreateAlarm,
                    )
                }
            }
        }
    }
}

@Composable
private fun Alarm(
    alarm: Alarm,
    navigateToCreateAlarm: () -> Unit,
    onScheduledChange: (Boolean) -> Unit,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    val cardContainerColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.surfaceContainer)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        onClick = {
            navigateToCreateAlarm()
            updateAlarmCreationState(alarm)
        },
        ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = CenterVertically,
        ) {
            AlarmInfo(
                modifier = Modifier.weight(2f),
                time = "${alarm.hour}:${alarm.minute}",
                title = alarm.title,
                isScheduled = alarm.isScheduled,
            )

            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 5.dp),
                text = alarm.description.substringAfter("-"),
            )
            IconToggleButton(
                modifier = Modifier.weight(1f),
                checked = alarm.isScheduled,
                onCheckedChange = { onScheduledChange(it) },
            ) {
                if (alarm.isScheduled) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.AlarmOn,
                        contentDescription = "AlarmOn",
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Outlined.AlarmOff,
                        contentDescription = "AlarmOff",
                    )
                }
            }
        }
    }
}

@Composable
private fun AlarmInfo(
    modifier: Modifier = Modifier,
    time: String,
    title: String,
    isScheduled: Boolean,
) {
    val textColor by animateColorAsState(
        targetValue = if (isScheduled) {
            LocalContentColor.current
        } else {
            LocalContentColor.current.copy(
                alpha = 0.5f,
            )
        },
    )

    Column(modifier = modifier) {
        Text(
            text = time,
            style = MaterialTheme.typography.headlineLarge,
            color = textColor,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}