package com.example.tasko.Main.presentation

import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.youcef_bounaas.tasko.Main.data.local.Task
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate

data class Priority(
    val level: Int,
    val name: String,
    val color: Color
)

// Priority definitions
val priorities = listOf(
    Priority(1, "Priority 1", Color(0xFFE53E3E)),
    Priority(2, "Priority 2", Color(0xFFFF8C00)),
    Priority(3, "Priority 3", Color(0xFF3182CE)),
    Priority(4, "Priority 4", Color(0xFF718096))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    task: Task? = null, // Pass the task to update (null means adding a new task)
    onDismiss: () -> Unit,
    onTaskAdded: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf(task?.title ?: "") }
    var taskDescription by remember { mutableStateOf(task?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(priorities.find { it.level == task?.priority } ?: priorities[0]) }
    var showPriorityPicker by remember { mutableStateOf(false) }
    var taskDueDate by remember { mutableStateOf(task?.dueDate ?: LocalDate.now().toString()) }
    var taskImagePath by remember { mutableStateOf(task?.imagePath ?: "") }
    
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val tempFile = getFileFromUri(context, it)
            taskImagePath = tempFile?.absolutePath ?: ""
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

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (task != null) "Update Task" else "Add New Task",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Priority Selector
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPriorityPicker = !showPriorityPicker },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    selectedPriority.color,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedPriority.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            if (showPriorityPicker) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Priority",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Priority Picker
                if (showPriorityPicker) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            priorities.forEach { priority ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedPriority = priority
                                            showPriorityPicker = false
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                priority.color,
                                                RoundedCornerShape(2.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = priority.name,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Task Title Input
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    placeholder = { Text("Task title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedPriority.color,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                // Description Input
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    placeholder = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedPriority.color,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

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
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedPriority.color,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Image Selection
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (taskImagePath.isEmpty()) "Select Image" else "Image Selected")
                }

                if (taskImagePath.isNotEmpty()) {
                    Text("Selected Image Path: $taskImagePath", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            if (taskTitle.isNotEmpty()) {
                                try {
                                    val updatedTask = task?.copy(
                                        title = taskTitle,
                                        description = taskDescription,
                                        priority = selectedPriority.level,
                                        dueDate = taskDueDate,
                                        imagePath = taskImagePath
                                    ) ?: Task(
                                        title = taskTitle,
                                        description = taskDescription,
                                        priority = selectedPriority.level,
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
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedPriority.color)
                    ) {
                        Text(if (task != null) "Update Task" else "Add Task")
                    }
                }
            }
        }
    }
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

@Preview(showBackground = true)
@Composable
fun PreviewAddTaskDialog() {
    AddTaskDialog(
        onDismiss = {},
        onTaskAdded = {}
    )
}