package com.ufpb.getha.ui.aparelhos.video

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

@Composable
fun VideoScreen(aparelhoId: String) {

    val context = LocalContext.current
    val activity = context as? Activity

    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.NetworkMediaItem(
                url = "http://192.168.15.12:8000/video?id=$aparelhoId",
            )
        ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = true,
        enablePip = false,
        handleAudioFocus = true,
        controllerConfig = VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = true,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = false,
            showNextTrackButton = false,
            showRepeatModeButton = false,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        ),
        volume = 1f,
        repeatMode = RepeatMode.NONE,
        modifier = Modifier
            .fillMaxSize(),
    )
}