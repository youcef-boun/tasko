package com.example.tasko.Main.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.youcef_bounaas.tasko.Main.data.local.Task

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewUI(
    task: Task,
    isDarkTheme: Boolean,
    onUpdateTask: (Task) -> Unit
) {
    var isEditingTitle by remember { mutableStateOf(false) }
    var isEditingDescription by remember { mutableStateOf(false) }
    var isEditingPriority by remember { mutableStateOf(false) }
    var isEditingDate by remember { mutableStateOf(false) }

    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedPriority by remember { mutableStateOf(task.priority) }
    var editedDate by remember { mutableStateOf(task.duedate) }

    val backgroundColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFF5F5F5)
    val surfaceColor = if (isDarkTheme) Color(0xFF2D2D2D) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val hintColor = if (isDarkTheme) Color(0xFF888888) else Color(0xFF666666)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
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

        Text(
            text = "3 tasks",
            color = hintColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Overdue Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Overdue 3",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Text(
                text = "Reschedule",
                fontSize = 14.sp,
                color = Color(0xFFE53E3E)
            )
        }

        // Task Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Task Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = task.isCompleted,
                        onClick = {
                            onUpdateTask(task.copy(isCompleted = !task.isCompleted))
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = task.priority.color,
                            unselectedColor = task.priority.color
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (isEditingTitle) {
                        OutlinedTextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onUpdateTask(task.copy(title = editedTitle))
                                    isEditingTitle = false
                                }
                            )
                        )
                    } else {
                        Text(
                            text = task.title,
                            fontSize = 18.sp,
                            color = textColor,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isEditingTitle = true }
                        )
                    }
                }

                // Task Description
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp)
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Description",
                        tint = hintColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (isEditingDescription) {
                        OutlinedTextField(
                            value = editedDescription,
                            onValueChange = { editedDescription = it },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onUpdateTask(task.copy(description = editedDescription))
                                    isEditingDescription = false
                                }
                            )
                        )
                    } else {
                        Text(
                            text = task.description,
                            fontSize = 16.sp,
                            color = textColor,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isEditingDescription = true }
                        )
                    }
                }

                // Task Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (isEditingDate) {
                        OutlinedTextField(
                            value = editedDate,
                            onValueChange = { editedDate = it },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onUpdateTask(task.copy(date = editedDate))
                                    isEditingDate = false
                                }
                            )
                        )
                    } else {
                        Text(
                            text = task.date,
                            fontSize = 16.sp,
                            color = Color(0xFFE53E3E),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isEditingDate = true }
                        )
                    }
                }

                // Task Priority
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                task.priority.color,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (isEditingPriority) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = backgroundColor)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                priorities.forEach { priority ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onUpdateTask(task.copy(priority = priority))
                                                isEditingPriority = false
                                            }
                                            .padding(8.dp),
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
                    } else {
                        Text(
                            text = task.priority.name,
                            fontSize = 16.sp,
                            color = textColor,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isEditingPriority = true }
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Handle deadline */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textColor
                        )
                    ) {
                        Text("Deadline")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PRO",
                            fontSize = 10.sp,
                            color = Color(0xFFFF8C00),
                            modifier = Modifier
                                .background(
                                    Color(0xFFFF8C00).copy(alpha = 0.2f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                    OutlinedButton(
                        onClick = { /* Handle labels */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textColor
                        )
                    ) {
                        Text("Labels")
                    }

                    OutlinedButton(
                        onClick = { /* Handle reminders */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textColor
                        )
                    ) {
                        Text("Reminders")
                    }
                }

                // Add Sub-task
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clickable { /* Handle add sub-task */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add sub-task",
                        color = Color(0xFFE53E3E),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add Comment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add a comment",
                color = hintColor,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                tint = hintColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskViewUI() {
    val sampleTask = Task(
        id = "1",
        title = "Sample Task",
        description = "This is a sample description.",
        priority = priorities[0],
        date = "Jul 16",
        isCompleted = false
    )
    TaskViewUI(task = sampleTask, isDarkTheme = false, onUpdateTask = {})
}

 */
