package com.example.todojetpackcomposeapp.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


enum class CategoryCardDropDownMenuBtn(val title: String) {
    EDIT("Edit"),
    DELETE("Delete")
}

@Composable
fun CategoryCardDropDownMenu(
    modifier: Modifier = Modifier,
    changeExpended: () -> Unit,
    expanded: Boolean,
    openDialog: () -> Unit,
    closeDropDownMenu: () -> Unit,
    openDeleteAlertDialog: () -> Unit
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
                else -> openDeleteAlertDialog()
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