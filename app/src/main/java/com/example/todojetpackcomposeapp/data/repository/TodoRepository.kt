package com.example.todojetpackcomposeapp.data.repository


import com.example.todojetpackcomposeapp.data.dao.TodoDao
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.data.models.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TodoRepository (
    private val todoDao: TodoDao
) {

    val getAllTasks: Flow<List<Task>> = todoDao.getAllTasks()
    fun getCheckedTasksOfCategoryCount(id: Int): Flow<Int> =
        todoDao.getCheckedTasksOfCategoryCount(id = id)

    fun getTasksOfCategoryCount(id: Int): Flow<Int> = todoDao.getTasksOfCategoryCount(id = id)

    suspend fun insertTask(task: Task) = todoDao.insertTask(task = task)
    suspend fun updateTask(task: Task) = todoDao.updateTask(task = task)
    suspend fun deleteTask(task: Task) = todoDao.deleteTask(task = task)

    val getAllCategories: Flow<List<Category>> = todoDao.getAllCategories()
    suspend fun insertCategory(category: Category) = todoDao.insertCategory(category = category)
    suspend fun updateCategory(category: Category) = todoDao.updateCategory(category = category)
    suspend fun deleteCategory(category: Category) = todoDao.deleteCategory(category = category)

}

