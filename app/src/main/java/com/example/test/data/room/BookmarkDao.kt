package com.example.test.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.data.dto.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(data: UserInfo)

    @Query("SELECT * FROM BOOKMARKS")
    fun getFavoriteAll(): Flow<List<UserInfo>>

    @Delete
    suspend fun deleteFavorite(data: UserInfo)
}