package com.ufpb.getha.ui.calculadora

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ufpb.getha.utils.MyTopBarApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class SlotState(
    var input: String = "",
    var isIncognita: Boolean = false,
    var label: String
)

private val LocalCalculatorActions = compositionLocalOf<CalculatorActions> {
    error("No CalculatorActions provided")
}

private data class CalculatorActions(
    val onNumberClick: (String) -> Unit,
    val onClearClick: () -> Unit,
    val onDecimalClick: () -> Unit,
    val onNextSlot: () -> Unit,
    val onPrevSlot: () -> Unit,
    val onBackspace: () -> Unit
)

@Composable
@Preview
fun CalculadoraScreen(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    MyTopBarApp(name = "Calculadora", drawerState = drawerState, scope = scope) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colorResource(id = com.ufpb.getha.R.color.green_main),
                onPrimary = colorResource(id = com.ufpb.getha.R.color.green_700),
            ),
        ) {
            CalculatorContent(paddingValues = it)
        }
    }
}

@Composable
fun CalculatorContent(paddingValues: PaddingValues) {
    val slots = listOf(
        remember { mutableStateOf(SlotState(label = "C1")) },
        remember { mutableStateOf(SlotState(label = "V1")) },
        remember { mutableStateOf(SlotState(label = "C2")) },
        remember { mutableStateOf(SlotState(label = "V2", isIncognita = true)) }
    )

    var currentFocusedSlot by remember { mutableStateOf(0) }
    var valorDisplay by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Function to toggle incognita state
    fun toggleIncognita(index: Int) {
        slots.forEachIndexed { i, slot ->
            if (i == index) {
                slot.value = slot.value.copy(
                    isIncognita = true,
                    input = "",
                )
            } else {
                if (slot.value.isIncognita) {
                    slot.value = slot.value.copy(
                        isIncognita = false,
                    )
                }
            }
        }
    }

    // Function to add character to current slot
    fun addCharToCurrentSlot(char: String) {
        val currentSlot = slots[currentFocusedSlot]
        if (!currentSlot.value.isIncognita) {
            val currentText = currentSlot.value.input
            if (char == "C") {
                currentSlot.value = currentSlot.value.copy(input = "")
            } else if (char == ".") {
                if (!currentText.contains('.')) {
                    currentSlot.value = currentSlot.value.copy(input = currentText + char)
                }
            } else {
                currentSlot.value = currentSlot.value.copy(input = currentText + char)
            }
        }
    }

    fun navigateSlot(direction: Int) {
        // Calculate the next slot index in a circular fashion.
        var nextSlot = (currentFocusedSlot + direction).mod(slots.size)
        // Save the starting index to prevent an infinite loop if all slots are incognita.
        val startingSlot = currentFocusedSlot

        // Loop until a non-incognita slot is found or we've checked all slots.
        while (nextSlot != startingSlot && slots[nextSlot].value.isIncognita) {
            nextSlot = (nextSlot + direction).mod(slots.size)
        }
        // Update focus only if the found slot is not incognita.
        if (!slots[nextSlot].value.isIncognita) {
            currentFocusedSlot = nextSlot
        }
    }


    // Clear last character
    fun clearLastChar() {
        val currentSlot = slots[currentFocusedSlot]
        if (!currentSlot.value.isIncognita && currentSlot.value.input.isNotEmpty()) {
            currentSlot.value = currentSlot.value.copy(
                input = currentSlot.value.input.dropLast(1)
            )
        }
    }

    // Create the actions object
    val calculatorActions = CalculatorActions(
        onNumberClick = { addCharToCurrentSlot(it) },
        onClearClick = { addCharToCurrentSlot("C") },
        onDecimalClick = { addCharToCurrentSlot(".") },
        onNextSlot = { navigateSlot(1) },
        onPrevSlot = { navigateSlot(-1) },
        onBackspace = { clearLastChar() }
    )

    // Provide the actions to the composition
    CompositionLocalProvider(LocalCalculatorActions provides calculatorActions) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CalculadoraSlots(
                    slots = slots,
                    currentFocusedSlot = currentFocusedSlot,
                    onFocusChange = { currentFocusedSlot = it },
                    onToggleIncognita = { toggleIncognita(it) },
                    valorDisplay = valorDisplay,
                    onCalculate = {
                        if (slots.any { slot -> slot.value.input.isEmpty() && !slot.value.isIncognita }) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Preencha todos os campos!")
                            }
                        } else {
                            try {
                                val contaResultado = conta(
                                    Conta(
                                        if (slots[0].value.isIncognita) Variavel.Indefinida else Variavel.Definida(
                                            slots[0].value.input.toDouble()
                                        ),
                                        if (slots[1].value.isIncognita) Variavel.Indefinida else Variavel.Definida(
                                            slots[1].value.input.toDouble()
                                        ),
                                        if (slots[2].value.isIncognita) Variavel.Indefinida else Variavel.Definida(
                                            slots[2].value.input.toDouble()
                                        ),
                                        if (slots[3].value.isIncognita) Variavel.Indefinida else Variavel.Definida(
                                            slots[3].value.input.toDouble()
                                        )
                                    )
                                )!!
                                valorDisplay = if (contaResultado % 1.0 == 0.0) {
                                    contaResultado.toInt().toString()
                                } else {
                                    if (contaResultado.isNaN() || contaResultado.isInfinite()) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Divisão por 0")
                                        }
                                        "0"
                                    } else {
                                        contaResultado.toString()
                                    }
                                }
                            } catch (_: NumberFormatException) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Insira um número válido!")
                                }
                            }
                        }
                    },
                    snackbarHostState = snackbarHostState
                )
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    CalculadoraKeyboard()
                }
            }
        }
    }
}

@Composable
fun CalculadoraSlots(
    slots: List<MutableState<SlotState>>,
    currentFocusedSlot: Int,
    onFocusChange: (Int) -> Unit,
    onToggleIncognita: (Int) -> Unit,
    valorDisplay: String,
    onCalculate: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val greenMain = colorResource(id = com.ufpb.getha.R.color.green_main)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        // Single input field with 4 subdivisions
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                slots.forEachIndexed { index, slotState ->
                    if (index > 0) {
                        VerticalDivider()
                    }

                    InputSubdivision(
                        slotState = slotState,
                        isFocused = currentFocusedSlot == index,
                        onToggleIncognita = { onToggleIncognita(index) },
                        onFocus = { onFocusChange(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Button(
            onClick = onCalculate,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 8.dp)
                .width(200.dp)
                .height(48.dp),
            border = BorderStroke(2.dp, greenMain),
            colors = ButtonDefaults.buttonColors(containerColor = greenMain)
        ) {
            Text("Calcular", color = Color.White)
        }

        if (valorDisplay.isNotEmpty()) {
            Text(
                text = "Resultado: $valorDisplay",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
private fun InputSubdivision(
    slotState: MutableState<SlotState>,
    isFocused: Boolean,
    onToggleIncognita: () -> Unit,
    onFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused = isFocused
    if (slotState.value.isIncognita) {
        isFocused = false
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onFocus() }
    ) {
        Text(
            text = slotState.value.label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.7f)
        )

        Box(modifier = Modifier.clickable { onFocus() }) {
            TextField(
                value = slotState.value.input,
                onValueChange = { /* Input handled by custom keyboard */ },
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(80.dp, 80.dp).clickable { onFocus() }
                    .border(
                        width = if (isFocused) 2.dp else 0.dp,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        Button(
            onClick = onToggleIncognita,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (slotState.value.isIncognita)
                    MaterialTheme.colorScheme.primary else
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
                .padding(top = 4.dp)
                .height(24.dp)
        ) {
            Text(
                "x",
                style = MaterialTheme.typography.labelSmall,
                color = if (slotState.value.isIncognita)
                    Color.White else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(100.dp)
            .background(Color.Gray.copy(alpha = 0.3f))
    )
}

@Composable
fun CalculadoraKeyboard() {
    val calculatorActions = LocalCalculatorActions.current
    val shadow = RoundedCornerShape(
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
        topStart = 32.dp,
        topEnd = 32.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 4.dp,
                shape = shadow
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outlineVariant,
                        MaterialTheme.colorScheme.surfaceDim
                    ),
                ),
                shape = shadow
            )
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculadoraKeyboardKey(
                "1",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("1") }
            CalculadoraKeyboardKey(
                "2",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("2") }
            CalculadoraKeyboardKey(
                "3",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("3") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculadoraKeyboardKey(
                "4",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("4") }
            CalculadoraKeyboardKey(
                "5",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("5") }
            CalculadoraKeyboardKey(
                "6",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("6") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculadoraKeyboardKey(
                "7",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("7") }
            CalculadoraKeyboardKey(
                "8",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("8") }
            CalculadoraKeyboardKey(
                "9",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("9") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculadoraKeyboardKey(".", Modifier.weight(1f)) { calculatorActions.onDecimalClick() }
            CalculadoraKeyboardKey(
                "0",
                Modifier.weight(1f)
            ) { calculatorActions.onNumberClick("0") }
            CalculadoraKeyboardKey("CE", Modifier.weight(1f)) { calculatorActions.onClearClick() }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CalculadoraKeyboardIconKey(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                modifier = Modifier.weight(1f)
            ) { calculatorActions.onPrevSlot() }
            CalculadoraKeyboardIconKey(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                modifier = Modifier.weight(1f)
            ) { calculatorActions.onNextSlot() }
            CalculadoraKeyboardIconKey(
                icon = {
                    Icon(
                        painter = painterResource(id = com.ufpb.getha.R.drawable.baseline_backspace_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                modifier = Modifier.weight(1f)
            ) { calculatorActions.onBackspace() }
        }
    }
}

@Composable
fun CalculadoraKeyboardKey(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(text, color = Color.White, fontSize = 20.sp)
    }
}

@Composable
fun CalculadoraKeyboardIconKey(icon: @Composable () -> Unit, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        icon()
    }
}