package com.example.test.presentation.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(SEARCH_ROUTE, navOptions)

fun NavGraphBuilder.searchScreen(onItemClick: (String) -> Unit, onBackPress: () -> Unit) {
    composable(SEARCH_ROUTE) {
        SearchRoute(
            onBackClick = onBackPress,
            onItemClicked = onItemClick
        )
    }
}