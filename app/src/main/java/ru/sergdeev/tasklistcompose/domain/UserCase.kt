package ru.sergdeev.tasklistcompose.domain

import androidx.lifecycle.LiveData

class UserCase(private val dataItemRepository: DataItemRepository) {

    suspend fun addDataItem(dataItem: DataItem) {
        dataItemRepository.addDataItem(dataItem)
    }

    fun getItems(): LiveData<List<DataItem>> {
        return dataItemRepository.getDataList()
    }
    suspend fun deleteItem(dataItem: DataItem) {
        dataItemRepository.deleteDataItem(dataItem)
    }

    suspend fun editItem(dataItem: DataItem) {
        dataItemRepository.editDataItem(dataItem)
    }
}