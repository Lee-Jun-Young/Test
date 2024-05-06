package com.example.test.presentation.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val LOGIN_ARG = "login"
const val DETAIL_ROUTE = "detail/$LOGIN_ARG"

fun NavController.navigateToDetail(login: String, navOptions: NavOptions) {
    val route = "detail/$login"
    navigate(route, navOptions)
}

fun NavGraphBuilder.detailScreen() {
    composable(route = DETAIL_ROUTE) {
        val login = "it.arguments?.getString(LOGIN_ARG)"
        DetailRoute(login)
    }
}