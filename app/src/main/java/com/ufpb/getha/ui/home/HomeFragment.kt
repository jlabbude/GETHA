package com.ufpb.getha.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ufpb.getha.utils.MyTopBarApp
import kotlinx.coroutines.CoroutineScope
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(drawerState: DrawerState, scope: CoroutineScope) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(Unit) {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("logo.png")
        bitmap = BitmapFactory.decodeStream(inputStream)
    }

    MyTopBarApp(
        name = "Home",
        drawerState = drawerState,
        scope = scope
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Column {
                    /*(modifier = Modifier.align(Alignment.Center))
                        Text(
                            text = "Bem-vindo",
                            style = TextStyle(fontSize = 24.sp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )*/
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "GETHA Logo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                CircularProgressIndicator(color = Color.Green)
            }
        }
    }
}
