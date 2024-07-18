package com.example.test.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test.data.dto.UserInfo
import com.example.test.data.room.BookmarkDao

@Database(entities = [UserInfo::class], version = 2)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}