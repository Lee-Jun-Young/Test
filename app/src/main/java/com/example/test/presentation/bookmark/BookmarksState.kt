package com.example.test.presentation.bookmark

import com.example.test.data.dto.UserInfo

sealed interface BookmarksState {
    data object Loading : BookmarksState

    data class Success(
        val item: List<UserInfo>,
    ) : BookmarksState
}