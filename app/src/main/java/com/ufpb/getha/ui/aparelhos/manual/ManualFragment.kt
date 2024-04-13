package com.ufpb.getha.ui.aparelhos.manual

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    private var currentPage: PdfRenderer.Page? = null
    private var currentPageIndex: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManualBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()

        val nextButton: Button = root.findViewById(R.id.next_button)
        val previousButton: Button = root.findViewById(R.id.previous_button)

        nextButton.setOnClickListener {
            currentPageIndex++
            showPage()
        }

        previousButton.setOnClickListener {
            currentPageIndex--
            showPage()
        }

        openRenderer()
        showPage()

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

    private fun showPage() {

        val gestureImageView: GestureImageView  = binding!!.getRoot().findViewById(R.id.pdf_image)

        if (currentPage != null) {
            currentPage!!.close()
            gestureImageView.controller.resetState()
        }


        if (currentPageIndex < 0) {
            currentPageIndex = pdfRenderer.pageCount - 1
        }
        else if (currentPageIndex >= pdfRenderer.pageCount) {
            currentPageIndex = 0
        }

        currentPage = pdfRenderer.openPage(currentPageIndex)

        val bitmap: Bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height,
                Bitmap.Config.ARGB_8888)
        currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        gestureImageView.setImageBitmap(bitmap)

        gestureImageView.controller.settings
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
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            pdfRenderer.close()
            if (currentPage != null) {
                currentPage!!.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding = null
    }
}