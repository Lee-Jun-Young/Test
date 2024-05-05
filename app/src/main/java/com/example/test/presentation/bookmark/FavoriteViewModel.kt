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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {
    val bookmarksState: StateFlow<BookmarksState> = localRepository.userData.map {
        BookmarksState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarksState.Loading,
    )

    fun postFavorite(isChecked: Boolean, data: UserInfo) {
        viewModelScope.launch {
            if (isChecked) {
                localRepository.postFavorite(data)
            } else {
                localRepository.deleteFavorite(data)
            }
        }
    }
}