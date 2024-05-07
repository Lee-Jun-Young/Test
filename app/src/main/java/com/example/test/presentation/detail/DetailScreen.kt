package com.example.test.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.bookmark.LoadingState
import com.example.test.presentation.home.RepositoryItem
import com.example.test.presentation.search.testIconToggleButton
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DetailRoute(login: String, viewModel: DetailViewModel = hiltViewModel()) {

    val detailUiState by viewModel.detailUiState.collectAsState()
    val detailRepositoriesUiState by viewModel.detailRepositoriesUiState.collectAsState()

    viewModel.getUserById(login)

    DetailScreen(
        detailUiState = detailUiState,
        detailRepoUiState = detailRepositoriesUiState,
        onFavoriteClick = viewModel::postFavorite
    )
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState,
    detailRepoUiState: DetailRepoUiState,
    onFavoriteClick: (Boolean, UserInfo) -> Unit
) {
    LazyColumn {
        item {
            when (detailUiState) {
                DetailUiState.Loading -> LoadingState(modifier)
                is DetailUiState.Success -> {
                    DetailContent(
                        user = detailUiState.item,
                        onChangeFavorite = onFavoriteClick
                    )
                }
            }
        }

        item {
            when (detailRepoUiState) {
                DetailRepoUiState.Loading -> {}
                is DetailRepoUiState.Success -> {
                    RepositoryList(
                        modifier = Modifier.padding(16.dp),
                        repositories = detailRepoUiState.item
                    )
                }
            }
        }
    }
}

@Composable
fun RepositoryList(
    modifier: Modifier = Modifier,
    repositories: List<RepositoryInfo>
) {
    Column(modifier = modifier) {
        Text(text = "Repositories")
        Spacer(modifier = Modifier.height(8.dp))
        repositories.forEach { repository ->
            RepositoryItem(data = repository)
        }
    }
}

@Composable
fun DetailContent(
    user: UserInfo,
    onChangeFavorite: (Boolean, UserInfo) -> Unit
) {
    Box {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                imageModel = { user.avatarUrl })
            Text(text = user.name ?: "")
            Text(text = user.login)
            Text(text = "${user.followers} followers · ${user.following} following")
        }

        var favoriteChecked by rememberSaveable { mutableStateOf(user.isFavorite) }
        testIconToggleButton(
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd),
            checked = favoriteChecked,
            onCheckedChange = { checked ->
                favoriteChecked = checked
                onChangeFavorite(checked, user)
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