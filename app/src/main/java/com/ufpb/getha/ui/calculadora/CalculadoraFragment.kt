@file:Suppress("KotlinConstantConditions")

package com.ufpb.getha.ui.calculadora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
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
            MaterialTheme {
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