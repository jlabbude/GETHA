package com.ufpb.getha.ui.aparelhos.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

@Composable
fun VideoScreen(viewModel: VideoViewModel, activity: ComponentActivity) {
    YourScreen(activity = activity, viewModel = viewModel)
}

class VideoFragment : Fragment() {

    private val viewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            VideoScreen(viewModel = viewModel, activity = requireActivity())
        }
        return composeView
    }
}