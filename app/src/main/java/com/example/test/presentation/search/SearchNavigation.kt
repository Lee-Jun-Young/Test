package com.example.test.presentation.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.test.data.dto.UserInfo

const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(SEARCH_ROUTE, navOptions)

fun NavGraphBuilder.searchScreen(
    onItemClick: (String) -> Unit,
    onBackPress: () -> Unit,
    onBookmarkClick: (UserInfo) -> Unit
) {
    composable(SEARCH_ROUTE) {
        SearchRoute(
            onBackClick = onBackPress,
            onItemClicked = onItemClick,
            onBookmarkClicked = onBookmarkClick
        )
    }
}