package com.youcef_bounaas.tasko.Main.domain

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.youcef_bounaas.tasko.Main.data.local.Task
import com.youcef_bounaas.tasko.Main.data.local.TasksDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    val pendingTasks: Flow<List<Task>> = repository.pendingTasks
    val completedTasks: Flow<List<Task>> = repository.completedTasks
    val Tasks: TasksDao = repository.tasks

    private val _uiState = MutableStateFlow("Pending")
    val uiState: StateFlow<String> = _uiState


    fun getTask(taskId: Long): Flow<Task?> = flow {
        emit(Tasks.getTask(taskId))
    }


    fun upsertTask(task: Task) {
        viewModelScope.launch {
            repository.upsertTask(task)
        }
    }


    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }


    fun markTaskAsCompleted(task: Task) {
        val updatedTask = task.copy(isCompleted = true)  // Mark the task as completed
        viewModelScope.launch {
            repository.upsertTask(updatedTask)  // Update the database with the task's new state
        }
    }


    fun toggleTaskFilter() {
        _uiState.value = if (_uiState.value == "Pending") "Completed" else "Pending"
    }



    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted) // Toggle completion status
        viewModelScope.launch {
            repository.upsertTask(updatedTask)
        }
    }




    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredTasks: StateFlow<List<Task>> = _searchQuery
        .flatMapLatest { query ->
            repository.searchDatabase("%$query%")
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())




    fun updateSearchQuery(query: String) {
        Log.d("SearchQuery", "Updated query: $query")
        _searchQuery.value = query
    }

    private val _selectedDate = MutableStateFlow(java.time.LocalDate.now())
    val selectedDate: StateFlow<java.time.LocalDate> = _selectedDate

    val tasksBySelectedDate: StateFlow<List<Task>> = _selectedDate
        .flatMapLatest { date ->
            if (_uiState.value == "Pending") {
                repository.getPendingTasksByDate(date.toString())
            } else {
                repository.getCompletedTasksByDate(date.toString())
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSelectedDate(date: java.time.LocalDate) {
        _selectedDate.value = date
    }

}
