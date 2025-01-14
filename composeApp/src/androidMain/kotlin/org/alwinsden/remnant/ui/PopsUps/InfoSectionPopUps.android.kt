package org.alwinsden.remnant.ui.PopsUps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.alwinsden.remnant.InterFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.util.Calendar

@Preview
@Composable
actual fun EnterCityNameDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (cityName: String) -> Unit
) {
    val expectedCityName = remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                OutlinedTextField(
                    value = expectedCityName.value,
                    onValueChange = { it ->
                        expectedCityName.value = it
                    },
                    label = {
                        Text(text = "City/District")
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xffffffff),
                        ),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Dismiss")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (expectedCityName.value.isNotEmpty()) {
                                onSaveData(expectedCityName.value)
                            }
                        },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@Composable
actual fun EnterAgeNumberDialog(
    onDismissRequest: () -> Unit,
    onSaveData: (ageNumber: Int) -> Unit
) {
    val expectedAgeValue = remember { mutableIntStateOf(18) }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                OutlinedTextField(
                    value = (expectedAgeValue.intValue).toString(),
                    onValueChange = { it ->
                        expectedAgeValue.intValue = it.toInt()
                    },
                    label = {
                        Text(text = "Your age. Must be above 18.")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xffffffff),
                        ),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Dismiss")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (expectedAgeValue.intValue > 17) {
                                onSaveData(expectedAgeValue.intValue)
                            }
                        },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TimePickerState(
    time: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePicker = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    val timePickerEnd = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY) + 1,
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(5)
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = 15.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Work start at",
                fontFamily = InterFontFamily,

                )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            TimeInput(
                state = timePicker
            )
            Text(
                text = "End at",
                fontFamily = InterFontFamily,

                )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            TimeInput(
                state = timePickerEnd,
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xffffffff),
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Dismiss")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        time(
                            timePicker.hour,
                            timePicker.minute,
                            timePickerEnd.hour,
                            timePickerEnd.minute
                        )
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}