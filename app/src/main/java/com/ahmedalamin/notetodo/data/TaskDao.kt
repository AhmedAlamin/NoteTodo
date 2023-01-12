package com.ahmedalamin.notetodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }

    @Query("select * from task_table where (completed != :hideCompleted or completed = 0) and name like '%' || :searchQuery || '%' order by important desc , name")
    fun getTasksSortedByName(searchQuery: String , hideCompleted:Boolean): Flow<List<Task>>

    @Query("select * from task_table where (completed != :hideCompleted or completed = 0) and name like '%' || :searchQuery || '%' order by important desc , created")
    fun getTasksSortedByDateCreated(searchQuery: String , hideCompleted:Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


}


