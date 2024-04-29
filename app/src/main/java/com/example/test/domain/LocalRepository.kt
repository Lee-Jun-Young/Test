package com.example.test.domain

import com.example.test.data.dto.UserInfo
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getFavoriteList(): Flow<List<UserInfo>>
    suspend fun postFavorite(data: UserInfo)
    suspend fun deleteFavorite(data: UserInfo)

}