package com.example.test.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.UserInfo
import com.example.test.domain.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    localRepository: LocalRepository
) : ViewModel() {

    val bookmarksState: StateFlow<BookmarksState> = localRepository.userData.map {
        it.forEach { userInfo ->
            userInfo.isFavorite = true
        }
        BookmarksState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarksState.Loading,
    )
}

sealed interface BookmarksState {
    data object Loading : BookmarksState

    data class Success(
        val item: List<UserInfo>,
    ) : BookmarksState
}