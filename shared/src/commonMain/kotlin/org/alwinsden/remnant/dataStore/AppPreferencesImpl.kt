package org.alwinsden.remnant.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface AppPreferences {
    suspend fun doesAuthKeyExist(): String
    suspend fun addUpdateAuthKey(jwtToken: String): Preferences
}

internal class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {
    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val TOKEN_VALUE_GENERATOR = "prefsString"
    }

    //INFO: stringPreferencesKey represents the kind of value that needs to be stored in the Datastore.
    private val authTokenKey = stringPreferencesKey("$PREFS_TAG_KEY$TOKEN_VALUE_GENERATOR")
    override suspend fun doesAuthKeyExist() = dataStore.data.map { preferences ->
        preferences[authTokenKey] ?: ""
    }.first()

    override suspend fun addUpdateAuthKey(jwtToken: String) = dataStore.edit { preferences ->
        preferences[authTokenKey] = jwtToken
    }
}