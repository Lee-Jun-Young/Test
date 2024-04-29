package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.presentation.bookmark.BookmarkScreen
import com.example.test.presentation.bookmark.FavoriteViewModel
import com.example.test.presentation.detail.DetailScreen
import com.example.test.presentation.detail.DetailViewModel
import com.example.test.presentation.home.HomeScreen
import com.example.test.presentation.home.HomeViewModel
import com.example.test.presentation.search.SearchScreen
import com.example.test.presentation.search.SearchViewModel
import com.example.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

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
                    SettingsDialog {
                        showSettingsDialog = false
                    }
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
                                val viewModel = hiltViewModel<HomeViewModel>()
                                HomeScreen(viewModel) {
                                    showSettingsDialog = true
                                }
                            }
                            composable("search") {
                                val viewModel = hiltViewModel<SearchViewModel>()
                                SearchScreen(navController, viewModel) {
                                    selectedItemIdx = 0
                                }
                            }
                            composable("bookmark") {
                                val viewModel = hiltViewModel<FavoriteViewModel>()
                                BookmarkScreen(navController, viewModel)
                            }
                            composable("detail/{login}") { backStackEntry ->
                                val userId = backStackEntry.arguments?.getString("login") ?: ""
                                val viewModel = hiltViewModel<DetailViewModel>()
                                DetailScreen(userId, viewModel)
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun SettingsDialog(onDismiss: () -> Unit = {}) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = "테마, 언어 설정 추가 예정",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        confirmButton = {
            Text(
                text = "Close",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Parcelize
data class City(val name: String, val country: String) : Parcelable

@Preview
@Composable
fun CityScreen() {
    var selectedCity = rememberSaveable {
        mutableStateOf(City("Madrid", "Spain"))
    }
    Column {
        Text(text = selectedCity.value.name)
        Text(text = selectedCity.value.country)
        Button(onClick = {
            selectedCity.value = City("Seoul", "Korea")
        }) {
            Text(text = "Change")
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun TestComposable() {
    TestTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Test") }
                )
            },
        ) { _ ->

        }
    }
}