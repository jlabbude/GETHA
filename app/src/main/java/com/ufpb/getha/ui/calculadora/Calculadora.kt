package com.ufpb.getha.ui.calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ufpb.getha.utils.MyTopBarApp
import kotlinx.coroutines.CoroutineScope

@Composable
fun CalculadoraScreen(drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                      scope: CoroutineScope = rememberCoroutineScope()) {
    MyTopBarApp(name = "Calculadora", drawerState = drawerState, scope = scope) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colorResource(id = com.ufpb.getha.R.color.green_main),
                onPrimary = colorResource(id = com.ufpb.getha.R.color.green_700),
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.White),
            ) {
                CalculadoraSlots()
            }
        }
    }
}


