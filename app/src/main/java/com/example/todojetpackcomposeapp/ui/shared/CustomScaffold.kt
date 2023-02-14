package com.example.todojetpackcomposeapp.ui.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todojetpackcomposeapp.ui.screens.addTask.AddTaskScreen
import com.example.todojetpackcomposeapp.ui.screens.main.MainScreen
import com.example.todojetpackcomposeapp.ui.screens.tasksList.TasksListScreen
import com.example.todojetpackcomposeapp.ui.viewmodel.TodoViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel,
    content: @Composable (Modifier) -> Unit = {}
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    var bottomBarItemsEnable by rememberSaveable { mutableStateOf(true) }

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "addTask" -> false
        else -> true
    }

    androidx.compose.material.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (showBottomBar) {
                AddTodoFloatingActionButton(
                    navController = navController,
                    resetTaskId = { viewModel.taskId.value = -1 }
                )
            }
        },
        floatingActionButtonPosition = androidx.compose.material.FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            if (showBottomBar) {
                androidx.compose.material.BottomAppBar(
                    backgroundColor = Color(0xFFFDFFE7),
                    cutoutShape = MaterialTheme.shapes.small.copy(
                        CornerSize(percent = 50)
                    )
                ) {
                    BottomNavigation(backgroundColor = Color(0xFFFDFFE7)) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            BottomNavigationItem(
                                modifier = modifier.padding(end =35.dp),
                                selected = true,
                                onClick = {
                                    bottomBarItemsEnable = true
                                    navController.navigate("main") {
                                        launchSingleTop = true
                                    }
                                    viewModel.resetTask()
                                    viewModel.searchField.value = ""

                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.DateRange,
                                        contentDescription = "Category",
                                        tint = if(bottomBarItemsEnable) Color.Black else Color.Gray
                                    )
                                }
                            )
                            BottomNavigationItem(
                                modifier = modifier.padding(start =35.dp),
                                selected = false,
                                onClick = {
                                    bottomBarItemsEnable = false
                                    viewModel.searchField.value = ""
                                    viewModel.resetTask()
                                    viewModel.categoryTitle.value = "All categories"
                                    navController.navigate("list") {
                                        popUpTo("main")
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.List,
                                        contentDescription = "List",
                                        tint = if(bottomBarItemsEnable) Color.Gray else Color.Black
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        content(Modifier.padding(it))
        NavHost(navController = navController, startDestination = "main") {
            composable("main") { MainScreen(viewModel = viewModel, navController = navController) }
            composable("list") {
                TasksListScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("addTask") {
                AddTaskScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun AddTodoFloatingActionButton(
    modifier: Modifier = Modifier,
    navController: NavController,
    resetTaskId: () -> Unit
) {

    FloatingActionButton(
        onClick = {
            resetTaskId()
            navController.navigate("addTask")
        },
        containerColor = Color.Yellow,
        shape = RoundedCornerShape(360.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add Task",
            modifier = modifier.size(35.dp),
            tint = Color(0xFF1E1D1A)
        )
    }
}

