package com.example.tasko.Main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.time.LocalDate

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
                    text = LocalDate.now().dayOfMonth.toString(),
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

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(isDarkTheme = false, selectedIndex = 0, onItemSelected = {})
}