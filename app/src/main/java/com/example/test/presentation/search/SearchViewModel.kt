package com.example.test.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.dto.UserInfo
import com.example.test.domain.LocalRepository
import com.example.test.domain.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    val searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Init)

    private val bookmarksIdList: Flow<List<String>> = localRepository.userData.map {
        it.map { it.login }
    }

    init {
        combine(bookmarksIdList, searchUiState) { favoriteList, item ->
            if (item is SearchUiState.Success) {
                searchUiState.value = SearchUiState.Success(item.item.map {
                    if (favoriteList.contains(it.login)) {
                        it.copy(isFavorite = true)
                    } else {
                        it.copy(isFavorite = false)
                    }
                })
            }
        }.launchIn(viewModelScope)
    }

    private var itemsCount = 0
    private var page = 1

    fun searchUser(query: String) {
        viewModelScope.launch {
            searchUiState.value = SearchUiState.Loading
            repository.getSearchUser(query, page).collectLatest {
                itemsCount = it.totalCount
                searchUiState.value =
                    SearchUiState.Success(it.items)
            }
        }
    }

    fun loadMore(query: String) {
        viewModelScope.launch {
            if (itemsCount > page * 30) {
                page++
                repository.getSearchUser(query, page).collectLatest {
                    searchUiState.value =
                        SearchUiState.Success((searchUiState.value as SearchUiState.Success).item + it.items)
                }
            }
        }
    }
}

sealed interface SearchUiState {
    data object Init : SearchUiState
    data object Loading : SearchUiState

    data class Success(
        val item: List<UserInfo>,
    ) : SearchUiState
}
