package org.alwinsden.remnant

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import remnant.composeapp.generated.resources.*
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.judson_bold
import remnant.composeapp.generated.resources.judson_regular
import remnant.composeapp.generated.resources.matesc_regular

val MatescFontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.matesc_regular, FontWeight.Normal)
    )

val JudsonFontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.judson_regular, FontWeight.Normal),
        Font(Res.font.judson_bold, FontWeight.Bold),
        Font(Res.font.judson_italic, FontWeight.Normal, FontStyle.Italic)
    )

val UrbanistFontFamily : FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.urbanist_light, FontWeight.Light),
        Font(Res.font.urbanist_regular, FontWeight.Normal),
        Font(Res.font.urbanist_semibold, FontWeight.SemiBold),
        Font(Res.font.urbanist_medium, FontWeight.Medium),
        Font(Res.font.urbanist_bold, FontWeight.Bold),
        Font(Res.font.urbanist_italic, FontWeight.Normal, FontStyle.Italic)
    )