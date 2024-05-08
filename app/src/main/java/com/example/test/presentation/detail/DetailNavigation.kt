package com.example.test.presentation.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.test.data.dto.UserInfo

const val LOGIN_ARG = "login"
const val DETAIL_ROUTE = "detail/{$LOGIN_ARG}"

fun NavController.navigateToDetail(login: String, navOptions: NavOptions? = null) {
    val route = "detail/$login"
    navigate(route, navOptions)
}

fun NavGraphBuilder.detailScreen(onBookmarkClick: (UserInfo) -> Unit) {
    composable(
        route = DETAIL_ROUTE,
        arguments = listOf(navArgument(LOGIN_ARG) { type = NavType.StringType })
    ) {
        DetailRoute(
            login = it.arguments?.getString(LOGIN_ARG) ?: "",
            onBookmarkClick = onBookmarkClick
        )
    }
}