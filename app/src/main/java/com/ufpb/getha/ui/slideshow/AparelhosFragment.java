package com.ufpb.getha.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ufpb.getha.R;
import com.ufpb.getha.databinding.FragmentAparelhosBinding;

public class AparelhosFragment extends Fragment {

    private FragmentAparelhosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        AparelhosViewModel aparelhosViewModel = new ViewModelProvider(this).get(AparelhosViewModel.class);

        binding = FragmentAparelhosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton imageButton = (ImageButton) root.findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.findViewById(R.id.button).setOnClickListener(v1 -> {

                    PopupMenu popup = new PopupMenu(getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());
                    popup.show();
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}