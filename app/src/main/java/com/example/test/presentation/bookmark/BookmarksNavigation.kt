package com.example.test.presentation.bookmark

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val BOOKMARKS_ROUTE = "bookmarks"

fun NavController.navigateToBookmark(navOptions: NavOptions) = navigate(BOOKMARKS_ROUTE, navOptions)

fun NavGraphBuilder.bookmarksScreen(onItemClick: (String) -> Unit) {
    composable(BOOKMARKS_ROUTE) {
        BookmarkRoute(onItemClick)
    }
}