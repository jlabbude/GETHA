package com.ufpb.getha

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.*
import com.ufpb.getha.ui.aparelhos.AparelhosScreen
import com.ufpb.getha.ui.aparelhos.manual.ManualScreen
import com.ufpb.getha.ui.aparelhos.video.VideoScreen
import com.ufpb.getha.ui.calculadora.CalculadoraScreen
import com.ufpb.getha.ui.catalogo.ZoonoseScreen
import com.ufpb.getha.ui.catalogo.entries.ZoonoseEntryScreen
import com.ufpb.getha.ui.home.HomeScreen
import kotlinx.coroutines.launch
import java.io.InputStream

var IP = "192.168.15.10"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }

        setContent {
            YourAppTheme {
                NavigationDrawerContent()
            }
        }
    }
}

@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = colorResource(id = R.color.green_main),
            onPrimary = colorResource(id = R.color.green_700),
        ),
        shapes = MaterialTheme.shapes,
        typography = MaterialTheme.typography,
        content = content
    )
}

//todo make constructor to avoid boilerplate of adding ModalNavigationDrawer and NavHost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationDrawerContent() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("logo.png")
        bitmap = BitmapFactory.decodeStream(inputStream)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Getha Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("nav_home")
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Calculadora") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("nav_calculadora")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_calculate_24),
                            contentDescription = null
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Aparelhos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("nav_aparelhos")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_device_hub_24),
                            contentDescription = null
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Zoonoses") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("nav_zoonose")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.home_storage_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                            contentDescription = null
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        NavHost(navController = navController, startDestination = "nav_home") {
            composable("nav_home") { HomeScreen(drawerState = drawerState, scope = scope) }
            composable("nav_calculadora") {
                CalculadoraScreen(
                    drawerState = drawerState,
                    scope = scope
                )
            }
            composable("nav_aparelhos") {
                AparelhosScreen(
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
            composable("nav_manual/{aparelhoId}") { backStackEntry ->
                val aparelhoId = backStackEntry.arguments?.getString("aparelhoId")!!
                ManualScreen(aparelhoId)
            }
            composable("nav_video/{aparelhoId}") { backStackEntry ->
                val aparelhoId = backStackEntry.arguments?.getString("aparelhoId")!!
                VideoScreen(aparelhoId)
            }
            composable("nav_zoonose") {
                ZoonoseScreen(
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
            composable("nav_zoonose_full/{zoonoseId}") { backStackEntry ->
                val zoonoseId = backStackEntry.arguments?.getString("zoonoseId")!!
                ZoonoseEntryScreen(zoonoseID = zoonoseId)
            }
        }
    }
}
