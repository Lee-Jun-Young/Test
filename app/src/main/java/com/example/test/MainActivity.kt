package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.presentation.bookmark.BookmarkRoute
import com.example.test.presentation.detail.DetailRoute
import com.example.test.presentation.home.HomeRoute
import com.example.test.presentation.search.SearchRoute
import com.example.test.presentation.setting.DarkThemeConfig
import com.example.test.presentation.setting.SettingsDialog
import com.example.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState = it
                }
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            TestTheme(darkTheme) {
                val items =
                    listOf(
                        BottomNavigationItem(
                            "Search",
                            Icons.Filled.Search,
                            Icons.Outlined.Search
                        ),
                        BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
                        BottomNavigationItem(
                            "Bookmark",
                            Icons.Filled.Favorite,
                            Icons.Outlined.FavoriteBorder
                        )
                    )
                var selectedItemIdx by rememberSaveable {
                    mutableIntStateOf(1)
                }
                val navController = rememberNavController()
                var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

                if (showSettingsDialog) {
                    SettingsDialog(
                        onDismiss = { showSettingsDialog = false }
                    )
                }

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
                                                    0 -> "search"
                                                    1 -> "home"
                                                    2 -> "bookmark"
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
                    ) { padding ->
                        NavHost(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .consumeWindowInsets(padding)
                                .windowInsetsPadding(
                                    WindowInsets.safeDrawing.only(
                                        WindowInsetsSides.Vertical,
                                    ),
                                ),
                            navController = navController,
                            startDestination = items[selectedItemIdx].title
                        ) {
                            composable("home") {
                                HomeRoute {
                                    showSettingsDialog = true
                                }
                            }
                            composable("search") {
                                SearchRoute(onBackClick = {
                                    navController.popBackStack()
                                }, onItemClicked = {
                                    navController.navigate("detail/$it")
                                })
                            }
                            composable("bookmark") {
                                BookmarkRoute(
                                    onItemClicked = {

                                    }
                                )
                            }
                            composable("detail/{login}") { backStackEntry ->
                                val userId = backStackEntry.arguments?.getString("login") ?: ""
                                DetailRoute(userId)
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
