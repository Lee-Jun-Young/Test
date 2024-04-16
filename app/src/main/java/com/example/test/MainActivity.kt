package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.presentation.HomeViewModel
import com.example.test.presentation.home.HomeScreen
import com.example.test.ui.theme.TestTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                val items =
                    listOf(
                        BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
                        BottomNavigationItem(
                            "Profile",
                            Icons.Filled.AccountCircle,
                            Icons.Outlined.AccountCircle
                        ),
                        BottomNavigationItem(
                            "Settings",
                            Icons.Filled.Settings,
                            Icons.Outlined.Settings
                        )
                    )
                var selectedItemIdx by rememberSaveable {
                    mutableIntStateOf(0)
                }
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { idx, item ->
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                imageVector =
                                                if (selectedItemIdx == idx) {
                                                    item.selectedIcon
                                                } else {
                                                    item.unselectedIcon
                                                }, contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            selectedItemIdx = idx
                                            navController.navigate(
                                                when (idx) {
                                                    0 -> "home"
                                                    1 -> "profile"
                                                    2 -> "settings"
                                                    else -> "home"
                                                }
                                            )
                                        },
                                        selected = selectedItemIdx == idx,
                                        label = {
                                            Text(text = item.title)
                                        }
                                    )

                                }
                            }
                        }
                    ) { _ ->
                        NavHost(
                            navController = navController,
                            startDestination = items[selectedItemIdx].title
                        ) {
                            composable("home") {
                                val viewModel = hiltViewModel<HomeViewModel>()

                                LaunchedEffect(key1 = true) {
                                    viewModel.getUserInfo()
                                }
                                HomeScreen(viewModel)
                            }
                            composable("profile") {
                                ProfileScreen()
                            }
                            composable("settings") {
                                SettingsScreen()
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Text(text = "Profile")
}

@Composable
fun SettingsScreen() {
    Text(text = "Settings")
}