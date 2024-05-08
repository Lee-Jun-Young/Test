package com.example.test.data.repository

import com.example.test.data.dto.UserInfo
import com.example.test.data.room.BookmarkDao
import com.example.test.domain.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : LocalRepository {

    override val userData: Flow<List<UserInfo>> = bookmarkDao.getFavoriteAll()

    override suspend fun postBookmark(data: UserInfo) {
        bookmarkDao.addFavorite(data)
    }

    override suspend fun deleteBookmark(data: UserInfo) {
        bookmarkDao.deleteFavorite(data)
    }
}