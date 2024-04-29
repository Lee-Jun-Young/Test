package com.example.test.data.repository

import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.network.GithubService
import com.example.test.data.dto.SearchResponse
import com.example.test.data.dto.UserInfo
import com.example.test.domain.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val service: GithubService) :
    GithubRepository {
    override suspend fun getUserInfo(): Flow<UserInfo> {
        return flow {
            emit(service.getUserInfo())
        }
    }

    override suspend fun getSearchUser(query: String): Flow<SearchResponse> {
        return flow {
            val temp = service.searchUser(query)
            emit(temp)
        }
    }

    override suspend fun getUserById(userId: String): Flow<UserInfo> {
        return flow {
            val temp = service.getUserById(userId)
            emit(temp)
        }
    }

    override suspend fun getUserRepositories(owner: String): Flow<List<RepositoryInfo>> {
        return flow {
            emit(service.getRepos(owner))
        }
    }
}