package com.example.todojetpackcomposeapp.ui.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.data.models.Task
import com.example.todojetpackcomposeapp.data.repository.TodoRepository
import com.example.todojetpackcomposeapp.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import java.util.*
import javax.inject.Inject


interface TodoViewModelAbstract {
    //tasks methods
    fun getAllTasks()
    fun insertTask()
    fun updateTask(task: Task)
    fun deleteTask(task: Task)

    //categories methods
    fun getCheckedTasksOfCategoryCount(id: Int): Flow<Int>
    fun getTasksOfCategoryCount(id: Int): Flow<Int>
    fun insertCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(category: Category)
}

@HiltViewModel
class TodoViewModel
@Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel(), TodoViewModelAbstract {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    var searchField: MutableState<String> = mutableStateOf("")
    var categoryTitle: MutableState<String> = mutableStateOf("")
    var taskId: MutableState<Int> = mutableStateOf(-1)
    var taskCategoryId: MutableState<Int> = mutableStateOf(-1)
    var taskTitle: MutableState<String> = mutableStateOf("")
    var desc: MutableState<String> = mutableStateOf("")
    private var checked: MutableState<Boolean> = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    var date: MutableState<LocalDate> = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    var time: MutableState<LocalTime> = mutableStateOf(LocalTime.now())


    private val _allTasks = MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<Task>>> = _allTasks

    private val _allCategories = MutableStateFlow<RequestState<List<Category>>>(RequestState.Idle)
    val allCategories: StateFlow<RequestState<List<Category>>> = _allCategories


    init {
        getAllTasks()
        getAllCategories()
    }


    //tasks method
    override fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                todoRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSelectedTask(task: Task) {
        if (task != null) {
            taskId.value = task.todoId
            taskCategoryId.value = task.taskCategoryId
            taskTitle.value = task.title
            desc.value = task.desc
            checked.value = task.checked
            date.value = task.date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()
            time.value = task.date.toInstant().atZone(ZoneId.of("UTC")).toLocalTime()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun insertTask() {
        ioScope.launch {

            val dateTime: LocalDateTime = date.value.atTime(time.value)
            val instant = dateTime.toInstant(ZoneOffset.UTC)

            val task = if (taskId.value == -1) {
                Task(
                    taskCategoryId = taskCategoryId.value,
                    title = taskTitle.value,
                    desc = desc.value,
                    checked = checked.value,
                    date = Date.from(instant)
                )
            } else {
                Task(
                    todoId = taskId.value,
                    taskCategoryId = taskCategoryId.value,
                    title = taskTitle.value,
                    desc = desc.value,
                    checked = checked.value,
                    date = Date.from(instant)
                )
            }
            todoRepository.insertTask(task = task)
            if (taskCategoryId.value != -1) {
                resetTaskWithoutCategory()
            } else {
                resetTask()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetTask() {
        taskId.value = -1
        taskCategoryId.value = -1
        taskTitle.value = ""
        desc.value = ""
        checked.value = false
        date.value = LocalDate.now()
        time.value = LocalTime.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetTaskWithoutCategory() {
        taskId.value = -1
        taskTitle.value = ""
        desc.value = ""
        checked.value = false
        date.value = LocalDate.now()
        time.value = LocalTime.now()
    }

    override fun updateTask(task: Task) {
        ioScope.launch {
            todoRepository.updateTask(task = task)
        }
    }

    override fun deleteTask(task: Task) {
        ioScope.launch {
            todoRepository.deleteTask(task = task)
        }
    }

    //categories method
    private fun getAllCategories() {
        _allCategories.value = RequestState.Loading
        try {
            viewModelScope.launch {
                todoRepository.getAllCategories.collect {
                    _allCategories.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allCategories.value = RequestState.Error(e)
        }
    }

    override fun insertCategory(category: Category) {
        ioScope.launch {
            todoRepository.insertCategory(category = category)
        }
    }

    override fun getCheckedTasksOfCategoryCount(id: Int): Flow<Int> =
        todoRepository.getCheckedTasksOfCategoryCount(id)

    override fun getTasksOfCategoryCount(id: Int): Flow<Int> =
        todoRepository.getTasksOfCategoryCount(id)

    override fun updateCategory(category: Category) {
        ioScope.launch {
            todoRepository.updateCategory(category = category)
        }
    }

    override fun deleteCategory(category: Category) {
        ioScope.launch {
            todoRepository.deleteCategory(category = category)
        }
    }

}

