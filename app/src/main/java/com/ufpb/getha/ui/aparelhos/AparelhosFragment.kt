package com.ufpb.getha.ui.aparelhos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.ufpb.getha.R
import com.ufpb.getha.databinding.FragmentAparelhosBinding

class AparelhosFragment : Fragment() {
    private var binding: FragmentAparelhosBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val aparelhosViewModel = ViewModelProvider(this)[AparelhosViewModel::class.java]
        binding = FragmentAparelhosBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()
        val imageButton = root.findViewById<ImageButton>(R.id.button)

        imageButton.setImageBitmap(aparelhosViewModel.getBitmap(requireContext()))

        imageButton.setOnClickListener { v: View? ->
            val popup = PopupMenu(context, v)
            popup.menuInflater.inflate(R.menu.popupmenu_aparelhos, popup.menu)
            popup.show()
            val navController =
                findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
            navController.setGraph(R.navigation.mobile_navigation)
            popup.setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.action_manual) {
                    navController.navigate(R.id.nav_manual)
                    return@setOnMenuItemClickListener true
                }
                else if (item.itemId == R.id.action_video) {
                    navController.navigate(R.id.nav_video)
                    return@setOnMenuItemClickListener true
                }
                false
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}