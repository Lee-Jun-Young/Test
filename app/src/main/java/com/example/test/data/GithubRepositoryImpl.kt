package com.example.test.data

import com.example.test.domain.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val service: GithubService) :
    GithubRepository {
    override suspend fun getUserInfo(): Flow<UserInfo> {
        return flow {
            val temp = service.getUserInfo()
            emit(temp)
        }
    }
}