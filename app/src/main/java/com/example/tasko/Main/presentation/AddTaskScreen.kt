package com.example.tasko.Main.presentation

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview


data class Priority(
    val level: Int,
    val name: String,
    val color: Color
)

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val date: String,
    val isCompleted: Boolean = false
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
fun TaskAddingUI(
    isDarkTheme: Boolean,
    onAddTask: (String, String, Priority) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(priorities[0]) }
    var showPriorityPicker by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFF5F5F5)
    val surfaceColor = if (isDarkTheme) Color(0xFF2D2D2D) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val hintColor = if (isDarkTheme) Color(0xFF888888) else Color(0xFF666666)

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                text = "Upcoming",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = textColor
            )
        }

        // Priority Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPriorityPicker = !showPriorityPicker },
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
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
                    color = textColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    if (showPriorityPicker) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Priority",
                    tint = textColor
                )
            }
        }

        // Priority Picker
        if (showPriorityPicker) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
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
                                color = textColor,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Task Input
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            placeholder = { Text("Task title", color = hintColor) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = surfaceColor,
                unfocusedContainerColor = surfaceColor,
                focusedBorderColor = selectedPriority.color,
                unfocusedBorderColor = if (isDarkTheme) Color(0xFF444444) else Color(0xFFCCCCCC)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // Description Input
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            placeholder = { Text("Description", color = hintColor) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = surfaceColor,
                unfocusedContainerColor = surfaceColor,
                focusedBorderColor = selectedPriority.color,
                unfocusedBorderColor = if (isDarkTheme) Color(0xFF444444) else Color(0xFFCCCCCC)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (taskTitle.isNotBlank()) {
                        onAddTask(taskTitle, taskDescription, selectedPriority)
                        taskTitle = ""
                        taskDescription = ""
                        keyboardController?.hide()
                    }
                }
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Today",
                    color = if (isDarkTheme) Color(0xFF4CAF50) else Color(0xFF2E7D32),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Priority",
                    color = textColor,
                    fontSize = 16.sp
                )

                Text(
                    text = "Reminders",
                    color = textColor,
                    fontSize = 16.sp
                )
            }

            FloatingActionButton(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onAddTask(taskTitle, taskDescription, selectedPriority)
                        taskTitle = ""
                        taskDescription = ""
                        keyboardController?.hide()
                    }
                },
                containerColor = Color(0xFFE53E3E),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Add Task"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskAddingUI() {
    TaskAddingUI(isDarkTheme = false, onAddTask = { _, _, _ -> })
}