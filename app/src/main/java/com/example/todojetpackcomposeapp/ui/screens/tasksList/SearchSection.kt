package com.example.todojetpackcomposeapp.ui.screens.tasksList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    searchFieldEditor: (String) -> Unit,
    searchField: String,
    isCheckedFilter: Boolean?,
    changeCheckedFilter: (Boolean?) -> Unit
) {

    val icon = when (isCheckedFilter) {
        true -> Icons.Filled.Check
        false -> Icons.Filled.Close
        else -> Icons.Filled.List
    }

    Row(modifier = modifier.padding(vertical = 20.dp)) {
        OutlinedTextField(
            modifier = modifier.heightIn(50.dp).weight(10f),
            value = searchField,
            onValueChange = { searchFieldEditor(it) },
            placeholder = { Text(text = "Search tasks by title") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFF0DC28)
            )
        )
        IconButton(
            modifier = modifier
                .padding(start = 10.dp)
                .heightIn(53.dp).weight(2.5f)
                .clip(MaterialTheme.shapes.small)
                .background(Color(0xFFF0DC28)),
            onClick = {
                when (isCheckedFilter) {
                    true -> changeCheckedFilter(false)
                    false -> changeCheckedFilter(null)
                    else -> changeCheckedFilter(true)
                }
            },
        ) {
            Icon(
                icon,
                contentDescription = "Filter",
                modifier.size(30.dp),
                tint = Color.Black,
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}