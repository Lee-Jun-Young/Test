package com.example.test.data.dto

import com.google.gson.annotations.SerializedName

data class RepositoryInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("private")
    val private: Boolean,
    @SerializedName("description")
    val description: String?,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int
)
