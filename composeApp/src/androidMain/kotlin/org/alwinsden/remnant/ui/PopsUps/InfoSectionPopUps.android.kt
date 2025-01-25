package org.alwinsden.remnant.ui.PopsUps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Send
import org.alwinsden.remnant.InterFontFamily
import java.util.Calendar

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
                text = "Work starts at",
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

//user description preview
@Preview
@Composable
fun UserDescPreview() {
    UserDescription(
        onDismissRequest = {
            //
        },
        onSaveData = {
            //
        }
    )
}

@Composable
actual fun UserDescription(
    onDismissRequest: () -> Unit,
    onSaveData: (userPrompt: String) -> Unit
) {
    var userPrompt = remember { mutableStateOf("") }
    BottomControllerComponent(
        onOuterClick = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box() {
                TextField(
                    value = userPrompt.value,
                    placeholder = {
                        Text(
                            text = "I am the kind of person who...",
                            fontSize = 13.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(250.dp)
                        .padding(0.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color(0xffE1E1E1),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        userPrompt.value = it
                    },
                    shape = RoundedCornerShape(5)
                )
                Image(
                    Lucide.Send,
                    colorFilter = ColorFilter.tint(color = Color(0xff888888)),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onSaveData(userPrompt.value)
                        }
                )
            }
        }
    }
}