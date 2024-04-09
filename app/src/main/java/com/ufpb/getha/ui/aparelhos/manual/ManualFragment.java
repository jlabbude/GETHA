package com.ufpb.getha.ui.aparelhos.manual;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ufpb.getha.R;
import com.ufpb.getha.databinding.FragmentManualBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ManualFragment extends Fragment {

    private FragmentManualBinding binding;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private int currentPageIndex = 0;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentManualBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button nextButton = root.findViewById(R.id.next_button);
        Button previousButton = root.findViewById(R.id.previous_button);

        nextButton.setOnClickListener(v -> {
            currentPageIndex++;
            showPage(currentPageIndex);
        });

        previousButton.setOnClickListener(v -> {
            currentPageIndex--;
            showPage(currentPageIndex);
        });

        try {
            openRenderer();
            showPage(currentPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    private void openRenderer() throws IOException {
        File file = new File(requireActivity().getFilesDir(), "lista.pdf");
        if (!file.exists()) {
            InputStream asset = requireActivity().getAssets().open("lista.pdf");
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private void showPage(int index) {

        ImageView imageView = binding.getRoot().findViewById(R.id.pdf_image);


        if (currentPage != null) {
            currentPage.close();
            imageView.setScaleY(1);
            imageView.setScaleX(1);
        }

        if (currentPageIndex >= 0 && currentPageIndex < pdfRenderer.getPageCount()) {
            currentPage = pdfRenderer.openPage(index);
        } else {
            currentPage = pdfRenderer.openPage(0);
            currentPageIndex = 0;
        }
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            float focusX;
            float focusY;

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                // Calculate focus point coordinates when scaling begins
                focusX = detector.getFocusX();
                focusY = detector.getFocusY();
                return true;
            }

            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {

                scaleFactor *= detector.getScaleFactor();

                scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

                imageView.setPivotX(detector.getFocusX());
                imageView.setPivotY(detector.getFocusY());

                imageView.setScaleX(scaleFactor);
                imageView.setScaleY(scaleFactor);

                return true;
            }

        });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }

        });

        imageView.setImageBitmap(bitmap);
    }

    // ScaleListener class

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (currentPage != null) {
                currentPage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding = null;
    }
}