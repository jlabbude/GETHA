package com.ufpb.getha.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ufpb.getha.databinding.FragmentCalculadoraBinding;

public class CalculadoraFragment extends Fragment {

    private FragmentCalculadoraBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CalculadoraViewModel calculadoraViewModel = new ViewModelProvider(this).get(CalculadoraViewModel.class);

        binding = FragmentCalculadoraBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        final TextView textView = binding.textGallery;

        calculadoraViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}