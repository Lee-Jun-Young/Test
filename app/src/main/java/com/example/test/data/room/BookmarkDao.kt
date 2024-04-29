package com.example.test.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.data.dto.UserInfo

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(data: UserInfo)

    @Query("SELECT * FROM BOOKMARKS")
    suspend fun getFavoriteAll(): List<UserInfo>

    @Delete
    suspend fun deleteFavorite(data: UserInfo)
}