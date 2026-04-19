package ru.sergdeev.tasklistcompose.domain

import androidx.lifecycle.LiveData

interface DataItemRepository {

    suspend fun addDataItem(dataItem: DataItem)

    suspend fun deleteDataItem(dataItem: DataItem)

    suspend fun editDataItem(dataItem: DataItem)

    suspend fun getDataItem(dataItemId: Int): DataItem

    fun getDataList(): LiveData<List<DataItem>>
}