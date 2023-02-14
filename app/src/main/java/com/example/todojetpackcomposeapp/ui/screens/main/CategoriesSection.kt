package com.example.todojetpackcomposeapp.ui.screens.main

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.utils.RequestState
import kotlinx.coroutines.flow.Flow


@Composable
fun CategoriesSection(
    modifier: Modifier = Modifier,
    allCategories: RequestState<List<Category>>,
    getCheckedTasksOfCategoryCount: (Int) -> Flow<Int>,
    getTasksOfCategoryCount: (Int) -> Flow<Int>,
    insertCategory: (Category) -> Unit,
    updateCategory: (Category) -> Unit,
    deleteCategory: (Category) -> Unit,
    navigateToCategoryTasks: () -> Unit,
    setCategoryId: (Int) -> Unit,
    setCategoryTitle: (String) -> Unit
) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Column(modifier = modifier.padding(top = 18.dp, bottom = 8.dp)) {
        Text(
            modifier = modifier.padding(bottom = 10.dp),
            style = MaterialTheme.typography.titleMedium,
            text = "Task Categories"
        )
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(animationSpec = tween(durationMillis = 500))
                    + expandVertically(expandFrom = Alignment.Bottom),
            exit = slideOutVertically(animationSpec = tween(durationMillis = 500))
                    + shrinkOut(shrinkTowards = Alignment.Center),
        ) {
            LazyVerticalGrid(
                modifier = modifier.height(350.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    if (allCategories is RequestState.Success) {
                        items(allCategories.data.size) { index ->
                            CategoryCard(
                                item = allCategories.data[index],
                                getCheckedTasksOfCategoryCount = getCheckedTasksOfCategoryCount,
                                getTasksOfCategoryCount = getTasksOfCategoryCount,
                                insertCategory = insertCategory,
                                updateCategory = updateCategory,
                                deleteCategory = deleteCategory,
                                navigateToCategoryTasks = navigateToCategoryTasks,
                                setCategoryId = setCategoryId,
                                setCategoryTitle = setCategoryTitle
                            )
                        }
                    }
                    item {
                        AddCategoryCard(
                            insertCategory = insertCategory,
                            updateCategory = updateCategory
                        )
                    }
                })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    item: Category,
    getCheckedTasksOfCategoryCount: (Int) -> Flow<Int>,
    getTasksOfCategoryCount: (Int) -> Flow<Int>,
    insertCategory: (Category) -> Unit,
    updateCategory: (Category) -> Unit,
    deleteCategory: (Category) -> Unit,
    navigateToCategoryTasks: () -> Unit,
    setCategoryId: (Int) -> Unit,
    setCategoryTitle: (String) -> Unit
) {

    val colorRGB: List<Float> = item.color.split("/").map { it.toFloat() }
    val color =
        Color(red = colorRGB[0], green = colorRGB[1], blue = colorRGB[2], alpha = colorRGB[3])

    val checkedTasksOfCategoryCount = getCheckedTasksOfCategoryCount(item.categoryId)
        .collectAsState(initial = 0)
        .value

    val tasksOfCategoryCount = getTasksOfCategoryCount(item.categoryId)
        .collectAsState(initial = 0)
        .value

    var deleteAlertDialogState by remember { mutableStateOf(false) }
    var stateDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val percentage = checkedTasksOfCategoryCount.toFloat() / tasksOfCategoryCount.toFloat()

    Card(
        modifier = modifier.height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            setCategoryId(item.categoryId)
            setCategoryTitle(item.title)
            navigateToCategoryTasks()
        }
    ) {
        Row {
            CustomCircularProgressBar(
                percentage = (if (percentage.isNaN() || percentage.isInfinite()) 0.0f else percentage),
                number = 100,
                radius = 20.dp,
                color = color
            )
            Box(modifier = modifier.fillMaxWidth()) {
                Canvas(
                    modifier = Modifier
                        .padding(horizontal = 35.dp, vertical = 15.dp)
                        .size(10.dp)
                        .align(Alignment.TopEnd),
                    onDraw = {
                        drawCircle(color = color)
                    }
                )
                IconButton(
                    modifier = modifier
                        .padding(horizontal = 10.dp, vertical = 9.dp)
                        .size(20.dp)
                        .align(Alignment.TopEnd),
                    onClick = { expanded = true },
                ) {
                    Icon(Icons.Filled.MoreVert, null)
                }
                CategoryCardDropDownMenu(
                    changeExpended = { expanded = false },
                    expanded = expanded,
                    openDialog = { stateDialog = true },
                    closeDropDownMenu = { expanded = false },
                    openDeleteAlertDialog = { deleteAlertDialogState = true }
                )

            }
        }
        Text(
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier.padding(horizontal = 15.dp),
            text = item.title
        )
        Text(
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier.padding(horizontal = 15.dp, vertical = 0.dp),
            text = "$tasksOfCategoryCount tasks"
        )
        Row(
            modifier = modifier
                .padding(horizontal = 15.dp, vertical = 13.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier
                    .weight(3f)
                    .height(30.dp)
                    .fillMaxWidth()
                    .padding(end = 5.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(color = Color(0f, 1f, 0f, 0.15f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier.padding(0.dp),
                    style = MaterialTheme.typography.bodySmall,
                    text = "$checkedTasksOfCategoryCount Completed"
                )
            }
            Column(
                modifier = modifier
                    .weight(2f)
                    .height(30.dp)
                    .fillMaxWidth()
                    .padding(start = 5.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(color = Color(1f, 0f, 0f, 0.15f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = "${tasksOfCategoryCount - checkedTasksOfCategoryCount} Left"
                )
            }
        }
        if (deleteAlertDialogState) {
           CustomDeleteAlertDialog(
               closeDeleteAlertDialog = { deleteAlertDialogState = false },
               item = item,
               deleteCategory = { deleteCategory(it) }
           )
        }
        if (stateDialog) {
            AddCategoryAlertDialog(
                closeDialog = { stateDialog = false },
                insertCategory = insertCategory,
                updateCategory = updateCategory,
                category = item
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryCard(
    modifier: Modifier = Modifier,
    insertCategory: (Category) -> Unit,
    updateCategory: (Category) -> Unit
) {

    var stateDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(bottom = 60.dp)
            .size(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { stateDialog = true }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
            Text(text = "Add a category")
        }
        if (stateDialog) {
            AddCategoryAlertDialog(
                closeDialog = { stateDialog = false },
                insertCategory = insertCategory,
                updateCategory = updateCategory
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDeleteAlertDialog(
    modifier: Modifier = Modifier,
    closeDeleteAlertDialog: () -> Unit,
    item: Category,
    deleteCategory: (Category) -> Unit
) {

    val interactionSourceBtn1 = remember { MutableInteractionSource() }
    val isPressedBtn1 by interactionSourceBtn1.collectIsPressedAsState()

    val interactionSourceBtn2 = remember { MutableInteractionSource() }
    val isPressedBtn2 by interactionSourceBtn2.collectIsPressedAsState()

    val colorBtn1 = if (isPressedBtn1) Color(0xFF1E1D1A) else Color.White
    val textColorBtn1 = if (isPressedBtn1) Color.White else Color(0xFF1E1D1A)

    val colorBtn2 = if (isPressedBtn2) MaterialTheme.colorScheme.error else Color.White
    val textColorBtn2 = if (isPressedBtn2) Color.White else MaterialTheme.colorScheme.error


    AlertDialog(
        onDismissRequest = closeDeleteAlertDialog
    ) {
        Surface(
            modifier = modifier.wrapContentWidth(),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column (
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Delete \"${item.title}\"?")
                Row(modifier = modifier.fillMaxWidth().padding(top = 20.dp)) {
                    Button(
                        modifier = modifier
                            .padding(start = 3.dp, end = 8.dp)
                            .weight(1f)
                            .height(50.dp)
                            .border(1.dp, Color(0xFF1E1D1A), MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        interactionSource = interactionSourceBtn1,
                        colors = ButtonDefaults.buttonColors(containerColor = colorBtn1),
                        onClick = closeDeleteAlertDialog
                    ) {
                        Text(text = "Close", color = textColorBtn1)
                    }
                    Button(
                        modifier = modifier
                            .padding(start = 8.dp, end = 3.dp)
                            .weight(1f)
                            .height(50.dp)
                            .border(1.dp, MaterialTheme.colorScheme.error, MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        interactionSource = interactionSourceBtn2,
                        colors = ButtonDefaults.buttonColors(containerColor = colorBtn2),
                        onClick = {deleteCategory(item)}
                    ) {
                        Text(text = "Delete", color = textColorBtn2)
                    }
                }
            }
        }
    }
}