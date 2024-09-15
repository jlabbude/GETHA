@file:Suppress("DEPRECATION", "DEPRECATION", "DEPRECATION")

package com.ufpb.getha.ui.aparelhos.video

import android.content.Context
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer

@Suppress("DEPRECATION")
class VideoViewModel : ViewModel() {
    private val _exoPlayer = mutableStateOf<Player?>(null)
    val exoPlayer: State<Player?> = _exoPlayer

    private val _isBuffering = mutableStateOf(true)
    val isBuffering: State<Boolean> = _isBuffering

    @OptIn(UnstableApi::class)
    fun initializeExoPlayer(context: Context, uri: String) {
        val player = SimpleExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            addListener(object : Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    _isBuffering.value = isLoading
                }
            })
        }
        _exoPlayer.value = player
    }

    override fun onCleared() {
        _exoPlayer.value?.release()
        super.onCleared()
    }
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel,
    uri: String,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            androidx.media3.ui.PlayerView(context).apply {
                viewModel.initializeExoPlayer(context, uri)
                player = viewModel.exoPlayer.value

                findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_rew)?.visibility = View.GONE
                findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_ffwd)?.visibility = View.GONE
                findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_prev)?.visibility = View.GONE
                findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_next)?.visibility = View.GONE
                findViewById<View>(com.google.android.exoplayer2.ui.R.id.exo_settings)?.visibility = View.GONE

            }
        },
        update = { view ->
            view.player = viewModel.exoPlayer.value
        }
    )

    if (viewModel.isBuffering.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.exoPlayer.value?.release()
        }
    }
}

@Composable
fun YourScreen(viewModel: VideoViewModel) {
    VideoPlayer(
        modifier = Modifier.fillMaxSize(),
        viewModel = viewModel,
        uri = "http://192.168.15.11:8000/video?id=${Id.aparelhoId}",
    )
}