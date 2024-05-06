package com.example.test.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.UserInfo
import com.example.test.domain.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RemoteRepository
) : ViewModel() {

    val myRepositoriesUiState = MutableStateFlow<MyRepositoryState>(MyRepositoryState.Loading)

    val myDataState: StateFlow<MyDataState> = repository.myData.map {
        getUserRepositories(it.login)
        MyDataState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyDataState.Loading
    )

    private fun getUserRepositories(owner: String) {
        viewModelScope.launch {
            repository.getUserRepositories(owner).collectLatest { repositories ->
                myRepositoriesUiState.value = MyRepositoryState.Success(repositories)
            }
        }
    }
}

sealed interface MyDataState {
    data object Loading : MyDataState

    data class Success(
        val item: UserInfo,
    ) : MyDataState
}

sealed interface MyRepositoryState {
    data object Loading : MyRepositoryState

    data class Success(
        val item: List<RepositoryInfo>,
    ) : MyRepositoryState
}