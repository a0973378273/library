package com.bean.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreDataSourceImpl(context: Context, name: String) : DataStoreDataSource {
    private val Context.dataStore by preferencesDataStore(name = name)
    private val dataStore = context.dataStore

    override suspend fun saveString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun readString(key: Preferences.Key<String>): Flow<String> =
        dataStore.data.map {
            it[key] ?: throw NullPointerException("No value found for key: $key")
        }

    override suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun readInt(key: Preferences.Key<Int>): Flow<Int> =
        dataStore.data.map {
            it[key] ?: throw NullPointerException("No value found for key: $key")
        }

    override suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun readBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> =
        dataStore.data.map {
            it[key] ?: throw NullPointerException("No value found for key: $key")
        }
}