package com.ufpb.getha.ui.catalogo

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import com.ufpb.getha.utils.MyTopBarApp
import com.ufpb.getha.utils.ServidorErrorPopup
import kotlinx.coroutines.CoroutineScope

val mainColor = Color(0xFF598462)

typealias OrganismoComponent = @Composable (modifier: Modifier) -> Unit

val BACTERIA: OrganismoComponent = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.bacteria),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val VIRUS: OrganismoComponent = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.virus),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val FUNGO: OrganismoComponent = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.fungo),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val PROTOZOARIO: OrganismoComponent = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.protozoario),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}
val HELMINTO: OrganismoComponent = @Composable { modifier: Modifier ->
    Icon(
        painter = painterResource(id = R.drawable.helminto),
        contentDescription = null,
        modifier = modifier,
        tint = mainColor
    )
}

@SuppressLint("MutableCollectionMutableState")
@ExperimentalMaterial3ExpressiveApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoonoseScreen(
    viewModel: ZoonoseViewModel = viewModel(),
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val textFieldState = remember { mutableStateOf(TextFieldState()) }
    val _zoonoses = viewModel.zoonoses.collectAsState().value
    var connErr = false
    if (_zoonoses.isEmpty()) {
        connErr = true
    }
    var zoonoses = remember { mutableStateOf(mutableListOf<ZoonoseCardJSON>()) }
    val focus = LocalFocusManager.current
    zoonoses.value = _zoonoses.toMutableList()

    val isLoading = viewModel.isLoading.collectAsState().value
    MyTopBarApp(name = "Zoonoses", drawerState = drawerState, scope = scope) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.White),
            ) {
                CircularProgressIndicator(
                    color = colorResource(
                        id = R.color.green_main
                    ),
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        } else if (connErr) {
            ServidorErrorPopup(navController)
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {

                SearchBar(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .size(height = 60.dp, width = 400.dp)
                        .align(Alignment.CenterHorizontally),
                    inputField = {
                        SearchBarDefaults.InputField(
                            textStyle = MaterialTheme.typography.bodyMedium,
                            state = textFieldState.value,
                            onSearch = {
                                focus.clearFocus()
                                val zoo = mutableListOf<ZoonoseCardJSON>()
                                for (zoonose in _zoonoses) {
                                    if (zoonose.nome.contains(
                                            textFieldState.value.text,
                                            ignoreCase = true
                                        )
                                    ) {
                                        zoo.add(zoonose)
                                    }
                                }
                                zoonoses.value = zoo
                            },
                            expanded = false,
                            onExpandedChange = { },
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
                    onExpandedChange = { },
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                ) {}
                if (zoonoses.value.isEmpty()) {
                    Text(
                        text = "Nenhuma zoonose encontrada",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn {
                        items(zoonoses.value) { zoonose ->
                            ZoonoseCard(
                                id = zoonose.id,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .size(width = 400.dp, height = 105.dp),
                                nomePopular = zoonose.nome,
                                nomeCientifico = zoonose.nomeCientifico,
                                Organismo = Organismo.fromString(zoonose.organismo).toComponent(),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}


@ExperimentalMaterial3ExpressiveApi
@Composable
fun ZoonoseCard(
    id: String,
    nomePopular: String,
    nomeCientifico: String,
    modifier: Modifier,
    @Suppress("LocalVariableName") Organismo: OrganismoComponent,
    navController: NavController
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        border = BorderStroke((1.5).dp, colorResource(id = R.color.green_main)),
        content = {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Organismo(
                        Modifier
                            .size(55.dp)
                            .align(Alignment.Center)
                    )
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
            navController.navigate("nav_zoonose_full/${id}")
        }
    )
}