package com.esaudev.shopapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val USER_TOKEN_KEY = "token"
const val PREFERENCES_NAME = "preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreSource @Inject constructor(
    @ApplicationContext context: Context
) {

    private object PreferencesKey {
        val userToken = stringPreferencesKey(USER_TOKEN_KEY)
    }

    private val dataStore = context.dataStore

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userToken] = token
        }
    }

    suspend fun getUserToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userToken = preferences[PreferencesKey.userToken]?: ""
                userToken
            }
    }
}