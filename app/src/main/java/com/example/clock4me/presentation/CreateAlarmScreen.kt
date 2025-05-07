package com.example.clock4me.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.clock4Me.R
import com.example.clock4me.data.model.Alarm
import com.example.clock4me.util.checkDate
import com.example.clock4me.util.checkNumberPicker
import com.example.clock4me.util.components.CustomChip
import com.example.clock4me.util.components.NumberPicker
import com.example.clock4me.util.components.imePaddingIfTrue
import com.google.gson.Gson
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreateAlarmScreen(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    alarmActions: AlarmActions,
    navigateToAlarmsList: () -> Unit = {},
) {
    val cardContainerColor by animateColorAsState(targetValue = MaterialTheme.colorScheme.surfaceContainer)
    val isAlarmTitleFocused = rememberSaveable { mutableStateOf(false) }

    Surface(modifier = modifier, color = cardContainerColor) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val alarmPickerPaddingStart =
                if (maxWidth > 400.dp) {
                    60.dp
                } else {
                    35.dp
                }

            AlarmPicker(
                modifier = Modifier
                    .padding(top = maxHeight / 6, start = alarmPickerPaddingStart),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
                cardContainerColor = cardContainerColor,
                updateAlarmTitleFocused = { newValue ->
                    isAlarmTitleFocused.value = newValue
                },
            )

            CustomizeAlarmEvent(
                modifier = Modifier
                    .align(Center)
                    .imePaddingIfTrue(
                        condition = isAlarmTitleFocused.value,
                    )
                    .background(color = MaterialTheme.colorScheme.surface),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
                updateAlarmTitleFocused = { newValue ->
                    isAlarmTitleFocused.value = newValue
                },
            )
            Buttons(
                modifier = Modifier
                    .align(BottomCenter),
                navigateToAlarmsList = navigateToAlarmsList,
                save = { alarmActions.save() },
            )
        }
    }
}

@Composable
private fun CustomizeAlarmEvent(
    modifier: Modifier,
    alarmCreationState: Alarm,
    updateAlarmTitleFocused: (Boolean) -> Unit,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = .3f)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = alarmCreationState.description,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            WeekDays(
                modifier = Modifier.fillMaxWidth(),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = updateAlarmCreationState,
            )
            AlarmTitle(
                modifier = Modifier
                    .align(Start)
                    .padding(8.dp),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = updateAlarmCreationState,
                updateAlarmTitleFocused = updateAlarmTitleFocused,
            )
        }
    }
}

@Composable
private fun AlarmPicker(
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
    updateAlarmTitleFocused: (Boolean) -> Unit,
    cardContainerColor: androidx.compose.ui.graphics.Color,
) {
    val textStyle = MaterialTheme.typography.displaySmall

    var hours by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(alarmCreationState.hour))
    }
    var minutes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(alarmCreationState.minute))
    }

    LaunchedEffect(hours, minutes) {
        this.launch {
            if (alarmCreationState.description.any { char -> char.isDigit() }) {
                updateAlarmCreationState(
                    alarmCreationState.copy(description = alarmCreationState.checkDate()),
                )
            }
        }
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NumberPicker(
                modifier = Modifier.weight(1f),
                number = hours,
                timeUnit = "Hours",
                onNumberChange = { value ->
                    if (value.text.checkNumberPicker(maxNumber = 23)) {
                        hours = value
                        updateAlarmCreationState(alarmCreationState.copy(hour = hours.text))
                    }
                    updateAlarmTitleFocused(false)
                },
                textStyle = textStyle,
                backgroundColor = cardContainerColor,
            )

            Text(
                text = ":",
                style = textStyle,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(top = 20.dp),
            )

            NumberPicker(
                modifier = Modifier.weight(1f),
                number = minutes,
                timeUnit = "Minutes",
                textStyle = textStyle,
                backgroundColor = cardContainerColor,
                onNumberChange = { value ->
                    if (value.text.checkNumberPicker(maxNumber = 59)) {
                        minutes = value
                        updateAlarmCreationState(alarmCreationState.copy(minute = minutes.text))
                    }
                    updateAlarmTitleFocused(false)
                },
            )
        }
    }
}

@Composable
private fun WeekDays(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    val daysSelected = remember {
        mutableStateMapOf<String, Boolean>().apply {
            putAll(alarmCreationState.daysSelected)
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        daysSelected.forEach { (day, isSelected) ->
            CustomChip(
                isChecked = isSelected,
                text = day,
                onChecked = { isChecked ->
                    daysSelected[day] = isChecked
                    val activeDays = daysSelected.filterValues { it }.keys
                    val description = if (activeDays.isEmpty()) {
                        alarmCreationState.checkDate()
                    } else {
                        activeDays.joinToString(separator = " ")
                    }

                    updateAlarmCreationState(
                        alarmCreationState.copy(
                            description = description,
                            isRecurring = daysSelected.any { it.value },
                            daysSelectedJson = Gson().toJson(daysSelected),
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun AlarmTitle(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
    updateAlarmTitleFocused: (Boolean) -> Unit,
) {
    var title by remember { mutableStateOf(alarmCreationState.title) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        if (isFocused) {
            this.launch {
                updateAlarmTitleFocused(true)
            }
        }
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.75f),
            value = title,
            onValueChange = {
                title = it
                updateAlarmCreationState(alarmCreationState.copy(title = title))
            },
            label = { Text("Alarm name") },
            interactionSource = interactionSource,
        )
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    navigateToAlarmsList: () -> Unit,
    save: () -> Unit,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            TextButton(
                onClick = {
                    navigateToAlarmsList()
                },
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
            TextButton(
                onClick = {
                    save()
                    navigateToAlarmsList()
                },
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}