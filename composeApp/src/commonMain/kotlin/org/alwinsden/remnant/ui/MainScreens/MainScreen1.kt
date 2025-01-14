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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.JudsonFontFamily
import org.alwinsden.remnant.PowerButtonPadding
import org.alwinsden.remnant.mainScreenColumnWidth
import org.alwinsden.remnant.ui.PopsUps.PersonalInfoPopup

@Composable
fun MainScreen1() {
    val optionEnterState = remember { mutableIntStateOf(-1) }
    val userBioSelection = remember { mutableMapOf<String, Int>() }
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
                    content = "Gender: not specified.",
                    onClick = {
                        optionEnterState.value = 0
                    }
                )
                ColoredBckBox(
                    bckColor = 0xffA9C1ED,
                    header = "Adapts advice for location context.",
                    content = "city: not selected.",
                    onClick = {}
                )
                ColoredBckBox(
                    bckColor = 0xffC88DE3,
                    header = "Tailors suggestions for age.",
                    content = "age: not specified.",
                    onClick = {}
                )
                ColoredBckBox(
                    bckColor = 0xffB56161,
                    header = "Helps us determine how busy you are.",
                    content = "specify work hours: 9am-6:30pm.",
                    onClick = {}
                )
                SpecialColoredBckBox(
                    bckColor = 0xff000000,
                    header = "Things we should about you?",
                    content = "Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? "
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
            })
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
fun SpecialColoredBckBox(bckColor: Long, header: String, content: String) {
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
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically,
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