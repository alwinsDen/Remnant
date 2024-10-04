package org.alwinsden.remnant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFF000000)
            )
    ) {
        Text(fontFamily = MatescFontFamily, fontSize = 64.sp, color = Color(0xFFFFFFFF), text = "Remnant")
        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(50),
        ) {
            Text(
                text = "Start your experience",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF505050),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 25.dp)
            )
        }
    }
}