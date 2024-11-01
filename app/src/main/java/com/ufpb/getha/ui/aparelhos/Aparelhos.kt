package com.ufpb.getha.ui.aparelhos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ufpb.getha.utils.MyTopBarApp
import com.ufpb.getha.utils.ServidorErrorPopup
import kotlinx.coroutines.CoroutineScope
import com.ufpb.getha.R

@Composable
fun AparelhosScreen(
    viewModel: AparelhosViewModel = viewModel(),
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val isLoading = viewModel.isLoading.collectAsState().value
    val imageMap = viewModel.imagesMap.collectAsState().value

    MyTopBarApp(name = "Aparelhos", drawerState = drawerState, scope = scope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = colorResource(
                        id = R.color.green_main
                    ),
                    modifier = Modifier.align(
                        androidx.compose.ui.Alignment.Center
                    )
                )
            } else {
                if (imageMap.isEmpty()) {
                    ServidorErrorPopup(navController)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(imageMap.keys.size) { id ->
                            val bitmap = imageMap[id+1]!!
                            ImageButton(bitmap, id+1, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageButton(bitmap: Bitmap, id: Int, navController: NavController) {
    var expanded = remember { mutableStateOf(false) }

    var lighterDarkGray = Color(0xFF666666)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .clickable { expanded.value = true }
            .aspectRatio(1f)
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Show dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false })
        {
            DropdownMenuItem(
                text = { Text("Manual", color = lighterDarkGray) },
                onClick = {
                    navController.navigate("nav_manual/$id")
                    expanded.value = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_chrome_reader_mode_24),
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("Vídeo", color = lighterDarkGray) },
                onClick = {
                    navController.navigate("nav_video/$id")
                    expanded.value = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.video),
                        contentDescription = null
                    )
                }
            )
        }
    }
}
