package com.example.test.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.test.presentation.setting.UserData
import com.example.test.domain.UserDataRepository
import com.example.test.presentation.setting.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userDataStore: DataStore<Preferences>
) : UserDataRepository {
    override val userData: Flow<UserData> = userDataStore.data.map {
        UserData(
            darkThemeConfig = DarkThemeConfig.valueOf(
                it[APP_THEME] ?: DarkThemeConfig.FOLLOW_SYSTEM.name
            )
        )
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        Result.runCatching {
            userDataStore.edit { preferences ->
                preferences[APP_THEME] = darkThemeConfig.name
            }
        }
    }

    private companion object {
        val APP_THEME = stringPreferencesKey(
            name = "app_theme"
        )
    }
}