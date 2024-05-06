package com.example.test.domain

import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.SearchResponse
import com.example.test.data.dto.UserInfo
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    val myData: Flow<UserInfo>
    
    suspend fun getSearchUser(query: String, page: Int = 1): Flow<SearchResponse>

    suspend fun getUserById(userId: String): Flow<UserInfo>

    suspend fun getUserRepositories(owner: String): Flow<List<RepositoryInfo>>
}