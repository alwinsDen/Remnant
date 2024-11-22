package org.alwinsden.remnant

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.TulipVertical

actual fun getPlatformName(): String {
    return "desktop"
}

@Preview
@Composable
actual fun EntryScreen2Source() {
    val nvvController = LocalNavController.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = "Why worry?\n" +
                    "What’s sad in\n" +
                    "pastures of this\n" +
                    "World?",
            textAlign = TextAlign.Center,
            fontFamily = JudsonFontFamily,
            fontSize = 35.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
        Text(
            text = "next ->",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 110.dp)
                .clickable {
                    nvvController.navigate(NavRouteClass.EntryScreen3.route)
                },
            color = Color(0xFF000000),
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Image(
                painter = painterResource(Res.drawable.TulipVertical),
                contentDescription = null,
                modifier =
                    Modifier
                        .aspectRatio(0.34065935f)
                        .offset(x = (-270).dp, y = 230.dp)
            )
        }

    }
}

data class Progress(
    val fraction: Float,
    val timeMillis: Long
)

actual val PowerButtonPadding = 15;
actual val mainScreenColumnWidth = 400.dp

@Preview
@Composable
fun test() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
//        MainScreen1()
    }
}

