package com.example.tasko


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.tasko.ui.theme.TaskoTheme
import com.youcef_bounaas.tasko.Main.data.local.TaskDatabase
import com.youcef_bounaas.tasko.Main.domain.TasksRepository
import com.youcef_bounaas.tasko.Main.domain.TasksViewModel
import com.youcef_bounaas.tasko.Main.domain.TasksViewModelFactory
import com.example.tasko.Main.presentation.MainNavigation

class MainActivity : ComponentActivity() {

    private val tasksDatabase by lazy { TaskDatabase.getDatabase(this) }
    private val tasksRepository by lazy { TasksRepository(tasksDatabase.taskDao()) }
    private val taskViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(tasksRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskoTheme {
                MainNavigation(viewModel = taskViewModel)

            }
        }
    }
}

