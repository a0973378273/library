package com.dexatek.bluetooth.tool

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


/**
 * Define the status at the data layer
 */
sealed interface DataStatus<out R> {
    /**
     * Get data success
     */
    data class Success<out T>(val data: T) : DataStatus<T>

    /**
     * Get data fail
     */
    data class Failed(val exception: Throwable) : DataStatus<Nothing>
}

/**
 * Get data status in suspend
 *
 * ```
 * example:
 * fun getXXX(): Flow<DataStatus<XXX>> = getDataStatusInSuspend {
 *         XXXDataSource.getXXX()
 * }
 * ```
 */
fun <T> getDataStatusInSuspend(action: suspend () -> T): Flow<DataStatus<T>> = flow {
    runCatching { action.invoke() }
        .onSuccess { emit(DataStatus.Success(it)) }
        .onFailure { emit(DataStatus.Failed(it)) }
}.flowOn(Dispatchers.IO)

/**
 * Get data status
 *
 * ```
 * example:
 * fun getXXX(): Flow<DataStatus<XXX>> = getDataStatus {
 *         XXXDataSource.getXXX()
 * }
 * ```
 */
fun <T> getDataStatus(action: () -> T): Flow<DataStatus<T>> = flow {
    runCatching { action.invoke() }
        .onSuccess { emit(DataStatus.Success(it)) }
        .onFailure { emit(DataStatus.Failed(it)) }
}.flowOn(Dispatchers.IO)

/**
 * Get data status by flow
 *
 * ```
 * example:
 * fun getXXX(): Flow<DataStatus<XXX>> = getDataStatusByFlow {
 *         XXXDataSource.getXXX()
 * }
 * ```
 */
fun <T> getDataStatusByFlow(action: () -> Flow<T>): Flow<DataStatus<T>> = action()
    .map<T, DataStatus<T>> { DataStatus.Success(it) } // 將數據封裝為成功狀態
    .catch { emit(DataStatus.Failed(it)) } // 發生異常時發送 Failed 狀態
    .flowOn(Dispatchers.IO) // 指定執行線程

