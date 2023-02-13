package com.example.todojetpackcomposeapp.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todojetpackcomposeapp.data.models.Category


enum class CategoryCardDropDownMenuBtn(val title: String) {
    EDIT("Edit"),
    DELETE("Delete")
}

@Composable
fun CategoryCardDropDownMenu(
    modifier: Modifier = Modifier,
    changeExpended: () -> Unit,
    expanded: Boolean,
    deleteCategory: (Category) -> Unit,
    item: Category,
    openDialog: () -> Unit,
    closeDropDownMenu: () -> Unit
) {
    DropdownMenu(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = MaterialTheme.shapes.small
            ),
        expanded = expanded,
        onDismissRequest = changeExpended
    ) {
        CategoryCardDropDownMenuBtn.values().forEach { itemValue ->
            fun btnClick() = when (itemValue) {
                CategoryCardDropDownMenuBtn.EDIT -> openDialog()
                else -> {
                    deleteCategory(item)
                }
            }
            DropdownMenuItem(
                text = { Text(text = itemValue.title) },
                onClick = {
                    btnClick()
                    closeDropDownMenu()
                }
            )
        }
    }
}