package com.example.todojetpackcomposeapp.data.models

import androidx.room.Embedded
import androidx.room.Relation


data class TaskWithCategoryRelation (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "task_category_id",
        entityColumn = "category_id"
    )
    val category: List<Category>)
