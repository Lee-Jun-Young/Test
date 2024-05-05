package com.example.test.data.repository

import com.example.test.data.room.BookmarkDao
import com.example.test.data.dto.UserInfo
import com.example.test.domain.LocalRepository
import com.example.test.domain.UserData
import com.example.test.presentation.setting.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : LocalRepository {

    override val userData: Flow<List<UserInfo>> = flow {
        val temp = bookmarkDao.getFavoriteAll()
        temp.forEach {
            it.isFavorite = true
        }
        emit(temp)
    }

    override suspend fun postFavorite(data: UserInfo) {
        bookmarkDao.addFavorite(data)
    }

    override suspend fun deleteFavorite(data: UserInfo) {
        bookmarkDao.deleteFavorite(data)
    }
}