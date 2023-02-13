package com.example.todojetpackcomposeapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.todojetpackcomposeapp.ui.shared.CustomScaffold
import com.example.todojetpackcomposeapp.ui.theme.TodoJetpackComposeAppTheme
import com.example.todojetpackcomposeapp.ui.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TodoViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoJetpackComposeAppTheme {
                CustomScaffold(viewModel = viewModel) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFF8F7F3),
                    ) {
                        //MainScreen(viewModel = viewModel)
                        //AddTaskScreen(viewModel = viewModel)
                        //TasksListScreen(viewModel = viewModel)
                    }
                }

            }
        }
    }
}