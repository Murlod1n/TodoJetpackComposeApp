package com.example.todojetpackcomposeapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id") val categoryId: Int = 0,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "color_coord") val color_coord: String,
    @ColumnInfo(name = "title") val title: String
)