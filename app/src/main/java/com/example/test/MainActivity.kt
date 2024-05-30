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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.test.navigation.BottomNavigationItem
import com.example.test.navigation.TestBottomBar
import com.example.test.navigation.TestNavHost
import com.example.test.presentation.bookmark.navigateToBookmark
import com.example.test.presentation.home.navigateToHome
import com.example.test.presentation.search.navigateToSearch
import com.example.test.presentation.setting.DarkThemeConfig
import com.example.test.presentation.setting.SettingsDialog
import com.example.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            val navController = rememberNavController()

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
                var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

                if (showSettingsDialog) {
                    SettingsDialog(
                        onDismiss = { showSettingsDialog = false }
                    )
                }

                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val showBottomBar =
                    navBackStackEntry?.destination?.route in BottomNavigationItem.entries.map { it.name.lowercase() }

                val currentDestination = navBackStackEntry?.destination

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = { snackbarData ->
                                    Snackbar(
                                        snackbarData = snackbarData,
                                    )
                                }
                            )
                        },
                        bottomBar = {
                            if (showBottomBar) {
                                TestBottomBar(
                                    destinations = BottomNavigationItem.entries,
                                    onNavigateToDestination = {
                                        navigateDestination(navController, it)
                                    },
                                    currentDestination = currentDestination,
                                )
                            }
                        }) { padding ->
                        TestNavHost(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .consumeWindowInsets(padding)
                                .windowInsetsPadding(
                                    WindowInsets.safeDrawing.only(
                                        WindowInsetsSides.Vertical,
                                    ),
                                ),
                            onShowDialog = { showSettingsDialog = true },
                            onBookmarkClick = viewModel::postFavorite,
                            onShowSnackbar = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (it) "북마크를 추가하였습니다." else "북마크를 해제하였습니다.",
                                        actionLabel = "닫기",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun navigateDestination(
    navController: NavHostController,
    bottomNavigationItem: BottomNavigationItem
) {
    val topLevelNavOptions = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (bottomNavigationItem) {
        BottomNavigationItem.HOME -> navController.navigateToHome(topLevelNavOptions)
        BottomNavigationItem.SEARCH -> navController.navigateToSearch(topLevelNavOptions)
        BottomNavigationItem.BOOKMARK -> navController.navigateToBookmark(topLevelNavOptions)
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
