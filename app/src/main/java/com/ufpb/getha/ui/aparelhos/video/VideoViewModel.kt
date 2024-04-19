@file:Suppress("DEPRECATION", "DEPRECATION", "DEPRECATION")

package com.ufpb.getha.ui.aparelhos.video

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.ufpb.getha.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator

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
    context: Context
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            androidx.media3.ui.PlayerView(context).apply {
                viewModel.initializeExoPlayer(context, uri)
                player = viewModel.exoPlayer.value
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
fun YourScreen(activity: ComponentActivity, viewModel: VideoViewModel) {
    VideoPlayer(
        modifier = Modifier.fillMaxSize(),
        viewModel = viewModel,
        uri = "android.resource://${activity.packageName}/${R.raw.aparelhosvideo}",
        context = activity
    )
}