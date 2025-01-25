package org.alwinsden.remnant.ui.PopsUps

import androidx.compose.runtime.Composable

@Composable
actual fun EnterCityNameDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (cityName: String) -> Unit,
    defaultValue: String
) {
}

@Composable
actual fun EnterAgeNumberDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (ageNumber: Int) -> Unit,
    defaultValue: Int
) {
}

@Composable
actual fun TimePickerState(
    time: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDismissRequest: () -> Unit,
    defaultValue: MutableMap<String, Any>
) {
}

@Composable
actual fun UserDescription(
    onDismissRequest: () -> Unit,
    onSaveData: (userPrompt: String) -> Unit,
    defaultValue: String
) {
}