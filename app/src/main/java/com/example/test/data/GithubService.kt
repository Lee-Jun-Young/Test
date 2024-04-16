package com.example.test.data

import retrofit2.http.GET

interface GithubService {

    @GET("user")
    suspend fun getUserInfo(): UserInfo
}