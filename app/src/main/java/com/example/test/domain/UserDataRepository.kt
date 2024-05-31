package com.example.test.domain

import com.example.test.presentation.setting.DarkThemeConfig
import com.example.test.presentation.setting.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
    suspend fun setFcmToken(fcmToken: String)
}