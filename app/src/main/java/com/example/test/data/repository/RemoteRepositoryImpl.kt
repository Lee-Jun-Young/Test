package com.example.test.data.repository

import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.SearchResponse
import com.example.test.data.dto.UserInfo
import com.example.test.data.network.GithubService
import com.example.test.domain.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val service: GithubService) :
    RemoteRepository {

    override val myData: Flow<UserInfo> = flow {
        emit(service.getUserInfo())
    }

    override suspend fun getSearchUser(query: String, page: Int): Flow<SearchResponse> {
        return try {
            val result = service.searchUser(query, page)
            flow {
                emit(result)
            }
        } catch (e: Exception) {
            flow {
                emit(SearchResponse(0, emptyList()))
            }
        }
    }

    override suspend fun getUserById(userId: String): Flow<UserInfo?> {
        return try {
            val result = service.getUserById(userId)
            flow {
                emit(result)
            }
        } catch (e: Exception) {
            flow {
                emit(null)
            }
        }
    }

    override suspend fun getUserRepositories(owner: String): Flow<List<RepositoryInfo>> {
        return try {
            val result = service.getRepos(owner)
            flow {
                emit(result)
            }
        } catch (e: Exception) {
            flow {
                emit(emptyList())
            }
        }
    }
}