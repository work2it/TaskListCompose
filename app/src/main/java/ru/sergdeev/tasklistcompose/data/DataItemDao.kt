package ru.sergdeev.tasklistcompose.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.sergdeev.tasklist.data.DataItemDbModel

@Dao
interface DataItemDao {

    @Query("SELECT * FROM data_items ORDER BY id DESC")
    fun getDataList(): LiveData<List<DataItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDataItem(dataItemDbModel: DataItemDbModel)

    @Query("DELETE FROM data_items WHERE id=:dataItemId")
    suspend fun deleteDataItem(dataItemId: Int)

    @Query("SELECT * FROM data_items WHERE id=:dataItemId LIMIT 1")
    suspend fun getDataItem(dataItemId: Int): DataItemDbModel

}