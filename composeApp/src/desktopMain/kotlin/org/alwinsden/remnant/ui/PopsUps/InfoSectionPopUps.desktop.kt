package org.alwinsden.remnant.ui.PopsUps

import androidx.compose.runtime.Composable

@Composable
actual fun EnterCityNameDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (cityName: String) -> Unit
) {
}

@Composable
actual fun EnterAgeNumberDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (ageNumber: Int) -> Unit
) {
}

@Composable
actual fun TimePickerState(
    time: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
}