package org.alwinsden.remnant.ui.DemoScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alwinsden.remnant.HTTP_CALL_CLIENT
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.JudsonFontFamily
import org.alwinsden.remnant.Screen3PngCoordinates
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import remnant.composeapp.generated.resources.*

//this is the suspended fade-in-out animation for the loader.
suspend fun animationFadeInFadeOut(onUpdate: (Float) -> Unit) {
    while (true) {
        repeat(10) {
            onUpdate(0.1f * (it + 1))
            delay(50)
        }
        delay(300)
        repeat(10) {
            onUpdate(1f - 0.1f * (it + 1))
            delay(50)
        }
        delay(300)
    }
}

@Preview
@Composable
fun EntryScreen3() {
    var runLoadingOpacity by remember { mutableStateOf(false) }
    var opacityValue by remember { mutableStateOf(0f) }
    LaunchedEffect(runLoadingOpacity) {
        if (runLoadingOpacity) {
            animationFadeInFadeOut {
                opacityValue = it
            }
        } else {
            opacityValue = 0f
        }
    }
    val resourceListing = listOf<DrawableResource>(
        Res.drawable.Frame_2,
        Res.drawable.Frame_3,
        Res.drawable.Frame_5,
        Res.drawable.Frame_6,
        Res.drawable.Frame_9,
        Res.drawable.Frame_11,
        Res.drawable.Frame_13,
        Res.drawable.Frame_17,
        Res.drawable.Frame_18,
        Res.drawable.Frame_20,
        Res.drawable.Frame_21,
        Res.drawable.Frame_22,
        Res.drawable.Frame_23,
        Res.drawable.Frame_24,
        Res.drawable.Frame_25,
        Res.drawable.Frame_26,
        Res.drawable.Frame_27,
        Res.drawable.Frame_31,
        Res.drawable.Frame_33,
    )
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffffffff))
    ) {
        val maxHeight = maxHeight
        val maxWidth = maxWidth
        for ((inx, ssl) in resourceListing.withIndex()) {
            Image(
                painter = painterResource(ssl),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .offset(
                        x = Screen3PngCoordinates[inx].first.dp,
                        y = Screen3PngCoordinates[inx].second.dp
                    )
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = "Let’s \n" +
                        "explore the \n" +
                        "philosophical\n" +
                        "aspect of human\n" +
                        "existence. ",
                fontSize = 28.sp,
                color = Color(0xff000000),
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontFamily = JudsonFontFamily,
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp),
            ) {
                Text(
                    text = "explore ->",
                    color = Color(0xff000000),
                    fontFamily = InterFontFamily,
                    fontSize = (16.5).sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            runLoadingOpacity = true
                            CoroutineScope(Dispatchers.IO).launch {
                                HTTP_CALL_CLIENT.demoCompletedPOSTRequest()
                                runLoadingOpacity = false
                            }
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                )
                Text(
                    text = "syncing...",
                    color = Color(0xff000000).copy(alpha = opacityValue),
                    fontFamily = InterFontFamily,
                    fontSize = (12).sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(
                            y = 20.dp
                        )
                )
            }
        }
    }
}