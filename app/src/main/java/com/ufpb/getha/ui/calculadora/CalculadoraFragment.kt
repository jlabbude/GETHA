@file:Suppress("KotlinConstantConditions")

package com.ufpb.getha.ui.calculadora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import com.ufpb.getha.databinding.FragmentCalculadoraBinding

class CalculadoraFragment : Fragment() {
    private var binding: FragmentCalculadoraBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculadoraBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = colorResource(id = com.ufpb.getha.R.color.green_main),
                    onPrimary = colorResource(id = com.ufpb.getha.R.color.green_700),
                ),
            ) {
                CalculadoraSlot()
            }
        }
        binding!!.root.addView(composeView)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}