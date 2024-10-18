package com.ufpb.getha.ui.calculadora

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class SlotState(
    var input: String = "",
    var checked: Boolean = false,
    var label: String
)

@Composable
@Preview
fun CalculadoraSlot() {

    val slotC1 = remember { mutableStateOf(SlotState(label = "Incógnita", checked = true)) }
    val slotV1 = remember { mutableStateOf(SlotState(label = "V1")) }
    val slotC2 = remember { mutableStateOf(SlotState(label = "C2")) }
    val slotV2 = remember { mutableStateOf(SlotState(label = "V2")) }

    var valorDisplay by remember { mutableStateOf("") }

    val green700 = colorResource(id = com.ufpb.getha.R.color.green_700)
    val greenMain = colorResource(id = com.ufpb.getha.R.color.green_main)
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    @Composable
    fun SlotRow(
        slotState: MutableState<SlotState>,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
            horizontalArrangement = Arrangement.Center) {
            Checkbox(
                checked = slotState.value.checked,
                onCheckedChange = {
                    onCheckedChange(it)
                },
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = slotState.value.input,
                onValueChange = {
                    slotState.value =
                        slotState
                            .value
                            .copy(input = it
                                .filterIndexed { index, char ->
                                    char.isDigit() || (char == '.' || char == ',') && !it.take(index).contains('.') && !it.take(index).contains(',')
                                }
                                .replace(',', '.'))
                    },
                label = { Text(slotState.value.label, modifier = Modifier.alpha(0.5f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = enabled,
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        SlotRow(
            slotState = slotC1,
            onCheckedChange = { checked ->
                if (checked) {
                    slotC1.value = slotC1.value.copy(label = "Incógnita", input = "", checked = true)
                    slotV1.value = slotV1.value.copy(label = "V1", checked = false)
                    slotC2.value = slotC2.value.copy(label = "C2", checked = false)
                    slotV2.value = slotV2.value.copy(label = "V2", checked = false)
                }
            },
            enabled = !slotC1.value.checked
        )

        SlotRow(
            slotState = slotV1,
            onCheckedChange = { checked ->
                if (checked) {
                    slotC1.value = slotC1.value.copy(label = "C1", checked = false)
                    slotV1.value = slotV1.value.copy(label = "Incógnita", input = "", checked = true)
                    slotC2.value = slotC2.value.copy(label = "C2", checked = false)
                    slotV2.value = slotV2.value.copy(label = "V2", checked = false)
                }
            },
            enabled = !slotV1.value.checked
        )

        SlotRow(
            slotState = slotC2,
            onCheckedChange = { checked ->
                if (checked) {
                    slotC1.value = slotC1.value.copy(label = "C1", checked = false)
                    slotV1.value = slotV1.value.copy(label = "V1", checked = false)
                    slotC2.value = slotC2.value.copy(label = "Incógnita", input = "", checked = true)
                    slotV2.value = slotV2.value.copy(label = "V2", checked = false)
                }
            },
            enabled = !slotC2.value.checked
        )

        SlotRow(
            slotState = slotV2,
            onCheckedChange = { checked ->
                if (checked) {
                    slotC1.value = slotC1.value.copy(label = "C1", checked = false)
                    slotV1.value = slotV1.value.copy(label = "V1", checked = false)
                    slotC2.value = slotC2.value.copy(label = "C2", checked = false)
                    slotV2.value = slotV2.value.copy(label = "Incógnita", input = "", checked = true)
                }
            },
            enabled = !slotV2.value.checked
        )

        Button(
            onClick = {
                if ((slotC1.value.input.isEmpty() && !slotC1.value.checked) ||
                    (slotV1.value.input.isEmpty() && !slotV1.value.checked) ||
                    (slotC2.value.input.isEmpty() && !slotC2.value.checked) ||
                    (slotV2.value.input.isEmpty() && !slotV2.value.checked)
                ) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Preencha todos os campos!")
                    }
                } else {
                    try {
                        val contaResultado = conta(
                            Conta(
                                if (slotC1.value.checked) Variavel.Indefinida else Variavel.Definida(slotC1.value.input.toDouble()),
                                if (slotV1.value.checked) Variavel.Indefinida else Variavel.Definida(slotV1.value.input.toDouble()),
                                if (slotC2.value.checked) Variavel.Indefinida else Variavel.Definida(slotC2.value.input.toDouble()),
                                if (slotV2.value.checked) Variavel.Indefinida else Variavel.Definida(slotV2.value.input.toDouble())
                            )
                        )!! // <- Should never happen but who knows
                        valorDisplay = if (contaResultado % 1.0 == 0.0) {
                            contaResultado.toInt().toString()
                        } else {
                            if (contaResultado.isNaN()) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Divisão por 0 ")
                                }
                                "0"
                            } else {
                                contaResultado.toString()
                            }
                        }
                    } catch (_: NumberFormatException) { // <- Should ALSO never happen but who knows
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Insira um número válido!")
                        }
                    }
                }
                focusManager.clearFocus()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 64.dp)
                .width(200.dp)
                .height(60.dp),
            border = BorderStroke(4.dp, greenMain),
            colors = ButtonDefaults.buttonColors(containerColor = greenMain)
        ) {
            Text("Calcular", color = Color.White)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = valorDisplay,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            color = green700,
            fontSize = 24.sp
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}