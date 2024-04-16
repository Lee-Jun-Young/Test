package com.example.test.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.test.presentation.HomeViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Column {
        GlideImage(
            modifier = Modifier.size(200.dp),
            imageModel = { viewModel.userInfo.value?.avatarUrl ?: "" },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        )

        Text(text = viewModel.userInfo.value?.name ?: "")
        Text(text = viewModel.userInfo.value?.login ?: "")
        Row {
            // click event webView
            Text(text = viewModel.userInfo.value?.followers.toString() + " followers")
            Text(text = " · ")
            Text(text = viewModel.userInfo.value?.following.toString() + " following")
        }
    }
}