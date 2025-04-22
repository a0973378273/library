package com.bean.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * DataStoreDataSource is an interface that defines methods for saving and reading
 * @sample App
 */
interface DataStoreDataSource {

    /**
     * Save a string value to the DataStore.
     *
     * @sample DataStoreRepositoryImpl.saveString
     */
    suspend fun saveString(key: Preferences.Key<String>, value: String)

    /**
     * Read a string value from the DataStore.
     *
     * @sample DataStoreRepositoryImpl.readString
     */
    fun readString(key: Preferences.Key<String>): Flow<String>

    /**
     * Save an integer value to the DataStore.
     *
     * @sample DataStoreRepositoryImpl.saveInt
     */
    suspend fun saveInt(key: Preferences.Key<Int>, value: Int)

    /**
     * Read an integer value from the DataStore.
     *
     * @sample DataStoreRepositoryImpl.readInt
     */
    fun readInt(key: Preferences.Key<Int>): Flow<Int>

    /**
     * Save a boolean value to the DataStore.
     *
     * @sample DataStoreRepositoryImpl.saveBoolean
     */
    suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean)

    /**
     * Read a boolean value from the DataStore.
     *
     * @sample DataStoreRepositoryImpl.readBoolean
     */
    fun readBoolean(key: Preferences.Key<Boolean>): Flow<Boolean>
}