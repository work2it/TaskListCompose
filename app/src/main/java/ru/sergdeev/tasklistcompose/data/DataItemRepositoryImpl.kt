package ru.sergdeev.tasklistcompose.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.sergdeev.tasklistcompose.domain.DataItem
import ru.sergdeev.tasklistcompose.domain.DataItemRepository

class DataItemRepositoryImpl(
    context: Context
) : DataItemRepository {

    private var dataItemDao = AppDatabase.getInstance(context).dataItemDao()
    private val mapper = DataItemMapper()
/*
    init {
        val database = AppDatabase.getInstance(context)
        dataItemDao = database.dataItemDao()
    }
*/
        override suspend fun addDataItem(dataItem: DataItem) {
            dataItemDao.addDataItem(
                mapper.mapEntityToDbModel(dataItem)
            )
        }

        override suspend fun deleteDataItem(dataItem: DataItem) {
            dataItemDao.deleteDataItem(dataItem.id)
        }

        override suspend fun editDataItem(dataItem: DataItem) {
            dataItemDao.addDataItem(
                mapper.mapEntityToDbModel(dataItem)
            )
        }

        override suspend fun getDataItem(dataItemId: Int): DataItem {
            val dbModel = dataItemDao.getDataItem(dataItemId)
            return mapper.mapDbModelToEntity(dbModel)
        }

        override fun getDataList(): LiveData<List<DataItem>> {
            return dataItemDao.getDataList().map {
                mapper.mapListDbModelToListEntity(it) as List<DataItem>
            }
        }
}