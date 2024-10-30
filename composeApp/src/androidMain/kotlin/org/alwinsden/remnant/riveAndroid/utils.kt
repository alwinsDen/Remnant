package org.alwinsden.remnant.riveAndroid

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader
import app.rive.runtime.kotlin.core.Fit

@OptIn(ExperimentalAssetLoader::class)
@Composable
fun RiveAnimationComposable(@RawRes resId: Int, stateMachineName: String) {
    AndroidView(
        modifier = Modifier,
        factory = { context ->
            RiveAnimationView(context).also {
                it.setRiveResource(
                    resId = resId,
                    stateMachineName = stateMachineName,
                    alignment = app.rive.runtime.kotlin.core.Alignment.CENTER,
                    fit = Fit.CONTAIN,
                    autoplay = true
                )
            }
        },
        update = { view -> { view } },
    )
}