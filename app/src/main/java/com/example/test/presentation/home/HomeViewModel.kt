package com.example.test.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.domain.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _repositories = MutableStateFlow<List<RepositoryInfo>?>(null)
    val repositories: StateFlow<List<RepositoryInfo>?> = _repositories.asStateFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo().collectLatest { userInfo ->
                _userInfo.value = userInfo
                getUserRepositories(userInfo.login)
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