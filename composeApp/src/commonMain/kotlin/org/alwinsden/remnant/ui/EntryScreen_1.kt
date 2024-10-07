package org.alwinsden.remnant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.alwinsden.remnant.HomenajeFontFamily
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.JudsonFontFamily

@Composable
fun EntryScreen1() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF000000)
            ),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("In this age of AI and extremely fast\n")
                    append("paced world, we often overlook\n")
                    append("the values that makes us complete \n")
                    append("as ")
                    withStyle(style = SpanStyle(fontFamily = HomenajeFontFamily)) {
                        append("humans")
                    }
                    append("\n in the first place.")
                },
                textAlign = TextAlign.Center,
                color = Color(0xFFFFFFFF),
                fontFamily = JudsonFontFamily,
                fontSize = 22.sp
            )
        }
        Button(
            onClick = {
                //
            },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 180.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF000000)),
        ) {
            Text(
                text = "Next ->",
                color = Color(0xFFFFFFFF),
                fontFamily = InterFontFamily
            )
        }
    }
}