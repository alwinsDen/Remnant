package org.alwinsden.remnant.ui.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.alwinsden.remnant.GenderMaps
import org.alwinsden.remnant.HTTP_CALL_CLIENT
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.JudsonFontFamily
import org.alwinsden.remnant.PowerButtonPadding
import org.alwinsden.remnant.api_data_class.UserBasicDetails
import org.alwinsden.remnant.mainScreenColumnWidth
import org.alwinsden.remnant.networking_utils.onError
import org.alwinsden.remnant.networking_utils.onSuccess
import org.alwinsden.remnant.ui.PopsUps.EnterAgeNumberDialog
import org.alwinsden.remnant.ui.PopsUps.EnterCityNameDialog
import org.alwinsden.remnant.ui.PopsUps.PersonalInfoPopup
import org.alwinsden.remnant.ui.PopsUps.TimePickerState
import org.alwinsden.remnant.ui.PopsUps.UserDescription

@Composable
fun MainScreen1() {
    val optionEnterState = remember { mutableIntStateOf(-1) }
    val changesSaved = remember { mutableStateOf(false) }
    val userBioSelection = remember {
        mutableMapOf<String, Any>(
            "gender" to 0,
            "city" to "not selected",
            "age" to 18,
            "startHour" to 9,
            "startMinute" to 0,
            "endHour" to 18,
            "endMinute" to 30,
            "userDescription" to "Say something about yourself. This information is crucial for our systems to optimize your Remnant experience. Thanks!"
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
            Modifier
                .padding(top = PowerButtonPadding.dp, start = 10.dp, end = 10.dp)
                .width(mainScreenColumnWidth)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Some info about you.",
                fontFamily = JudsonFontFamily,
                fontSize = 35.sp
            )
            Text(
                text = "This information is fed into our AI model to determine mood factors throughout day.",
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                color = Color(0xff595959)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .padding(top = 45.dp)
            ) {
                ColoredBckBox(
                    bckColor = 0xff76BF00,
                    header = "Personalize mental health guidance.",
                    content = "Gender: " + GenderMaps[userBioSelection["gender"]],
                    onClick = {
                        optionEnterState.value = 0
                    }
                )
                ColoredBckBox(
                    bckColor = 0xffA9C1ED,
                    header = "Adapts advice for location context.",
                    content = "city: " + userBioSelection["city"],
                    onClick = {
                        optionEnterState.value = 1
                    }
                )
                ColoredBckBox(
                    bckColor = 0xffC88DE3,
                    header = "Tailors suggestions for age.",
                    content = "age: " + userBioSelection["age"],
                    onClick = {
                        optionEnterState.value = 2
                    }
                )
                ColoredBckBox(
                    bckColor = 0xffB56161,
                    header = "Helps us determine how busy you are.",
                    content = "specify work hours: " + String.format(
                        "%02d",
                        userBioSelection["startHour"]
                    ) + ":" + String.format(
                        "%02d", userBioSelection["startMinute"]
                    ) + " - " + String.format(
                        "%02d",
                        userBioSelection["endHour"]
                    ) + ":" + String.format("%02d", userBioSelection["endMinute"]),
                    onClick = {
                        optionEnterState.value = 3
                    }
                )
                SpecialColoredBckBox(
                    bckColor = 0xff000000,
                    header = "Things we should about you?",
                    content = userBioSelection["userDescription"] as String,
                    onClick = {
                        optionEnterState.value = 4
                    }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (changesSaved.value) {
                    Image(
                        Lucide.BadgeCheck,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color(0xff7C7C7C))
                    )
                    Text(
                        text = "changes saved.",
                        fontFamily = InterFontFamily,
                        color = Color(0xff7C7C7C)
                    )
                }
            }
            Button(
                onClick = {
                    val userBasicStructData = UserBasicDetails(
                        gender = userBioSelection["gender"] as Int,
                        city = userBioSelection["city"] as String,
                        userAge = userBioSelection["age"] as Int,
                        userPrompt = userBioSelection["userDescription"] as String,
                        workingHrStart = userBioSelection["startHour"] as Int,
                        workingMinuteStart = userBioSelection["startMinute"] as Int,
                        workingHrEnd = userBioSelection["endHour"] as Int,
                        workingMinuteEnd = userBioSelection["endMinute"] as Int
                    )
                    //this here is the user details logic.
                    CoroutineScope(Dispatchers.IO).launch {
                        HTTP_CALL_CLIENT.userBasicDetailsRequest(data = userBasicStructData)
                            .onSuccess { it ->
                                println(it.message)
                                changesSaved.value = true
                            }
                            .onError {
                                println("failed to send user details")
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff599A85)
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "save ->",
                    color = Color(0xffffffff),
                    fontFamily = InterFontFamily
                )
            }
        }
    }
    when (optionEnterState.value) {
        0 -> {
            PersonalInfoPopup(onOuterClick = {
                optionEnterState.value = -1
            }, onSelectedOption = { it ->
                userBioSelection["gender"] = it
                optionEnterState.value = -1
            },
                defaultValue = userBioSelection["gender"] as Int
            )
        }

        1 -> {
            EnterCityNameDialog(onDismissRequest = {
                optionEnterState.value = -1
            }, onSaveData = { it ->
                userBioSelection["city"] = it
                optionEnterState.value = -1
            },
                defaultValue = userBioSelection["city"] as String
            )
        }

        2 -> {
            EnterAgeNumberDialog(
                onDismissRequest = {
                    optionEnterState.value = -1
                },
                onSaveData = { it ->
                    userBioSelection["age"] = it
                    optionEnterState.value = -1
                },
                defaultValue = userBioSelection["age"] as Int
            )
        }

        3 -> {
            TimePickerState(
                time = { startHour, startMinute, endHour, endMinute ->
                    userBioSelection["startHour"] = startHour
                    userBioSelection["startMinute"] = startMinute
                    userBioSelection["endHour"] = endHour
                    userBioSelection["endMinute"] = endMinute
                    optionEnterState.value = -1
                },
                onDismissRequest = {
                    optionEnterState.value = -1
                },
                defaultValue = userBioSelection
            )
        }

        4 -> {
            UserDescription(
                onDismissRequest = {
                    optionEnterState.value = -1
                },
                onSaveData = { it ->
                    userBioSelection["userDescription"] = it
                    optionEnterState.value = -1
                },
                defaultValue = userBioSelection["userDescription"] as String
            )
        }
    }
}

@Composable
private fun ColoredBckBox(bckColor: Long, header: String, content: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp)
                .fillMaxWidth(.98f)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(bckColor),
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xff000000),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(35.dp)
            ) {}
        }
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xffffffff),
                )
                .border(
                    width = 1.dp,
                    color = Color(0xff000000),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .fillMaxWidth(.95f)
                .height(35.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClick()
                    }
            ) {
                Column {
                    Text(
                        text = header,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = content,
                        fontFamily = InterFontFamily,
                        color = Color(0xff595959)
                    )
                }
                Image(
                    Lucide.ChevronRight,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun SpecialColoredBckBox(bckColor: Long, header: String, content: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.98f)
                .offset(
                    y = 10.dp
                )
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xff000000),
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xff000000),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    color = Color(0xffffffff),
                    modifier = Modifier
                        .padding(bottom = 5.dp, start = 10.dp),
                    fontFamily = InterFontFamily,
                    text = "*fed into our data model as a factors."
                )
            }
        }
        Column(
            modifier = Modifier
                .height(130.dp)
                .padding(start = 5.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xffffffff),
                )
                .border(
                    width = 1.dp,
                    color = Color(0xff000000),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .fillMaxWidth(.965f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = header,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(.90f),
                        text = content,
                        fontFamily = InterFontFamily,
                        color = Color(0xff595959),
                    )
                }
                Image(
                    Lucide.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        }
    }
}