package com.example.test.data

import android.content.Context
import androidx.room.Room
import com.example.test.data.repository.TestAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Singleton
    @Provides
    fun provideTestAppDatabase(
        @ApplicationContext context: Context
    ): TestAppDatabase = Room
        .databaseBuilder(context, TestAppDatabase::class.java, "test.db")
        .build()
}