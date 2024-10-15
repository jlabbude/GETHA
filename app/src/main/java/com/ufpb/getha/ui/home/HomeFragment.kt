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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ufpb.getha.utils.MyTopBarApp
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    // Load image from assets asynchronously
    LaunchedEffect(Unit) {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("logo.png")
        bitmap = BitmapFactory.decodeStream(inputStream)
    }

    MyTopBarApp {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "GETHA Logo",
                    modifier = Modifier
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                CircularProgressIndicator(color = Color.Green)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
