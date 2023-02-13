package com.example.todojetpackcomposeapp.ui.screens.addTask


import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.ui.viewmodel.TodoViewModel
import com.example.todojetpackcomposeapp.utils.RequestState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.*
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel,
    navController: NavController
) {

    val allCategories by viewModel.allCategories.collectAsState()

    var screenTitle by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    var dateCheck by remember { mutableStateOf(false) }
    var timeCheck by remember { mutableStateOf(false) }

    val calendarState = rememberSheetState()
    val clockState = rememberSheetState()


    BackHandler {
        if (viewModel.taskCategoryId.value != -1) {
            viewModel.resetTaskWithoutCategory()
        } else {
            viewModel.resetTask()
        }
        navController.popBackStack()
    }

    val route = navController.currentBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        screenTitle = if (route?.contains("edit") == true) {
            "Edit task"
        } else {
            "Add new task"
        }

    }

    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier
                .padding(top = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = screenTitle,
                style = MaterialTheme.typography.displayLarge,
                color = Color(0xFF1E1D1A)
            )
            IconButton(
                modifier = modifier
                    .padding(end = 7.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .size(40.dp)
                    .background(Color(0xFF1E1D1A)),
                onClick = {
                    if (viewModel.taskCategoryId.value != -1) {
                        viewModel.resetTaskWithoutCategory()
                    } else {
                        viewModel.resetTask()
                    }
                    navController.popBackStack()
                },
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier.size(20.dp),
                    tint = Color(0xFFF0DC28),
                )
            }
        }
        Spacer(modifier = modifier.height(30.dp))
        Text(text = "To-do", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextFieldValidation(
            modifier = modifier
                .heightIn(60.dp)
                .fillMaxWidth(),
            value = viewModel.taskTitle.value,
            onValueChange = {
                viewModel.taskTitle.value = it
                titleError = ""
            },
            placeholder = { Text(text = "E.g website design") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = Color.LightGray),
            error = titleError
        )
        Spacer(modifier = modifier.height(30.dp))
        Text(text = "Category", style = MaterialTheme.typography.headlineSmall)
        CategoryDropDawnMenu(
            options = allCategories,
            selectCategory = { viewModel.taskCategoryId.value = it },
            selectedText = viewModel.categoryTitle.value,
            selectedTextFun = { viewModel.categoryTitle.value = it },
            categoryError = categoryError,
            removeError = { categoryError = "" }
        )
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
            ),
            selection = CalendarSelection.Date { date ->
                viewModel.date.value = date
                dateCheck = true
            }
        )
        ClockDialog(
            state = clockState,
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                viewModel.time.value = LocalTime.of(hours, minutes)
                timeCheck = true
            }
        )
        Spacer(modifier = modifier.height(30.dp))
        Text(text = "Date", style = MaterialTheme.typography.headlineSmall)
        Row(modifier = modifier.padding(top = 10.dp)) {
            Button(
                modifier = modifier
                    .padding(end = 5.dp)
                    .weight(1f)
                    .height(50.dp)
                    .border(1.dp, Color(0xFF1E1D1A), MaterialTheme.shapes.small),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (dateCheck) Color(0xFF1E1D1A) else Color.White
                ),
                onClick = { calendarState.show() }
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = viewModel.date.value.toString(),
                    color = if (dateCheck) Color(0xFFF0DC28) else Color(0xFF1E1D1A)
                )
            }
            Button(
                modifier = modifier
                    .padding(start = 5.dp)
                    .weight(1f)
                    .height(50.dp)
                    .border(1.dp, Color(0xFF1E1D1A), MaterialTheme.shapes.small),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (timeCheck) Color(0xFF1E1D1A) else Color.White
                ),
                onClick = { clockState.show() }
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "${viewModel.time.value.hour}:${viewModel.time.value.minute}",
                    color = if (timeCheck) Color(0xFFF0DC28) else Color(0xFF1E1D1A)
                )
            }
        }
        Spacer(modifier = modifier.height(15.dp))
        Text(text = "Description", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            modifier = modifier
                .padding(top = 10.dp)
                .heightIn(min = 50.dp)
                .fillMaxWidth(),
            value = viewModel.desc.value,
            onValueChange = { viewModel.desc.value = it },
            placeholder = { Text(text = "Description") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = Color.LightGray)
        )
        Spacer(modifier = modifier.height(15.dp))
        Button(
            modifier = modifier
                .padding(vertical = 8.dp)
                .height(50.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1D1A)),
            onClick = {
                if (viewModel.taskTitle.value == "" || viewModel.taskCategoryId.value == -1) {
                    if (viewModel.taskTitle.value == "") titleError = "Field cannot be empty!"
                    if (viewModel.taskCategoryId.value == -1) categoryError =
                        "Please, select a category!"
                } else {
                    viewModel.insertTask()
                    navController.popBackStack()
                }
            }
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = screenTitle,
                color = Color(0xFFF0DC28)
            )
        }
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDawnMenu(
    modifier: Modifier = Modifier,
    options: RequestState<List<Category>>,
    selectCategory: (Int) -> Unit,
    selectedText: String,
    selectedTextFun: (String) -> Unit,
    categoryError: String,
    removeError: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column {
        OutlinedTextFieldValidation(
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() }
                .noRippleClickable {
                    removeError()
                    expanded = !expanded
                },
            value = if (selectedText == "All categories" || selectedText == "") "Select a category" else selectedText,
            onValueChange = { },
            placeholder = { Text(text = "E.g website design") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (categoryError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.error,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            trailingIcon = { Icon(icon, "category select") },
            readOnly = true,
            error = categoryError,
            enabled = false
        )
        if (options is RequestState.Success) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .background(Color.White)
                    .clip(MaterialTheme.shapes.small)
            ) {
                options.data.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label.title) },
                        onClick = {
                            selectedTextFun(label.title)
                            selectCategory(label.categoryId)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}