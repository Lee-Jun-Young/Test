package com.example.test.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.R
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.presentation.bookmark.LoadingState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel(), onShowDialog: () -> Unit = {}) {
    val homeState by viewModel.myDataState.collectAsStateWithLifecycle()
    val repositoryState by viewModel.myRepositoriesUiState.collectAsStateWithLifecycle()

    HomeScreen(homeState = homeState, repositoryState = repositoryState) {
        onShowDialog()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeState: MyDataState,
    repositoryState: MyRepositoryState,
    onShowDialog: () -> Unit = {}
) {
    LazyColumn {
        item {
            when (homeState) {
                MyDataState.Loading -> LoadingState(modifier)
                is MyDataState.Success -> HomeContent(
                    userInfo = homeState.item,
                    onShowDialog = onShowDialog
                )
            }
        }
        item {
            when (repositoryState) {
                MyRepositoryState.Loading -> {}
                is MyRepositoryState.Success -> RepositoryList(
                    repositories = repositoryState.item
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    userInfo: UserInfo,
    onShowDialog: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        NiaTopAppBar(
            title = stringResource(id = R.string.home_text),
            actionIcon = Icons.Default.MoreVert,
            actionIconContentDescription = stringResource(id = R.string.settings_text),
            onActionClick = {
                onShowDialog()
            }
        )

        GlideImage(
            modifier = Modifier.size(200.dp),
            imageModel = { userInfo.avatarUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        )

        Text(text = userInfo.name ?: "")
        Text(text = userInfo.login)
        Row {
            Text(text = userInfo.followers.toString() + " followers")
            Text(text = " · ")
            Text(text = userInfo.following.toString() + " following")
        }
    }
}

@Composable
fun RepositoryList(
    modifier: Modifier = Modifier,
    repositories: List<RepositoryInfo>
) {
    Column(modifier = modifier) {
        repositories.forEach { repository ->
            RepositoryItem(data = repository)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NiaTopAppBar(
    title: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors
    )
}

@Composable
fun RepositoryItem(
    data: RepositoryInfo,
) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        border = BorderStroke(0.5.dp, Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(text = data.name, style = MaterialTheme.typography.titleMedium)
            Text(text = data.description ?: "", style = MaterialTheme.typography.titleSmall)
            Row {
                Text(
                    text = data.stargazersCount.toString() + " stars",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = " · ")
                Text(
                    text = data.forksCount.toString() + " forks",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}