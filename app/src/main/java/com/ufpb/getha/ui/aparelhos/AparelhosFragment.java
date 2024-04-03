package com.ufpb.getha.ui.aparelhos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.customview.widget.Openable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.ufpb.getha.R;
import com.ufpb.getha.databinding.FragmentAparelhosBinding;

public class AparelhosFragment extends Fragment {

    private FragmentAparelhosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        AparelhosViewModel aparelhosViewModel = new ViewModelProvider(this).get(AparelhosViewModel.class);

        binding = FragmentAparelhosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton imageButton = root.findViewById(R.id.button);

        imageButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());
            popup.show();

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.setGraph(R.navigation.aparelhos_navigation);

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_manual) {
                    navController.navigate(R.id.nav_manual);
                    return true;
                }
                return false;
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}