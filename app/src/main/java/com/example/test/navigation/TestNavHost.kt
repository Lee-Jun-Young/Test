package com.example.test.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.bookmark.bookmarksScreen
import com.example.test.presentation.detail.detailScreen
import com.example.test.presentation.detail.navigateToDetail
import com.example.test.presentation.home.HOME_ROUTE
import com.example.test.presentation.home.homeScreen
import com.example.test.presentation.search.searchScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TestNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
    onShowDialog: () -> Unit = {},
    onBookmarkClick: (UserInfo) -> Unit,
    onShowSnackbar: (Boolean) -> Unit
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            homeScreen {
                onShowDialog()
            }
            bookmarksScreen(
                onItemClick = { navController.navigateToDetail(it) },
                onBookmarkClick = onBookmarkClick,
                onShowSnackbar = onShowSnackbar,
                sharedTransitionScope = this@SharedTransitionLayout
            )

            searchScreen(
                onBackPress = { navController.popBackStack() },
                onItemClick = { navController.navigateToDetail(it) },
                onBookmarkClick = onBookmarkClick,
                sharedTransitionScope = this@SharedTransitionLayout
            )
            detailScreen(
                onBookmarkClick = onBookmarkClick,
                sharedTransitionScope = this@SharedTransitionLayout
            )
        }
    }
}
