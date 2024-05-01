package com.example.test.data.repository

import com.example.test.domain.UserData
import com.example.test.domain.UserDataRepository
import com.example.test.presentation.setting.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
) : UserDataRepository {
    override val userData: Flow<UserData> =
        flow { emit(UserData(darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM)) }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userData.map { it.copy(darkThemeConfig = darkThemeConfig) }
    }

}