package com.example.test.data.di

import android.content.Context
import androidx.room.Room
import com.example.test.data.room.AppDatabase
import com.example.test.data.room.BookmarkDao
import com.example.test.data.network.GithubService
import com.example.test.data.repository.GithubRepositoryImpl
import com.example.test.data.repository.LocalRepositoryImpl
import com.example.test.domain.GithubRepository
import com.example.test.domain.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideSearchRepository(service: GithubService): GithubRepository =
        GithubRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideLocalRepository(bookmarkDao: BookmarkDao): LocalRepository =
        LocalRepositoryImpl(bookmarkDao)

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "local.db")
        .build()

    @Singleton
    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): BookmarkDao = appDatabase.bookmarkDao()
}