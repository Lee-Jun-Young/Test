package com.example.test.presentation.bookmark

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.test.data.dto.UserInfo

const val BOOKMARK_ROUTE = "bookmark"

fun NavController.navigateToBookmark(navOptions: NavOptions) = navigate(BOOKMARK_ROUTE, navOptions)

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.bookmarksScreen(
    onItemClick: (String) -> Unit,
    onBookmarkClick: (UserInfo) -> Unit,
    onShowSnackbar: (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    composable(BOOKMARK_ROUTE) {
        BookmarkRoute(
            onItemClicked = onItemClick,
            onBookmarkClicked = onBookmarkClick,
            onShowSnackbar = onShowSnackbar,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = this@composable
        )
    }
}