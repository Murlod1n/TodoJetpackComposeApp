package com.example.todojetpackcomposeapp.ui.screens.tasksList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todojetpackcomposeapp.data.models.Task
import java.time.LocalDate
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySection(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    sectionName: String,
    tasksDate: LocalDate,
    navController: NavController,
    getSelectedTask: (Task) -> Unit,
    setDate: (LocalDate) -> Unit,
    getTaskColor: (Int) -> String,
    deleteTask: (Task) -> Unit,
    updateTask: (Task) -> Unit,
    resetSearchField: () -> Unit,
    setCategoryTitle: (Int) -> Unit,
    animationVisible: Boolean
) {

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = sectionName,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = modifier
                    .padding(horizontal = 10.dp)
                    .clip(MaterialTheme.shapes.small)
                    .size(30.dp)
                    .background(Color(0xFF1E1D1A)),
                onClick = {
                    resetSearchField()
                    setDate(tasksDate)
                    navController.navigate("addTask")
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Filter",
                    modifier.size(20.dp),
                    tint = Color.Yellow,
                )
            }
        }
        Column(
            modifier = modifier.padding(vertical = 20.dp),
        ) {
            if (tasksList.isEmpty()) {
                EmptyContent()
            } else {
                tasksList.forEach { item ->
                    TaskCard(
                        item = item,
                        navigateToEdit = { navController.navigate("addTask") },
                        getSelectedTask = getSelectedTask,
                        getTaskColor = getTaskColor,
                        deleteTask = deleteTask,
                        updateTask = updateTask,
                        setCategoryTitle = setCategoryTitle,
                        animationVisible = animationVisible
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    item: Task,
    navigateToEdit: () -> Unit,
    getSelectedTask: (Task) -> Unit,
    getTaskColor: (Int) -> String,
    deleteTask: (Task) -> Unit,
    updateTask: (Task) -> Unit,
    setCategoryTitle: (Int) -> Unit,
    animationVisible: Boolean
) {

    var color by remember { mutableStateOf(Color(1, 1, 1, 1)) }

    LaunchedEffect(Unit) {
        val colorRGB = getTaskColor(item.taskCategoryId).split("/").map { it.toFloat() }
        color =
            Color(red = colorRGB[0], green = colorRGB[1], blue = colorRGB[2], alpha = colorRGB[3])
    }

    val time = item.date.toInstant().atZone(ZoneId.of("UTC")).toLocalTime()
    val date = item.date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()

    AnimatedVisibility(
        visible = animationVisible,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 500))
                + expandHorizontally(expandFrom = Alignment.End),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
                + shrinkHorizontally() + fadeOut(),
    ) {
        Card(
            modifier = modifier
                .padding(bottom = 10.dp)
                .height(85.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = if (!item.checked) Color.White else Color.LightGray
            ),
            onClick = {
                navigateToEdit()
                setCategoryTitle(item.taskCategoryId)
                getSelectedTask(item)
            }
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(
                    modifier = modifier.padding(top = 17.dp),
                    onCheckedChange = {
                        updateTask(
                            Task(
                                todoId = item.todoId,
                                taskCategoryId = item.taskCategoryId,
                                title = item.title,
                                desc = item.desc,
                                checked = it,
                                date = item.date
                            )
                        )
                    },
                    checked = item.checked,
                )
                Row(
                    modifier = modifier
                        .weight(3f)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = modifier.padding(top = 7.dp, bottom = 7.dp, end = 10.dp)) {
                        Text(
                            text = item.title,
                            style = if (item.checked) MaterialTheme.typography.titleMedium.copy(
                                textDecoration = TextDecoration.LineThrough
                            ) else
                                MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.None),
                            maxLines = 2
                        )
                        Text(
                            modifier = modifier.padding(top = 3.dp, end = 5.dp),
                            text = item.desc,
                            style = MaterialTheme.typography.displaySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                }
                Row(
                    modifier = modifier.weight(1.2f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(
                        modifier = if (item.checked) modifier.fillMaxSize() else modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (item.checked) {
                            IconButton(
                                modifier = modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .size(30.dp)
                                    .background(Color.LightGray),
                                onClick = { deleteTask(item) }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    modifier.size(30.dp),
                                    tint = Color(0xFF1E1D1A),
                                )
                            }
                        } else {
                            Text(
                                modifier = modifier.padding(end = 10.dp),
                                text = "${date.dayOfMonth} ${date.month}",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 10.sp)
                            )
                            Text(
                                modifier = modifier.padding(end = 10.dp),
                                textAlign = TextAlign.Center,
                                text = "${if (time.hour.toString().length < 2) "0" + time.hour else time.hour}" +
                                        ":${if (time.minute.toString().length < 2) "0" + time.minute else time.minute}",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
                            )
                        }
                    }
                    Box(modifier = modifier.fillMaxHeight()) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(7.dp),
                            onDraw = {
                                drawRect(color = if (!item.checked) color else Color.LightGray)
                            }
                        )
                    }
                }
            }
        }
    }
}
