package com.youcef_bounaas.tasko.Main.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youcef_bounaas.tasko.Main.domain.TasksViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberImagePainter
import com.example.tasko.Main.presentation.CalendarView
import com.youcef_bounaas.tasko.Main.data.local.Task
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(viewModel: TasksViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val showUpdateDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<Task?>(null) }

    // Collect states
    val uiState by viewModel.uiState.collectAsState() // Pending or Completed
    val searchQuery by viewModel.searchQuery.collectAsState() // Current search query
    val selectedDate by viewModel.selectedDate.collectAsState()
    val tasksBySelectedDate by viewModel.tasksBySelectedDate.collectAsState(emptyList())

    // Decide which tasks to show based on searchQuery and toggle
    val tasks = if (uiState == "Pending") viewModel.pendingTasks else viewModel.completedTasks
    val filteredTasks by viewModel.filteredTasks.collectAsState(emptyList()) // Search filtered tasks
    val displayedTasks by if (searchQuery.isNotEmpty()) {
        // If search query is active, show filtered tasks
        viewModel.filteredTasks.collectAsState(emptyList())
    } else {
        // Otherwise, show regular tasks based on UI state
        tasks.collectAsState(emptyList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager \n $uiState Tasks") },
                actions = {
                    IconButton(onClick = { viewModel.toggleTaskFilter() }) {
                        if (uiState == "Pending") {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Toggle Task Filter")
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Toggle Task Filter")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {

            CalendarView(
                selectedDate = selectedDate, // Use ViewModel's selected date
                onDateSelected = { selectedDate ->
                    viewModel.updateSelectedDate(selectedDate)
                }
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    if (searchQuery.isNotEmpty()) displayedTasks else tasksBySelectedDate
                ) { task ->
                    TaskItem(task, viewModel, selectedTask, showUpdateDialog)
                }
            }
        }

        // Add new task dialog
        if (showDialog.value) {
            AddTaskDialog(
                onDismiss = { showDialog.value = false },
                onTaskAdded = { newTask -> viewModel.upsertTask(newTask) }
            )
        }

        // Update task dialog
        if (showUpdateDialog.value && selectedTask.value != null) {
            val taskToUpdate = selectedTask.value!!
            AddTaskDialog(
                task = taskToUpdate,
                onDismiss = { showUpdateDialog.value = false },
                onTaskAdded = { updatedTask ->
                    viewModel.upsertTask(updatedTask)
                    showUpdateDialog.value = false
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    viewModel: TasksViewModel,
    selectedTask: MutableState<Task?>,
    showUpdateDialog: MutableState<Boolean>
) {
    val priorityColors = when (task.priority) {
        1 -> Color.Red
        2 -> Color.Yellow
        3 -> Color.Blue
        4 -> Color.Gray
        else -> Color.Black
    }

    Column(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                selectedTask.value = task
                showUpdateDialog.value = true
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(Modifier.padding(8.dp)) {
                if (!task.imagePath.isNullOrEmpty()) {
                    Image(
                        painter = rememberImagePainter(task.imagePath),
                        contentDescription = "Task Image",
                        modifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(text = task.title)
                Text(text = task.description, maxLines = 1)
                Text(text = "Due: ${task.dueDate}")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = task.isCompleted,
                        colors = RadioButtonColors(
                            selectedColor = priorityColors,
                            unselectedColor = priorityColors,
                            disabledUnselectedColor = priorityColors,
                            disabledSelectedColor = priorityColors
                        ),
                        onClick = { viewModel.toggleTaskCompletion(task) }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.deleteTask(task) }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}



