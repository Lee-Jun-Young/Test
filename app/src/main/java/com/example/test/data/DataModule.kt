package com.example.test.data

import com.example.test.domain.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideSearchRepository(service: GithubService): GithubRepository =
        GithubRepositoryImpl(service)
}