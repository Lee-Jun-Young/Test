package com.example.test.presentation.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.search.SearchList
import com.example.test.presentation.search.UserItem


@Composable
fun BookmarkScreen(navController: NavController, viewModel: FavoriteViewModel) {
    val favoriteList = viewModel.bookmarkList.collectAsState()
    favoriteList.value?.let {
        if (it.isNotEmpty()) {
            FavoriteList(
                items = it,
                onItemClicked = { user ->
                    navController.navigate("detail/${user.login}")
                },
                onChangeFavorite = { isChecked, user ->
                    viewModel.postFavorite(isChecked, user)
                }
            )
        }
    }
}

@Composable
fun FavoriteList(
    items: List<UserInfo>,
    onItemClicked: (UserInfo) -> Unit,
    onChangeFavorite: (Boolean, UserInfo) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items.size) { idx ->
            UserItem(user = items[idx], onItemClicked = onItemClicked) { isChecked, user ->
                onChangeFavorite(isChecked, user)
            }
        }
    }
}