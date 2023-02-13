package com.example.todojetpackcomposeapp.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.ui.screens.addTask.OutlinedTextFieldValidation
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun AddCategoryAlertDialog(
    modifier: Modifier = Modifier,
    closeDialog: () -> Unit,
    insertCategory: (Category) -> Unit,
    updateCategory: (Category) -> Unit,
    category: Category? = null
) {

    var text by remember { mutableStateOf(category?.title ?: "") }
    var titleError by remember { mutableStateOf("") }

    val interactionSourceBtn1 = remember { MutableInteractionSource() }
    val isPressedBtn1 by interactionSourceBtn1.collectIsPressedAsState()

    val interactionSourceBtn2 = remember { MutableInteractionSource() }
    val isPressedBtn2 by interactionSourceBtn2.collectIsPressedAsState()

    val colorBtn1 = if (isPressedBtn1) Color(0xFF1E1D1A) else Color.White
    val textColorBtn1 = if (isPressedBtn1) Color.White else Color(0xFF1E1D1A)

    val colorBtn2 = if (isPressedBtn2) Color(0xFF1E1D1A) else Color.White
    val textColorBtn2 = if (isPressedBtn2) Color.White else Color(0xFF1E1D1A)

    val colorCoord: List<Float> =
        category?.color_coord?.split("/")?.map { it.toFloat() } ?: listOf(0f, 0f)

    val gradientColors = listOf(Color.Cyan, Color.Blue, Color.Green)

    val controller = rememberColorPickerController()

    LaunchedEffect(Unit) {
        controller.selectByCoordinate(
            colorCoord[0],
            colorCoord[1],
            fromUser = false
        )
    }

    AlertDialog(
        onDismissRequest = closeDialog
    ) {
        Surface(
            modifier = Modifier.wrapContentWidth(),
            shape = MaterialTheme.shapes.large,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Category",
                    modifier = modifier.padding(vertical = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextFieldValidation(
                    modifier = modifier
                        .heightIn(60.dp)
                        .fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        titleError = ""
                        text = it
                    },
                    placeholder = { Text(text = "Enter category name") },
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.LightGray
                    ),
                    error = titleError
                )
                Text(
                    text = "Choose a color",
                    style = TextStyle(brush = Brush.linearGradient(colors = gradientColors)),
                    modifier = modifier.padding(top = 15.dp)
                )
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(20.dp),
                    controller = controller,
                    onColorChanged = { }
                )
                Box(
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(color = controller.selectedColor.value)
                        .border(
                            border = ButtonDefaults.outlinedButtonBorder,
                            shape = MaterialTheme.shapes.small
                        )
                )
                Spacer(modifier = modifier.height(20.dp))
                Row(modifier = modifier.fillMaxWidth()) {
                    Button(
                        modifier = modifier
                            .padding(8.dp)
                            .weight(1f)
                            .height(50.dp)
                            .border(1.dp, Color(0xFF1E1D1A), MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        interactionSource = interactionSourceBtn1,
                        colors = ButtonDefaults.buttonColors(containerColor = colorBtn1),
                        onClick = closeDialog
                    ) {
                        Text(text = "Close", color = textColorBtn1)
                    }
                    Button(
                        modifier = modifier
                            .padding(8.dp)
                            .weight(1f)
                            .height(50.dp)
                            .border(1.dp, Color(0xFF1E1D1A), MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        interactionSource = interactionSourceBtn2,
                        colors = ButtonDefaults.buttonColors(containerColor = colorBtn2),
                        onClick = {
                            if (text == "") {
                                titleError = "Field cannot be empty"
                            } else {
                                if (category != null) {
                                    updateCategory(
                                        Category(
                                            categoryId = category.categoryId,
                                            color = "${controller.selectedColor.value.red}" +
                                                    "/${controller.selectedColor.value.green}" +
                                                    "/${controller.selectedColor.value.blue}" +
                                                    "/${controller.selectedColor.value.alpha}",
                                            color_coord = "${controller.selectedPoint.value.x}" +
                                                    "/${controller.selectedPoint.value.y}",
                                            title = text
                                        )
                                    )
                                } else {
                                    insertCategory(
                                        Category(
                                            color = "${controller.selectedColor.value.red}" +
                                                    "/${controller.selectedColor.value.green}" +
                                                    "/${controller.selectedColor.value.blue}" +
                                                    "/${controller.selectedColor.value.alpha}",
                                            color_coord = "${controller.selectedPoint.value.x}" +
                                                    "/${controller.selectedPoint.value.y}",
                                            title = text
                                        )
                                    )
                                }
                                closeDialog()
                            }
                        }
                    ) {
                        Text(text = if (category == null) "Add" else "Edit", color = textColorBtn2)
                    }
                }
            }
        }
    }
}