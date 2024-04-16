package com.example.test.data

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String
)
