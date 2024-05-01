package com.example.test.domain

import com.example.test.presentation.setting.DarkThemeConfig
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}