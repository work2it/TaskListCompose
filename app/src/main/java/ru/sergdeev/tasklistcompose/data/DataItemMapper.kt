package ru.sergdeev.tasklistcompose.data

import ru.sergdeev.tasklist.data.DataItemDbModel
import ru.sergdeev.tasklistcompose.domain.DataItem

class DataItemMapper {

    fun mapEntityToDbModel(dataItem: DataItem) = DataItemDbModel(
        id = dataItem.id,
        title = dataItem.title,
        desc = dataItem.desc,
        dt = dataItem.dt,
        enabled = dataItem.enabled
    )

    fun mapDbModelToEntity(dataItemDbModel: DataItemDbModel) = DataItem(
        id = dataItemDbModel.id,
        title = dataItemDbModel.title,
        desc = dataItemDbModel.desc,
        dt = dataItemDbModel.dt,
        enabled = dataItemDbModel.enabled
    )

    fun mapListDbModelToListEntity(list: List<DataItemDbModel>?) = list?.map {
        mapDbModelToEntity(it)
    }
}