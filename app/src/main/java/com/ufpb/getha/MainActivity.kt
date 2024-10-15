package com.ufpb.getha

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.ufpb.getha.ui.aparelhos.AparelhosScreen
import com.ufpb.getha.ui.calculadora.CalculadoraScreen
import com.ufpb.getha.ui.home.HomeScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Navigation bar", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
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
                    icon = { Icon(painter = painterResource(R.drawable.baseline_calculate_24), contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Aparelhos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("nav_aparelhos")
                    },
                    icon = { Icon(painter = painterResource(R.drawable.baseline_device_hub_24), contentDescription = null) }
                )
            }
        },
        drawerState = drawerState
    ) {
        NavHost(navController = navController, startDestination = "nav_home") {
            composable("nav_home") { HomeScreen() }
            composable("nav_calculadora") { CalculadoraScreen() }
            composable("nav_aparelhos") { AparelhosScreen(navController = navController) }
            composable("nav_manual/{aparelhoId}") { backStackEntry ->
                val aparelhoId = backStackEntry.arguments?.getString("aparelhoId")
                ManualScreen(aparelhoId)
            }
            composable("nav_video/{aparelhoId}") { backStackEntry ->
                val aparelhoId = backStackEntry.arguments?.getString("aparelhoId")
                VideoScreen(aparelhoId)
            }
        }
    }
}

@Composable
fun ManualScreen(aparelhoId: String?) {
    // Manual screen content for a specific aparelhoId
}

@Composable
fun VideoScreen(aparelhoId: String?) {
    // Video screen content for a specific aparelhoId
}
