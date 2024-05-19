package com.ufpb.getha.ui.aparelhos.manual

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import java.io.File

@Composable
fun PdfPages(pages: List<Bitmap>) {
    val scale = remember { mutableFloatStateOf(1f) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale.floatValue = (scale.floatValue * zoom).coerceIn(1f, 3f)
                    offsetX.floatValue += pan.x
                    offsetY.floatValue += pan.y
                }
            }
            .graphicsLayer(
                scaleX = scale.floatValue,
                scaleY = scale.floatValue,
                translationX = offsetX.floatValue,
                translationY = offsetY.floatValue
            )
    ) {
        LazyColumn {
            items(pages) { page ->
                PdfPageImage(page)
            }
        }
    }
}
@Composable
fun PdfPageImage(bitmap: Bitmap) {
    Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
}

class ManualFragment : Fragment() {
    private lateinit var pdfRenderer: PdfRenderer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            val pages = remember { mutableStateOf(listOf<Bitmap>()) }
            LaunchedEffect(Unit) {
                // Initialize pdfRenderer here
                val inputStream = requireContext().assets.open("lista.pdf")
                val tempFile = File.createTempFile("tempPdf", ".pdf", requireContext().cacheDir).apply {
                    outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(fileDescriptor)
                pages.value = showPages()
            }
            PdfPages(pages.value)
        }
        return composeView
    }

    private fun showPages(): List<Bitmap> {
    val pageCount = pdfRenderer.pageCount
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels

    return List(pageCount) { i ->
        val page = pdfRenderer.openPage(i)
        val scaleFactor = screenWidth.toFloat() / page.width
        val bitmapHeight = (page.height * scaleFactor).toInt()
        val bitmap = Bitmap.createBitmap(screenWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        bitmap
        }
    }
}