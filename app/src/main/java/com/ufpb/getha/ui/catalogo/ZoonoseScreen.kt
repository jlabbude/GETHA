package com.ufpb.getha.ui.catalogo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ufpb.getha.R
import com.ufpb.getha.ui.aparelhos.AparelhosViewModel
import com.ufpb.getha.utils.MyTopBarApp
import kotlinx.coroutines.CoroutineScope

val mainColor = Color(0xFF598462)

typealias Organismo = @Composable (modifier: Modifier) -> Unit
val BACTERIA = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.bacteria),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val VIRUS = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.virus),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val FUNGO = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.fungo),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val PROTOZOARIO = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.protozoario),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}

@ExperimentalMaterial3ExpressiveApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoonoseScreen(
    viewModel: AparelhosViewModel = viewModel(),
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val textFieldState = remember { mutableStateOf(TextFieldState()) }
    MyTopBarApp(name = "Zoonoses", drawerState = drawerState, scope = scope) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            SearchBar(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .size(height = 50.dp, width = 400.dp)
                    .align(Alignment.CenterHorizontally),
                inputField = {
                    SearchBarDefaults.InputField(
                        state = textFieldState.value,
                        onSearch = {  },
                        expanded = false,
                        onExpandedChange = {  },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null,
                                tint = colorResource(R.color.green_main)
                            )
                        }, // maybe icon button later?
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright,
                ),
                expanded = false,
                onExpandedChange = {  },
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
            ) {
                /*Column(Modifier.verticalScroll(rememberScrollState())) {
                    repeat(4) { idx ->
                        val resultText = "Suggestion $idx"
                        ListItem(
                            headlineContent = { Text(resultText) },
                            supportingContent = { Text("Additional info") },
                            leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            modifier =
                            Modifier.clickable {
                                textFieldState.value.setTextAndPlaceCursorAtEnd(resultText)
                                expanded.value = false
                            }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }*/
            }
            for (i in 0..10) {
                if (i % 2 == 0) {
                    ZoonoseCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 400.dp, height = 105.dp),
                        nomePopular = "Brucelose",
                        nomeCientifico = "Brucella melitensis",
                        Organismo = BACTERIA
                    )
                } else if (i % 3 == 0) {
                    ZoonoseCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 400.dp, height = 105.dp),
                        nomePopular = "Raiva",
                        nomeCientifico = "Lyssavirus",
                        Organismo = VIRUS
                    )
                } else if (i % 5 == 0) {
                    ZoonoseCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 400.dp, height = 105.dp),
                        nomePopular = "Histoplasmose",
                        nomeCientifico = "Histoplasma capsulatum",
                        Organismo = FUNGO
                    )
                } else {
                    ZoonoseCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 400.dp, height = 105.dp),
                        nomePopular = "Toxoplasmose ",
                        nomeCientifico = "Toxoplasma Gondii",
                        Organismo = PROTOZOARIO
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ZoonoseCard(nomePopular: String, nomeCientifico: String, modifier: Modifier, @Suppress("LocalVariableName") Organismo: Organismo) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        border = BorderStroke((1.5).dp, colorResource(id = R.color.green_main)),
        content = {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.width(100.dp).align(Alignment.CenterVertically)) {
                    Organismo(Modifier.size(55.dp).align(Alignment.Center))
                    /*Canvas(
                        modifier = Modifier.fillMaxSize(),
                        onDraw = {
                            drawCircle(
                                color = mainColor,
                                radius = 70.0f
                            )
                        }
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = nomePopular[0].toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )*/
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = nomePopular,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = TextUnit(20f, TextUnitType.Sp),
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = nomeCientifico,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onClick = {

        }
    )
}