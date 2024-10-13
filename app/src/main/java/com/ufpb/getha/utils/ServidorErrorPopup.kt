package com.ufpb.getha.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController

@Composable
fun ServidorErrorPopup(navController: NavController) {
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        confirmButton = {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Ok")
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

