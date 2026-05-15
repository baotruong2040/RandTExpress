package com.example.randtexpress.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

internal val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

data class SessionData(
    val token: String? = null,
    val userId: Int? = null,
    val role: String? = null
)

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: androidx.datastore.core.DataStore<Preferences>
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = intPreferencesKey("auth_user_id")
        val ROLE = stringPreferencesKey("auth_role")
    }

    val sessionData: Flow<SessionData> = dataStore.data.map { preferences ->
        SessionData(
            token = preferences[Keys.TOKEN],
            userId = preferences[Keys.USER_ID],
            role = preferences[Keys.ROLE]
        )
    }

    suspend fun saveSession(
        token: String,
        userId: Int,
        role: String
    ) {
        dataStore.edit { preferences ->
            preferences[Keys.TOKEN] = token
            preferences[Keys.USER_ID] = userId
            preferences[Keys.ROLE] = role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.TOKEN)
            preferences.remove(Keys.USER_ID)
            preferences.remove(Keys.ROLE)
        }
    }
}
