package org.alwinsden.remnant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.android_dark_rd_4x

@Composable
actual fun GoogleLoginInteractible() {
    OutlinedButton(
        onClick = {
            /**/
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF000000)),
    ) {
        Image(
            painter = painterResource(Res.drawable.android_dark_rd_4x),
            contentDescription = null,
            modifier = Modifier
                .width(250.dp)
        )
    }
}