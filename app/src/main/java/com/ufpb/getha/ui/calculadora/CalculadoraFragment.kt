package com.ufpb.getha.ui.calculadora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}