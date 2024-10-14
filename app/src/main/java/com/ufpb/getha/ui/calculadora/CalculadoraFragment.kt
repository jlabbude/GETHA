@file:Suppress("KotlinConstantConditions")

package com.ufpb.getha.ui.calculadora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun CalculadoraSlot() {
    var inputC1 by remember { mutableStateOf("") }
    var inputV1 by remember { mutableStateOf("") }
    var inputC2 by remember { mutableStateOf("") }
    var inputV2 by remember { mutableStateOf("") }

    var checkedC1 by remember { mutableStateOf(true) }
    var checkedV1 by remember { mutableStateOf(false) }
    var checkedC2 by remember { mutableStateOf(false) }
    var checkedV2 by remember { mutableStateOf(false) }

    var textC1 by remember { mutableStateOf("Incógnita") }
    var textV1 by remember { mutableStateOf("V1") }
    var textC2 by remember { mutableStateOf("C2") }
    var textV2 by remember { mutableStateOf("V2") }

    var valorCalculado by remember { mutableDoubleStateOf(0.0) }

    val focus = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Row {
            Checkbox(
                checked = checkedC1,
                onCheckedChange = {
                    when (it) {
                        true -> {
                            checkedC1 = it; textC1 = "Incógnita"
                            checkedV1 = false; textV1 = "V1"
                            checkedC2 = false; textC2 = "C2"
                            checkedV2 = false; textV2 = "V2"
                        }
                        false -> {}
                    }
              },
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = inputC1,
                onValueChange = { inputC1 = it.toDouble().toString() },
                label = { Text(textC1, modifier = Modifier.alpha(0.5f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Row {
            Checkbox(
                checked = checkedV1,
                onCheckedChange = {
                    when (it) {
                        true -> {
                            checkedC1 = false; textC1 = "C1"
                            checkedV1 = it; textV1 = "Incógnita"
                            checkedC2 = false; textC2 = "C2"
                            checkedV2 = false; textV2 = "V2"
                        }
                        false -> {}
                    }
              },
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = inputV1,
                onValueChange = { inputV1 = it.toDouble().toString() },
                label = { Text(textV1, modifier = Modifier.alpha(0.5f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Row {
            Checkbox(
                checked = checkedC2,
                onCheckedChange = {
                    when (it) {
                        true -> {
                            checkedC1 = false; textC1 = "C1"
                            checkedV1 = false; textV1 = "V1"
                            checkedC2 = it; textC2 = "Incógnita"
                            checkedV2 = false; textV2 = "V2"
                        }
                        false -> {}
                    }
              },
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = inputC2,
                onValueChange = { inputC2 = it.toDouble().toString() },
                label = { Text(textC2, modifier = Modifier.alpha(0.5f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Row {
            Checkbox(
                checked = checkedV2,
                onCheckedChange = {
                    when (it) {
                        true -> {
                            checkedC1 = false; textC1 = "C1"
                            checkedV1 = false; textV1 = "V1"
                            checkedC2 = false; textC2 = "C2"
                            checkedV2 = it; textV2 = "Incógnita"
                        }
                        false -> {}
                    }
              },
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = inputV2,
                onValueChange = { inputV2 = it.toDouble().toString() },
                label = { Text(textV2, modifier = Modifier.alpha(0.5f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 64.dp)
                .width(200.dp)
                .height(60.dp),
            onClick = {
                valorCalculado = conta(
                    Conta(
                        if (checkedC1) Variavel.Indefinida else Variavel.Definida(inputC1.toDouble()),
                        if (checkedV1) Variavel.Indefinida else Variavel.Definida(inputV1.toDouble()),
                        if (checkedC2) Variavel.Indefinida else Variavel.Definida(inputC2.toDouble()),
                        if (checkedV2) Variavel.Indefinida else Variavel.Definida(inputV2.toDouble())
                    )
                )!!
                focus.clearFocus()
            },
            border = BorderStroke(4.dp,
                colorResource(id = com.ufpb.getha.R.color.green_500)
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = com.ufpb.getha.R.color.green_500)
            )
        ) {
            Text("Calcular", color = Color.White)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "$valorCalculado",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            color = colorResource(id = com.ufpb.getha.R.color.green_700),
            fontSize = 24.sp,
        )
    }
}