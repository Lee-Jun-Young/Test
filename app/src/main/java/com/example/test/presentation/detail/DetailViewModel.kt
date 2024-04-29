package com.example.test.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.RepositoryInfo
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
class DetailViewModel @Inject constructor(
    private val repository: GithubRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _detailInfo = MutableStateFlow<UserInfo?>(null)
    val detailInfo: StateFlow<UserInfo?> = _detailInfo.asStateFlow()

    private val _repositories = MutableStateFlow<List<RepositoryInfo>?>(null)
    val repositories: StateFlow<List<RepositoryInfo>?> = _repositories.asStateFlow()

    fun getUserById(query: String) {
        viewModelScope.launch {
            repository.getUserById(query).collectLatest {
                _detailInfo.value = it
                getUserRepositories(it.login)
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
                _repositories.value = repositories
            }
        }
    }
}