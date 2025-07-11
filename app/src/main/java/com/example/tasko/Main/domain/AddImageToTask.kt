package com.youcef_bounaas.tasko.Main.domain

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.youcef_bounaas.tasko.Main.data.local.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddImageToTask(taskId: Long, context: Context, onUpdate: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val db = TaskDatabase.getDatabase(context)
    var selectedImagePath by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val imagePath = saveImageToInternalStorage(context, it, "task_image_$taskId")
            if (imagePath != null) {
                selectedImagePath = imagePath
                coroutineScope.launch(Dispatchers.IO) {
                    val task = db.taskDao().getTask(taskId)
                    task?.let {
                        val updatedTask = it.copy(imagePath = imagePath)
                        db.taskDao().upsertTask(updatedTask)
                        onUpdate() // Notify that the task was updated
                    }
                }
            }
        }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Add/Update Image")
    }

    selectedImagePath?.let {
        Text("Selected Image Path: $it") // Debug/info display
    }
}