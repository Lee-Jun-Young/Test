@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.test.presentation.bookmark

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.R
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.search.UserItem

@Composable
fun BookmarkRoute(
    onItemClicked: (String) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
    onBookmarkClicked: (UserInfo) -> Unit,
    onShowSnackbar: (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val bookmarkState by viewModel.bookmarksState.collectAsStateWithLifecycle()

    BookmarkScreen(
        bookmarkState = bookmarkState,
        onItemClicked = onItemClicked,
        onFavoriteClicked = onBookmarkClicked,
        onShowSnackbar = onShowSnackbar,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@Composable
internal fun BookmarkScreen(
    modifier: Modifier = Modifier,
    bookmarkState: BookmarksState,
    onItemClicked: (String) -> Unit,
    onFavoriteClicked: (UserInfo) -> Unit,
    onShowSnackbar: (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    when (bookmarkState) {
        BookmarksState.Loading -> LoadingState(modifier)
        is BookmarksState.Success -> if (bookmarkState.item.isNotEmpty()) {
            BookmarksList(
                items = bookmarkState.item,
                onItemClicked = onItemClicked,
                onChangeFavorite = onFavoriteClicked,
                onShowSnackbar = onShowSnackbar,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
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
    onChangeFavorite: (UserInfo) -> Unit,
    onShowSnackbar: (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollableState
    ) {
        itemsIndexed(items, key = { _, contact -> contact.id }) { index, user ->
            UserItem(
                user = user,
                onItemClicked = onItemClicked,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
            ) { user ->
                onChangeFavorite(user)
                onShowSnackbar(user.isFavorite)
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
        Text(text = stringResource(id = R.string.bookmark_result_empty))
    }
}