package com.example.test.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.search.UserItem

@Composable
fun BookmarkRoute(
    onItemClicked: (String) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel()
) {

    val bookmarkState by viewModel.bookmarksState.collectAsStateWithLifecycle()

    BookmarkScreen(
        bookmarkState = bookmarkState,
        onItemClicked = onItemClicked,
        onFavoriteClicked = viewModel::postFavorite
    )
}

@Composable
internal fun BookmarkScreen(
    modifier: Modifier = Modifier,
    bookmarkState: BookmarksState,
    onItemClicked: (String) -> Unit,
    onFavoriteClicked: (Boolean, UserInfo) -> Unit
) {
    when (bookmarkState) {
        BookmarksState.Loading -> LoadingState(modifier)
        is BookmarksState.Success -> if (bookmarkState.item.isNotEmpty()) {
            BookmarksList(
                items = bookmarkState.item,
                onItemClicked = onItemClicked,
                onChangeFavorite = onFavoriteClicked
            )
        } else {
            EmptyState(modifier)
        }
    }
}

@Composable
fun BookmarksList(
    items: List<UserInfo>,
    onItemClicked: (String) -> Unit,
    onChangeFavorite: (Boolean, UserInfo) -> Unit
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollableState
    ) {
        items(items.size) { idx ->
            UserItem(user = items[idx], onItemClicked = onItemClicked) { isChecked, user ->
                onChangeFavorite(isChecked, user)
            }
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No bookmarks")
    }
}