package com.ufpb.getha.ui.aparelhos.manual

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.alexvasilkov.gestures.Settings
import com.alexvasilkov.gestures.views.GestureImageView
import com.ufpb.getha.R
import com.ufpb.getha.databinding.FragmentManualBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class ManualFragment : Fragment() {

    private var binding: FragmentManualBinding? = null
    private lateinit var pdfRenderer: PdfRenderer
    private var pagesLayout: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManualBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        pagesLayout = root.findViewById(R.id.pages_layout)

        openRenderer()
        showPages()

        return root
    }

    private fun openRenderer() {
        val file = File(requireActivity().filesDir, "lista.pdf")
        if (!file.exists()) {
            val asset: InputStream = requireActivity().assets.open("lista.pdf")
            val output: OutputStream = FileOutputStream(file)
            try {
                val buffer = ByteArray(2024)
                var size: Int
                while (asset.read(buffer).also { size = it } != -1) {
                    output.write(buffer, 0, size)
                }
                asset.close()
                output.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
    }

    private fun showPages() {
        val pageCount = pdfRenderer.pageCount
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val pageHeight = displayMetrics.heightPixels  // Adjust this if needed

        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            pageHeight
        )

        for (i in 0 until pageCount) {
            val page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            val imageView = GestureImageView(requireContext())
            imageView.layoutParams = layoutParams
            imageView.setImageBitmap(bitmap)

            imageView.controller.settings
                .setMaxZoom(10f)
                .setDoubleTapZoom(-1f)
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setRotationEnabled(false)
                .setRestrictRotation(false)
                .setOverscrollDistance(0f, 0f)
                .setOverzoomFactor(2f)
                .setFillViewport(false)
                .setFitMethod(Settings.Fit.INSIDE)
                .setGravity(Gravity.CENTER)

            pagesLayout?.addView(imageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            pdfRenderer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding = null
    }
}