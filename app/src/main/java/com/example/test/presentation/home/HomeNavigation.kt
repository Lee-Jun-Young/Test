package com.example.test.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HOME_ROUTE: String = "home"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(onShowDialog: () -> Unit) {
    composable(HOME_ROUTE) {
        HomeRoute {
            onShowDialog()
        }
    }
}