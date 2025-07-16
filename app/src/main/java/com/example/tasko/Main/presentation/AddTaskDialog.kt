package com.youcef_bounaas.tasko.Main.presentation

import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.youcef_bounaas.tasko.Main.data.local.Task
import java.io.FileOutputStream


import java.io.File
import java.io.InputStream
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    task: Task? = null, // Pass the task to update (null means adding a new task)
    onDismiss: () -> Unit,
    onTaskAdded: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf(task?.title ?: "") }
    var taskPriority by remember { mutableStateOf(task?.priority?.toString() ?: "") }
    var taskDescription by remember { mutableStateOf(task?.description ?: "") }
    var taskDueDate by remember { mutableStateOf(task?.dueDate ?: LocalDate.now().toString()) }
    var taskImagePath by remember { mutableStateOf(task?.imagePath ?:"") }

    var isPriorityValid by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val tempFile = getFileFromUri(context, it) // Convert Uri to a File
            taskImagePath = tempFile?.absolutePath ?: "" // Store the file path for local processing
        }
    }


    // Set initial date based on the task if exists
    task?.dueDate?.let {
        val parts = it.split("-")
        if (parts.size == 3) {
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
        }
    }

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    // Open date picker
    fun showDatePicker() {
        android.app.DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            taskDueDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
        }, year, month, day).show()
    }






    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = if (task != null) "Update Task" else "Add New Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task Title") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Description") },
                    singleLine = false
                )

                OutlinedTextField(
                    value = taskPriority,
                    onValueChange = {
                        // Ensure the input is a number and between 1 and 4
                        if (it.isEmpty() || it.toIntOrNull() in 1..4) {
                            taskPriority = it
                            isPriorityValid = true  // Set to true when input is valid
                        } else {
                            isPriorityValid = false  // Mark as invalid if input is outside the range
                        }
                    },
                    label = { Text("Priority (1-4)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isPriorityValid  // Show error if input is invalid
                )
                if (!isPriorityValid) {
                    Text("Priority must be between 1 and 4", color = MaterialTheme.colorScheme.error)
                }



                // Due Date with DatePicker
                OutlinedTextField(
                    value = taskDueDate,
                    onValueChange = {},
                    label = { Text("Due Date (YYYY-MM-DD)") },
                    singleLine = true,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                        }
                    }
                )





                Button(
                    onClick = { imagePickerLauncher.launch("image/*") }
                ) {
                    Text(text = if (taskImagePath.isEmpty()) "Select Image" else "Image Selected")
                }

                taskImagePath?.let {
                    if (it.isNotEmpty()) {
                        Text("Selected Image Path: $it")
                    }
                }














            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotEmpty() && taskPriority.isNotEmpty() && taskDueDate.isNotEmpty()) {
                        try {
                            val updatedTask = task?.copy(
                                title = taskTitle,
                                description = taskDescription,
                                priority = taskPriority.toInt(),
                                dueDate = taskDueDate,


                            ) ?: Task(
                                title = taskTitle,
                                description = taskDescription,
                                priority = taskPriority.toInt(),
                                dueDate = taskDueDate,
                                isCompleted = false,
                                imagePath = taskImagePath



                            )
                            onTaskAdded(updatedTask)
                            onDismiss()
                        } catch (e: Exception) {
                            // Handle invalid input gracefully
                        }
                    }
                }
            ) {
                Text(if (task != null) "Update Task" else "Add Task")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}



// Helper function to extract file path from Uri
fun getFileFromUri(context: Context, uri: Uri): File? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    inputStream?.use { input ->
        val tempFile = File.createTempFile("temp_image", null, context.cacheDir)
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
        return tempFile
    }
    return null
}