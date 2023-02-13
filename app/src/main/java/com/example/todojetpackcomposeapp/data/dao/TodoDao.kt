package com.example.todojetpackcomposeapp.data.dao

import androidx.room.*
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.data.models.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {

    //tasks list Query
    @Query("SELECT * FROM tasks")
    fun getAllTasks() : Flow<List<Task>>

    @Query("SELECT Count(*) FROM tasks WHERE checked = 1")
    fun getCheckedTasksCount() : Flow<Int>

    @Query("SELECT Count(*) FROM tasks WHERE task_category_id = :id AND checked = 1")
    fun getCheckedTasksOfCategoryCount(id: Int) : Flow<Int>

    @Query("SELECT Count(*) FROM tasks WHERE task_category_id = :id")
    fun getTasksOfCategoryCount(id: Int) : Flow<Int>

    @Query("SELECT color FROM categories WHERE category_id = :id")
    fun getTaskColor(id: Int): Flow<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)


    //Category Query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE category_id = :id")
    fun getCategory(id: Int): Flow<Category>
}