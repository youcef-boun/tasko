package com.youcef_bounaas.tasko.Main.domain


import com.youcef_bounaas.tasko.Main.data.local.Task
import com.youcef_bounaas.tasko.Main.data.local.TasksDao
import kotlinx.coroutines.flow.Flow

class TasksRepository(private val tasksDao: TasksDao) {


    val pendingTasks: Flow<List<Task>> = tasksDao.getPendingTasks()

    val completedTasks: Flow<List<Task>> = tasksDao.getCompletedTasks()

    val tasks : TasksDao = tasksDao

    suspend fun upsertTask(task:Task){
        tasksDao.upsertTask(task)
    }

    suspend fun deleteTask(task:Task){
        tasksDao.deleteTask(task)
    }

    fun searchDatabase(query: String): Flow<List<Task>> {
        return tasksDao.searchDatabase(query)
    }

    fun getPendingTasksByDate(date: String): Flow<List<Task>> {
        return tasksDao.getPendingTasksByDate(date)
    }

    fun getCompletedTasksByDate(date: String): Flow<List<Task>> {
        return tasksDao.getCompletedTasksByDate(date)
    }


}
