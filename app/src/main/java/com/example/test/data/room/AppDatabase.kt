package com.example.test.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test.data.dto.UserInfo

@Database(entities = [UserInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}