package com.ufpb.getha.ui.aparelhos

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.google.android.flexbox.FlexboxLayout
import com.ufpb.getha.R
import com.ufpb.getha.utils.ServidorErrorPopup
import com.ufpb.getha.databinding.FragmentAparelhosBinding
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.launch

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
        val buttonContainer = root.findViewById<FlexboxLayout>(R.id.button_container)
        val navController = findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
        val imageLinkedMap = LinkedHashMap<Int, Bitmap>()
        val progressBar = root.findViewById<View>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            for (i in 1..5) {
                try {
                    imageLinkedMap[i] = aparelhosViewModel.getBitmap(requireContext())
                } catch (_: HttpRequestTimeoutException) {
                    break
                }
                Log.i("Getha", "Added")
            }
            Log.w("Getha", "Size is $imageLinkedMap")
            progressBar.visibility = View.GONE

            if (imageLinkedMap.isEmpty) {
                val composeView = ComposeView(requireContext())
                composeView.setContent {
                    ServidorErrorPopup(navController)
                }
                buttonContainer.addView(composeView)
            } else {
                imageLinkedMap.forEach { id, image ->
                    Log.w("Getha", "Image is $image $id")
                    val imageButton = ImageButton(requireContext()).apply {
                        layoutParams = ViewGroup.LayoutParams(requireView().width/2, image.height)
                        setBackgroundColor(Color.parseColor("#E0E8E0"))
                        setImageBitmap(image)
                    }
                    imageButton.setOnClickListener { v: View? ->
                        val popup = PopupMenu(context, v)
                        popup.menuInflater.inflate(R.menu.popupmenu_aparelhos, popup.menu)
                        popup.show()
                        navController.setGraph(R.navigation.mobile_navigation)
                        popup.setOnMenuItemClickListener { item: MenuItem ->
                            when (item.itemId) {
                                R.id.action_manual -> {
                                    navController.navigate(AparelhosFragmentDirections.actionManual(id))
                                    return@setOnMenuItemClickListener true
                                }
                                R.id.action_video -> {
                                    navController.navigate(AparelhosFragmentDirections.actionVideo(id))
                                    return@setOnMenuItemClickListener true
                                }
                            }
                            false
                        }
                    }
                    buttonContainer.addView(imageButton)
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}