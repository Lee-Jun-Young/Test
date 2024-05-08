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
import kotlinx.coroutines.flow.combine
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
            val userInfoFlow = repository.getUserById(query)
            val userDataFlow = localRepository.userData
            getUserRepositories(query)

            combine(userInfoFlow, userDataFlow) { userInfo, userData ->
                val list = userData.map { it.id }
                userInfo.isFavorite = list.contains(userInfo.id)
                DetailUiState.Success(userInfo)
            }.collect {
                detailUiState.value = it
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