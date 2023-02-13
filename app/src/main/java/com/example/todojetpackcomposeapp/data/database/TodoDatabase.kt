package com.example.todojetpackcomposeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todojetpackcomposeapp.data.dao.TodoDao
import com.example.todojetpackcomposeapp.data.models.Category
import com.example.todojetpackcomposeapp.data.models.DateTypeConvert
import com.example.todojetpackcomposeapp.data.models.Task


@Database(entities = [Category::class, Task::class], version = 1)
@TypeConverters(DateTypeConvert::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, TodoDatabase::class.java, "todo.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as TodoDatabase
        }
    }
}