package com.example.test.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    SEARCH("Search", Icons.Filled.Search, Icons.Outlined.Search),
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BOOKMARK("Bookmark", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
}