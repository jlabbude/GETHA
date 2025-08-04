package com.ufpb.getha.ui.aparelhos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ufpb.getha.R
import com.ufpb.getha.ui.catalogo.mainColor
import com.ufpb.getha.utils.MyTopBarApp
import com.ufpb.getha.utils.ServidorErrorPopup
import kotlinx.coroutines.CoroutineScope

private const val MAX_LEN = 11

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
                if (imageMap.isEmpty) {
                    ServidorErrorPopup(navController)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        modifier = Modifier.padding(5.dp)
                    ) {
                        items(count = imageMap.keys.size, key = { index -> index }) { index ->
                            val aparelho = imageMap.keys.elementAt(index)
                            val bitmap = imageMap[aparelho]!!
                            ImageButton(bitmap, aparelho, navController)
                        }
                    }
                }
            }
        }
    }
}

/*@Preview
BitmapFactory.decodeStream(LocalContext.current.assets.open("logo.png")!!)!!,
AparelhoJSON(id = "1", nome = "Banho Maria"),
rememberNavController()
 */
@Composable
fun ImageButton(
    bitmap: Bitmap,
    aparelho: AparelhoJSON,
    navController: NavController,
) {
    var expanded = remember { mutableStateOf(false) }

    var lighterDarkGray = Color(0xFF666666)

    if (aparelho.nome.length > MAX_LEN) {
        aparelho.nome = aparelho.nome.slice(0..MAX_LEN-3) + "..."
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .clickable { expanded.value = true }
            .aspectRatio(1f)
            .border(
                width = (1.5).dp,
                color = mainColor,
                shape = RoundedCornerShape(16.dp)
            )

    ) {
        Text(
            aparelho.nome,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .zIndex(1f),
            fontSize = 48.sp,
            style = MaterialTheme.typography.bodyLarge.copy(
                shadow = Shadow(
                    color = Color(0x40000000),
                    offset = Offset(3.0f, 3.0f)
                ),
                fontSize = 64.sp
            ),
            color = Color(0xFF0B4C0E)
        )
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .shadow(16.dp)
        )

        // Show dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false })
        {
            DropdownMenuItem(
                text = { Text("Manual", color = lighterDarkGray) },
                onClick = {
                    navController.navigate("nav_manual/${aparelho.id}")
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
                    navController.navigate("nav_video/${aparelho.id}")
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
