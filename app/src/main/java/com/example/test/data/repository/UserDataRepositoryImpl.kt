package com.example.test.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.test.domain.UserDataRepository
import com.example.test.presentation.setting.AppLanguageConfig
import com.example.test.presentation.setting.DarkThemeConfig
import com.example.test.presentation.setting.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userDataStore: DataStore<Preferences>
) : UserDataRepository {
    override val userData: Flow<UserData> = userDataStore.data.map {
        UserData(
            fcmToken = it[FCM_TOKEN] ?: "",
            darkThemeConfig = DarkThemeConfig.valueOf(
                it[APP_THEME] ?: DarkThemeConfig.FOLLOW_SYSTEM.name
            ),
            language = AppLanguageConfig.valueOf(
                it[APP_LANGUAGE] ?: AppLanguageConfig.ENGLISH.name
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

    override suspend fun setFcmToken(fcmToken: String) {
        Result.runCatching {
            userDataStore.edit { preferences ->
                preferences[FCM_TOKEN] = fcmToken
            }
        }
    }

    override suspend fun setAppLanguage(language: AppLanguageConfig) {
        Result.runCatching {
            userDataStore.edit { preferences ->
                preferences[APP_LANGUAGE] = language.name
            }
        }
    }

    private companion object {
        val APP_THEME = stringPreferencesKey(
            name = "app_theme"
        )
        val FCM_TOKEN = stringPreferencesKey(
            name = "fcm_token"
        )
        val APP_LANGUAGE = stringPreferencesKey(
            name = "app_language"
        )
    }
}