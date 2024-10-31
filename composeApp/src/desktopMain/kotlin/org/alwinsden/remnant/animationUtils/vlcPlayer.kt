package org.alwinsden.remnant.animationUtils

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.alwinsden.remnant.Progress
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.*

@Composable
fun VideoPlayer(
    url: String,
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)?
) = VideoPlayerImpl(
    seek = state.seek,
    modifier = modifier,
    url = url,
    onFinish = onFinish,
    progressState = state._progress
)

@Composable
fun rememberVideoStatePlayer(
    seek: Float = 0f
): VideoPlayerState = rememberSaveable(saver = VideoPlayerState.saver()) {
    VideoPlayerState(
        seek,
        progress = Progress(0f, 0)
    )
}

@Composable
private fun VideoPlayerImpl(
    progressState: MutableState<Progress>,
    onFinish: (() -> Unit)?,
    url: String,
    seek: Float,
    modifier: Modifier
) {
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    mediaPlayer.emitProgressTo(progressState)
    mediaPlayer.setupVideoFinishHandler(onFinish)
    val factory = remember { { mediaPlayerComponent } }
    LaunchedEffect(url) { mediaPlayer.media().play(url) }
    LaunchedEffect(seek) { mediaPlayer.controls().setPosition(seek.toDouble()) }
    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
    SwingPanel(
        factory = factory,
        background = Color.Transparent,
        modifier = modifier
    )
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}

@Composable
private fun MediaPlayer.setupVideoFinishHandler(onFinish: (() -> Unit)?) {
    DisposableEffect(onFinish) {
        val listener = object : MediaPlayerEventAdapter() {
            override fun finished(mediaPlayer: MediaPlayer) {
                onFinish?.invoke()
                mediaPlayer.submit { mediaPlayer.controls().play() }
            }
        }
        events().addMediaPlayerEventListener(listener)
        onDispose { events().removeMediaPlayerEventListener(listener) }
    }
}

@Composable
private fun MediaPlayer.emitProgressTo(state: MutableState<Progress>) {
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            val fraction = status().position().toFloat()
            val time = status().time()
            state.value = Progress(fraction, time)
            delay(50)
        }
    }
}

private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("media player not supported.")
}

private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

class VideoPlayerState(
    seek: Float = 0f,
    progress: Progress
) {
    var seek by mutableStateOf(seek)
    internal val _progress = mutableStateOf(progress)
    val progress: State<Progress> = _progress

    companion object {
        fun saver() = listSaver<VideoPlayerState, Any>(
            save = {
                listOf(
                    it.seek,
                    it.progress.value
                )
            },
            restore = {
                VideoPlayerState(
                    seek = it[0] as Float,
                    progress = it[1] as Progress
                )
            }
        )
    }
}