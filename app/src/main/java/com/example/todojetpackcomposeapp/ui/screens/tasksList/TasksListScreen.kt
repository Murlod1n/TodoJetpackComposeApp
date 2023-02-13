package com.example.todojetpackcomposeapp.ui.screens.tasksList

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.data.models.Task
import com.example.todojetpackcomposeapp.ui.viewmodel.TodoViewModel
import com.example.todojetpackcomposeapp.utils.RequestState
import java.time.LocalDate
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksListScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel,
    navController: NavController,
) {
    BackHandler {
        viewModel.searchField.value = ""
        viewModel.resetTask()
        navController.navigate("main") {
            popUpTo(0)
        }
    }

    val tasksList by viewModel.allTasks.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()
    var allTasksList by remember { mutableStateOf(listOf<Task>()) }

    var title by remember { mutableStateOf("All categories") }
    var isCheckedFilter: Boolean? by remember { mutableStateOf(null) }
    var titleColor by remember { mutableStateOf(Color.Black) }
    var animationVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { animationVisible = true }

    fun getTaskColor(id: Int): String {
        return if (allCategories is RequestState.Success) {
            val color = (allCategories as RequestState.Success<List<Category>>).data
                .find { it.categoryId == id }?.color
            color ?: "1/1/1/1"
        } else {
            "1/1/1/1"
        }
    }

    fun setCategoryTitle(id: Int) {
        viewModel.categoryTitle.value = if (allCategories is RequestState.Success) {
            val categoryItem = (allCategories as RequestState.Success<List<Category>>).data
                .find { it.categoryId == id }!!
            categoryItem.title
        } else {
            "All categories"
        }
    }



    allTasksList =
        if (tasksList is RequestState.Success) {
            if (viewModel.taskCategoryId.value > -1) {
                title = viewModel.categoryTitle.value
                val colorRGB =
                    getTaskColor(viewModel.taskCategoryId.value).split("/").map { it.toFloat() }
                titleColor =
                    Color(
                        red = colorRGB[0],
                        green = colorRGB[1],
                        blue = colorRGB[2],
                        alpha = colorRGB[3]
                    )
                if (viewModel.searchField.value != "") {
                    (tasksList as RequestState.Success<List<Task>>).data
                        .filter {
                            it.taskCategoryId == viewModel.taskCategoryId.value &&
                                    it.title.lowercase()
                                        .contains(viewModel.searchField.value.lowercase())
                        }
                } else {
                    (tasksList as RequestState.Success<List<Task>>).data
                        .filter { it.taskCategoryId == viewModel.taskCategoryId.value }
                }
            } else {
                if (viewModel.searchField.value != "") {
                    (tasksList as RequestState.Success<List<Task>>).data
                        .filter {
                            it.title.lowercase().contains(viewModel.searchField.value.lowercase())
                        }
                } else {
                    (tasksList as RequestState.Success<List<Task>>).data
                }
            }
        } else {
            listOf()
        }

    allTasksList = when (isCheckedFilter) {
        true -> allTasksList.filter { it.checked }
        false -> allTasksList.filter { !it.checked }
        else -> allTasksList
    }

    val todayTasks: List<Task> = allTasksList.filter {
        val date = it.date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()
        val nowDate = LocalDate.now()
        date.equals(nowDate)
    }

    val tomorrowTasks: List<Task> = allTasksList.filter {
        val date = it.date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()
        val nowDate = LocalDate.now().plusDays(1)
        date.equals(nowDate)
    }

    val allTasks =
        (allTasksList.toSet().subtract(todayTasks.toSet()).subtract(tomorrowTasks.toSet())).toList()

    val daysTitleList = listOf("Now", "Tomorrow", "All tasks")

    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        SearchSection(
            searchField = viewModel.searchField.value,
            searchFieldEditor = { viewModel.searchField.value = it },
            isCheckedFilter = isCheckedFilter,
            changeCheckedFilter = { isCheckedFilter = it }
        )
        Column(
            modifier = modifier.padding(bottom = 20.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
            Canvas(
                modifier = modifier
                    .padding(top = 10.dp)
                    .height(2.dp)
                    .fillMaxWidth(),
                onDraw = { drawRect(color = titleColor) }
            )
        }

        for (index in daysTitleList.indices) {
            val itemsList: List<Task> = when (index) {
                0 -> todayTasks
                1 -> tomorrowTasks
                else -> allTasks
            }

            DaySection(
                tasksList = itemsList,
                sectionName = daysTitleList[index],
                tasksDate = LocalDate.now().plusDays(index.toLong()),
                navController = navController,
                getSelectedTask = { viewModel.getSelectedTask(it) },
                setDate = { viewModel.date.value = it },
                getTaskColor = { getTaskColor(it) },
                deleteTask = { viewModel.deleteTask(it) },
                updateTask = { viewModel.updateTask(it) },
                resetSearchField = { viewModel.searchField.value = "" },
                setCategoryTitle = { setCategoryTitle(it) },
                animationVisible = animationVisible
            )
        }
    }
}