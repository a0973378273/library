package com.bean.datastore

import kotlinx.coroutines.flow.Flow

/**
 * DataStoreRepository is an interface that defines methods for saving and reading
 * @sample App
 */
interface DataStoreRepository {

    /**
     * Save a string value to the DataStore.
     *
     * @sample SampleViewModel.saveString
     */
    suspend fun saveString(key: String, value: String)

    /**
     * Read a string value from the DataStore.
     *
     * @sample SampleViewModel.readString
     */
    fun readString(key: String): Flow<String>

    /**
     * Save an integer value to the DataStore.
     *
     * @sample SampleViewModel.saveInt
     */
    suspend fun saveInt(key: String, value: Int)

    /**
     * Read an integer value from the DataStore.
     *
     * @sample SampleViewModel.readInt
     */
    fun readInt(key: String): Flow<Int>

    /**
     * Save a boolean value to the DataStore.
     *
     * @sample SampleViewModel.saveBoolean
     */
    suspend fun saveBoolean(key: String, value: Boolean)

    /**
     * Read a boolean value from the DataStore.
     *
     * @sample SampleViewModel.readBoolean
     */
    fun readBoolean(key: String): Flow<Boolean>
}