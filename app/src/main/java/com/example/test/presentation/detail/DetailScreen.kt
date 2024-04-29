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
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.home.RepositoryItem
import com.example.test.presentation.search.testIconToggleButton
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DetailScreen(userId: String, viewModel: DetailViewModel) {
    viewModel.getUserById(userId)
    val user = viewModel.detailInfo.collectAsState()
    val repositories = viewModel.repositories.collectAsState()

    user.value?.let {
        DetailContent(it, repositories.value) { isChecked, it ->
            viewModel.postFavorite(isChecked, it)
        }
    }
}

@Composable
fun DetailContent(
    user: UserInfo,
    repositories: List<RepositoryInfo>?,
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

            repositories?.let {
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Repositories")
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(it.size) { idx ->
                        RepositoryItem(data = it[idx])
                    }
                }
            }
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