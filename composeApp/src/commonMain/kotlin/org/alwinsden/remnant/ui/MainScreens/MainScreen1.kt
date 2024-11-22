package org.alwinsden.remnant.ui.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MainScreen1() {
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
                    content = "Gender: not specified."
                )
                ColoredBckBox(
                    bckColor = 0xff76BF00,
                    header = "Personalize mental health guidance.",
                    content = "Gender: not specified."
                )
                ColoredBckBox(
                    bckColor = 0xff76BF00,
                    header = "Personalize mental health guidance.",
                    content = "Gender: not specified."
                )
            }
        }
    }
}

@Composable
private fun ColoredBckBox(bckColor: Long, header: String, content: String) {
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
                        color = Color(0xff76BF00),
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