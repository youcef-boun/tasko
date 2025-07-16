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

@Composable
fun BottomNavigationBar(
    isDarkTheme: Boolean,
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    val backgroundColor = if (isDarkTheme) Color(0xFF2D2D2D) else Color.White
    val selectedColor = if (isDarkTheme) Color(0xFFE53E3E) else Color(0xFFE53E3E)
    val unselectedColor = if (isDarkTheme) Color(0xFF888888) else Color(0xFF666666)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Today
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onItemSelected(0) }
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (selectedIndex == 0) selectedColor else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "16",
                    color = if (selectedIndex == 0) Color.White else selectedColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Today",
                color = if (selectedIndex == 0) selectedColor else unselectedColor,
                fontSize = 12.sp
            )
        }

        // Upcoming
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onItemSelected(1) }
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Upcoming",
                tint = if (selectedIndex == 1) selectedColor else unselectedColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Upcoming",
                color = if (selectedIndex == 1) selectedColor else unselectedColor,
                fontSize = 12.sp
            )
        }

        // Search
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onItemSelected(2) }
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = if (selectedIndex == 2) selectedColor else unselectedColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Search",
                color = if (selectedIndex == 2) selectedColor else unselectedColor,
                fontSize = 12.sp
            )
        }

        // Browse
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onItemSelected(3) }
        ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Browse",
                tint = if (selectedIndex == 3) selectedColor else unselectedColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Browse",
                color = if (selectedIndex == 3) selectedColor else unselectedColor,
                fontSize = 12.sp
            )
        }
    }
}

// Example usage
@Composable
fun TaskManagementApp() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(0) } // 0 = Add, 1 = View, 2 = Navigation
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    val sampleTask = Task(
        id = "1",
        title = "task 1",
        description = "description 1",
        priority = priorities[0],
        date = "Jul 13",
        isCompleted = false
    )

    Column(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            0 -> TaskAddingUI(
                isDarkTheme = isDarkTheme,
                onAddTask = { title, description, priority ->
                    val newTask = Task(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        description = description,
                        priority = priority,
                        date = "Jul 16",
                        isCompleted = false
                    )
                    tasks = tasks + newTask
                }
            )
            1 -> TaskViewUI(
                task = sampleTask,
                isDarkTheme = isDarkTheme,
                onUpdateTask = { updatedTask ->
                    // Handle task update
                }
            )
            2 -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Navigation Demo",
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            isDarkTheme = isDarkTheme,
            selectedIndex = currentScreen,
            onItemSelected = { index ->
                currentScreen = index
            }
        )
    }

   
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(isDarkTheme = false, selectedIndex = 0, onItemSelected = {})
}