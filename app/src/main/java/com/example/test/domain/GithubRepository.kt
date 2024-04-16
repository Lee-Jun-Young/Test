package com.example.test.domain

import com.example.test.data.UserInfo
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun getUserInfo(): Flow<UserInfo>
}