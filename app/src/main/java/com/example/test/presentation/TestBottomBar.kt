package com.example.test.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.test.BottomNavigationItem

@Composable
fun TestBottomBar(
    navHostController: NavHostController,
    destinations: List<BottomNavigationItem>,
    onNavigateToDestination: (BottomNavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        destinations.forEachIndexed { idx, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.name.lowercase()) item.selectedIcon else item.unselectedIcon,
                        contentDescription = null
                    )
                },
                onClick = {
                    onNavigateToDestination(item)
                },
                selected = currentRoute == item.name.lowercase(),
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}