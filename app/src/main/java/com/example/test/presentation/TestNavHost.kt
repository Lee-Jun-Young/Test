package com.example.test.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.test.presentation.bookmark.bookmarksScreen
import com.example.test.presentation.detail.detailScreen
import com.example.test.presentation.home.HOME_ROUTE
import com.example.test.presentation.home.homeScreen
import com.example.test.presentation.search.searchScreen

@Composable
fun TestNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
    onShowDialog: () -> Unit = {},
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen {
            onShowDialog()
        }
        bookmarksScreen { navController.navigate("detail/${it}") }
        searchScreen(onItemClick = { navController.navigate("detail/${it}") }) {

        }
        detailScreen()
    }
}
