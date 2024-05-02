package com.example.test.data.network

import com.example.test.data.dto.OrganizationDto
import com.example.test.data.dto.RepositoryInfo
import com.example.test.data.dto.SearchResponse
import com.example.test.data.dto.UserInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("user")
    suspend fun getUserInfo(): UserInfo

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int
    ): SearchResponse

    @GET("users/{user}")
    suspend fun getUserById(@Path("user") user: String): UserInfo

    @GET("users/{owner}/repos")
    suspend fun getRepos(@Path("owner") owner: String): List<RepositoryInfo>

    @GET("user/orgs")
    suspend fun getOrgs(): List<OrganizationDto>
}