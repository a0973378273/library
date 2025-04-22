package com.bean.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreRepositoryImpl(private val dataStoreDataSource: DataStoreDataSource) : DataStoreRepository {
    
    override suspend fun saveString(key: String, value: String) = dataStoreDataSource.saveString(stringPreferencesKey(key), value)

    override fun readString(key: String) = dataStoreDataSource.readString(stringPreferencesKey(key))

    override suspend fun saveInt(key: String, value: Int) = dataStoreDataSource.saveInt(intPreferencesKey(key), value)

    override fun readInt(key: String) = dataStoreDataSource.readInt(intPreferencesKey(key))

    override suspend fun saveBoolean(key: String, value: Boolean) = dataStoreDataSource.saveBoolean(booleanPreferencesKey(key), value)

    override fun readBoolean(key: String) = dataStoreDataSource.readBoolean(booleanPreferencesKey(key))
}