package com.example.todojetpackcomposeapp.di

import android.app.Application
import com.example.todojetpackcomposeapp.data.dao.TodoDao
import com.example.todojetpackcomposeapp.data.database.TodoDatabase
import com.example.todojetpackcomposeapp.data.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providerTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepository(todoDao)
    }

    @Singleton
    @Provides
    fun providerAppDataBase(app: Application): TodoDatabase {
        return TodoDatabase.getDatabase(context = app)
    }

    @Singleton
    @Provides
    fun providerTodoDao(appDatabase: TodoDatabase): TodoDao {
        return appDatabase.todoDao()
    }
}