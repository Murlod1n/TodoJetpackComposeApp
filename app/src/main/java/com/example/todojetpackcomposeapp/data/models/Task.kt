package com.example.todojetpackcomposeapp.data.models

import androidx.room.*
import java.util.Date


@Entity(tableName = "tasks", foreignKeys = [
    ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("category_id"),
        childColumns = arrayOf("task_category_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("task_id", unique = true)]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id") val todoId: Int = 0,
    @ColumnInfo(name = "task_category_id") var taskCategoryId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @TypeConverters(DateTypeConvert::class)
    @ColumnInfo(name = "date") val date: Date
)