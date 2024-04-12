package com.ufpb.getha.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ufpb.getha.databinding.FragmentCalculadoraBinding

class CalculadoraFragment : Fragment() {
    private var binding: FragmentCalculadoraBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val calculadoraViewModel = ViewModelProvider(this).get(
            CalculadoraViewModel::class.java
        )
        binding = FragmentCalculadoraBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()
        val textView = binding!!.textGallery
        calculadoraViewModel.text.observe(getViewLifecycleOwner()) { text: CharSequence? ->
            textView.text = text
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}