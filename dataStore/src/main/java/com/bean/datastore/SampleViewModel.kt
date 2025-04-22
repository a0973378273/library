package com.bean.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SampleViewModel(private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    fun saveString(key: String, value: String) {
        viewModelScope.launch {
            dataStoreRepository.saveString(key, value)
        }
    }

    fun readString(key: String) {
        viewModelScope.launch {
            dataStoreRepository.readString(key).collect {
                println(it)
            }
        }
    }

    fun saveInt(key: String, value: Int) {
        viewModelScope.launch {
            dataStoreRepository.saveInt(key, value)
        }
    }

    fun readInt(key: String) {
        viewModelScope.launch {
            dataStoreRepository.readInt(key).collect {
                println(it)
            }
        }
    }

    fun saveBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveBoolean(key, value)
        }
    }

    fun readBoolean(key: String) {
        viewModelScope.launch {
            dataStoreRepository.readBoolean(key).collect {
                println(it)
            }
        }
    }
}