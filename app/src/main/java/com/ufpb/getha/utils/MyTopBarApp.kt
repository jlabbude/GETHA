package com.ufpb.getha.utils

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.ufpb.getha.IP
import com.ufpb.getha.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBarApp(
    name: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable ((PaddingValues) -> Unit)
) {
    val openDialog = remember { mutableStateOf(false) }
    val localIP = remember { mutableStateOf(IP) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = "Menu Icon",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.green_main) // Custom color for the AppBar
                ),
                actions = {
                    IconButton(
                        onClick = { openDialog.value = true },

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_settings_24),
                            contentDescription = "Menu Icon",
                            tint = Color.White,
                        )
                        if (openDialog.value) {
                            Dialog(onDismissRequest = {
                                openDialog.value = false
                                if (localIP.value != "") {
                                    IP = localIP.value
                                    Log.e("IP", IP)
                                }
                            }) {
                                Box {
                                    TextField(
                                        value = localIP.value,
                                        onValueChange = {
                                            localIP.value = it
                                        },
                                        label = { Text("Mudar IP") },
                                    )
                                }
                            }
                        }
                    }
                }
            )
        },
        content = content
    )
}