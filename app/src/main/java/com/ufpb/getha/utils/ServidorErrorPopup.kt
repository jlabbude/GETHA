package com.ufpb.getha.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController

@Composable
fun ServidorErrorPopup(navController: NavController) {
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        confirmButton = {
            Button(onClick = { navController.navigateUp() }) {
                Text("Ok", color = Color.White)
            }
        },
        title = {
            Text(text = "Alerta")
        },
        text = {
            Text("Não foi possível estabelecer uma conexão com o servidor.")
        },
        properties = DialogProperties(dismissOnClickOutside = false),
        shape = RoundedCornerShape(16.dp),
    )
}