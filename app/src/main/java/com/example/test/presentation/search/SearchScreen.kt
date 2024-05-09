package com.example.test.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.bookmark.LoadingState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onItemClicked: (String) -> Unit,
    onBookmarkClicked: (UserInfo) -> Unit
) {
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()

    SearchScreen(
        searchUiState = searchUiState,
        onBackClick = onBackClick,
        onLoadMore = viewModel::loadMore,
        onSearchQueryChanged = {
            viewModel.searchUser(it)
        }, onFavoriteClicked = onBookmarkClicked
    ) {
        onItemClicked(it)
    }
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    searchUiState: SearchUiState,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMore: (String) -> Unit,
    onBackClick: () -> Unit,
    onFavoriteClicked: (UserInfo) -> Unit,
    onItemClicked: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        SearchToolbar(
            searchQuery = searchQuery,
            onSearchQueryChanged = {
                searchQuery = it
            },
            onSearchTriggered = {
                searchQuery = it
                if (searchQuery.isNotEmpty())
                    onSearchQueryChanged(searchQuery)
            },
            onBackClick = {
                onBackClick()
            }
        )
        when (searchUiState) {
            SearchUiState.Loading -> LoadingState(modifier.fillMaxSize())
            is SearchUiState.Success -> if (searchUiState.item.isNotEmpty()) {
                SearchList(
                    items = searchUiState.item,
                    onItemClicked = onItemClicked,
                    onChangeFavorite = { user ->
                        onFavoriteClicked(user)
                    },
                    onLoadMore = {
                        onLoadMore(searchQuery)
                    }
                )
            } else {
                Text("No data", modifier = Modifier.padding(16.dp))
            }

            else -> {}
        }
    }
}

@Composable
private fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
        )
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun SearchList(
    items: List<UserInfo>,
    onItemClicked: (String) -> Unit,
    onChangeFavorite: (UserInfo) -> Unit,
    onLoadMore: () -> Unit
) {
    val scrollableState = rememberLazyListState()
    val threadHold = 10

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollableState
    ) {

        itemsIndexed(items, key = { _, contact -> contact.login }) { index, user ->
            if (index == items.size.minus(threadHold)) {
                onLoadMore()
            }

            UserItem(user = user, onItemClicked = onItemClicked) { user ->
                onChangeFavorite(user)
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserInfo,
    onItemClicked: (String) -> Unit,
    onChangeFavorite: (UserInfo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("userItem")
            .clickable {
                onItemClicked(user.login)
            }
    ) {
        Row {
            GlideImage(
                modifier = Modifier.size(100.dp),
                imageModel = { user.avatarUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            Text(
                text = user.login,
                modifier = Modifier.padding(16.dp)
            )

            var favoriteChecked by remember { mutableStateOf(user.isFavorite) }
            testIconToggleButton(
                checked = favoriteChecked,
                onCheckedChange = { checked ->
                    favoriteChecked = checked
                    user.isFavorite = checked
                    onChangeFavorite(user)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                    )
                },
            )

        }
    }
}

@Composable
fun testIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = if (checked) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.12f,
                )
            } else {
                Color.Transparent
            },
        ),
    ) {
        if (checked) checkedIcon() else icon()
    }
}