package com.example.todojetpackcomposeapp.ui.screens.main


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todojetpackcomposeapp.data.models.Task
import com.example.todojetpackcomposeapp.ui.viewmodel.TodoViewModel
import com.example.todojetpackcomposeapp.utils.RequestState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel,
    navController: NavController
) {

    val allTasks by viewModel.allTasks.collectAsState()
    val allCategories by viewModel.allCategories.collectAsState()

    var allCheckedTasksCount = 0
    var allTasksCount = 0



    if (allTasks is RequestState.Success) {
        allCheckedTasksCount =
            (allTasks as RequestState.Success<List<Task>>).data.filter { it.checked }.size
        allTasksCount = (allTasks as RequestState.Success<List<Task>>).data.size
    }


    Column(
        modifier = modifier
            .background(color = Color(0xFFF8F7F3))
            .padding(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        ProgressSection(
            allTasksCount = allTasksCount,
            allCheckedTasksCount = allCheckedTasksCount,
            navigateToListScreen = {
                viewModel.searchField.value = ""
                viewModel.categoryTitle.value = "All categories"
                viewModel.resetTask()
                navController.navigate("list") {
                    popUpTo("main")
                    launchSingleTop = true
                }
            }
        )
        CategoriesSection(
            allCategories = allCategories,
            getCheckedTasksOfCategoryCount = { viewModel.getCheckedTasksOfCategoryCount(it) },
            getTasksOfCategoryCount = { viewModel.getTasksOfCategoryCount(it) },
            insertCategory = { viewModel.insertCategory(it) },
            updateCategory = { viewModel.updateCategory(it) },
            deleteCategory = { viewModel.deleteCategory(it) },
            navigateToCategoryTasks = { navController.navigate("list") },
            setCategoryId = { viewModel.taskCategoryId.value = it },
            setCategoryTitle = { viewModel.categoryTitle.value = it }
        )
    }
}