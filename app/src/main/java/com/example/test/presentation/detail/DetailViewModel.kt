package com.example.test.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.domain.LocalRepository
import com.example.test.domain.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    val detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailRepositoriesUiState = MutableStateFlow<DetailRepoUiState>(DetailRepoUiState.Loading)

    fun getUserById(query: String) {
        viewModelScope.launch {
            repository.getUserById(query).collectLatest {
                getUserRepositories(it.login)

                localRepository.userData.map { bookmarks ->
                    bookmarks.forEach { bookmark ->
                        if (bookmark.login == it.login) {
                            it.isFavorite = true
                            return@forEach
                        }
                    }
                    it
                }.collect {
                    detailUiState.value = DetailUiState.Success(it)
                }
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

    private fun getUserRepositories(owner: String) {
        viewModelScope.launch {
            repository.getUserRepositories(owner).collectLatest { repositories ->
                detailRepositoriesUiState.value = DetailRepoUiState.Success(repositories)
            }
        }
    }
}

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Success(
        val item: UserInfo,
    ) : DetailUiState
}

sealed interface DetailRepoUiState {
    data object Loading : DetailRepoUiState

    data class Success(
        val item: List<RepositoryInfo>,
    ) : DetailRepoUiState
}