package com.example.tasko.Main.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youcef_bounaas.tasko.Main.domain.TasksViewModel
import com.youcef_bounaas.tasko.Main.presentation.TaskScreen
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars

@Composable
fun MainNavigation(viewModel: TasksViewModel) {
    var selectedScreenIndex by remember { mutableStateOf(0) }
    val isDarkTheme = isSystemInDarkTheme()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Main content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedScreenIndex) {
                    0 -> TaskToday(viewModel = viewModel) // Today
                    1 -> TaskScreen(viewModel = viewModel) // Upcoming
                    2 -> TaskSearch(viewModel = viewModel) // Search

                }
            }

            // Bottom navigation bar with proper system navigation bar padding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                BottomNavigationBar(
                    isDarkTheme = isDarkTheme,
                    selectedIndex = selectedScreenIndex,
                    onItemSelected = { index ->
                        selectedScreenIndex = index
                    }
                )
            }
        }
    }
} 