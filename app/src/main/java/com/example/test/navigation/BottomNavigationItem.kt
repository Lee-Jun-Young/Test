package com.example.test.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.test.R

enum class BottomNavigationItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    SEARCH(R.string.search_text, Icons.Filled.Search, Icons.Outlined.Search),
    HOME(R.string.home_text, Icons.Filled.Home, Icons.Outlined.Home),
    BOOKMARK(R.string.bookmarks_text, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
}
