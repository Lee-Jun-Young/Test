package com.example.test.presentation.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.UserInfo
import com.example.test.domain.GithubRepository
import com.example.test.domain.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _bookmarkList = MutableStateFlow<List<UserInfo>?>(null)
    val bookmarkList: StateFlow<List<UserInfo>?> = _bookmarkList.asStateFlow()

    init {
        getBookmarkAll()
    }

    private fun getBookmarkAll() {
        viewModelScope.launch {
            localRepository.getFavoriteList().collectLatest {
                _bookmarkList.value = it
            }
        }
    }

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