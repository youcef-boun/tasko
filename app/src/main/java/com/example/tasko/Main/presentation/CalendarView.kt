package com.example.tasko.Main.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val today = LocalDate.now()
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    // Derive the current month from the pager state
    val currentMonth by remember {
        derivedStateOf {
            val monthOffset = pagerState.currentPage - (Int.MAX_VALUE / 2)
            YearMonth.now().plusMonths(monthOffset.toLong())
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Month/Year Header with dropdown indicator - now updates with swipe
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    // Handle month/year picker click
                }
            ) {
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select Month",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Static Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Swipeable Calendar Grid
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val monthOffset = page - (Int.MAX_VALUE / 2)
            val month = YearMonth.now().plusMonths(monthOffset.toLong())

            CalendarGrid(
                yearMonth = month,
                today = today,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
        }
    }
}
@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    today: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0
    val daysInMonth = yearMonth.lengthOfMonth()

    // Previous month's trailing days
    val prevMonth = yearMonth.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()

    // Next month's leading days
    val nextMonth = yearMonth.plusMonths(1)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Previous month's trailing days
        items(firstDayOfWeek) { index ->
            val day = daysInPrevMonth - firstDayOfWeek + index + 1
            val date = prevMonth.atDay(day)
            CalendarDay(
                day = day,
                date = date,
                isCurrentMonth = false,
                isToday = date == today,
                isSelected = date == selectedDate,
                onClick = { onDateSelected(date) }
            )
        }

        // Current month days
        items(daysInMonth) { index ->
            val day = index + 1
            val date = yearMonth.atDay(day)
            CalendarDay(
                day = day,
                date = date,
                isCurrentMonth = true,
                isToday = date == today,
                isSelected = date == selectedDate,
                onClick = { onDateSelected(date) }
            )
        }

        // Next month's leading days to fill the grid
        val remainingCells = 42 - (firstDayOfWeek + daysInMonth) // 6 rows * 7 days = 42
        items(remainingCells) { index ->
            val day = index + 1
            val date = nextMonth.atDay(day)
            CalendarDay(
                day = day,
                date = date,
                isCurrentMonth = false,
                isToday = date == today,
                isSelected = date == selectedDate,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    date: LocalDate,
    isCurrentMonth: Boolean,
    isToday: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isToday -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> Color.Transparent
    }

    val textColor = when {
        isToday -> MaterialTheme.colorScheme.onPrimary
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        isCurrentMonth -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarViewPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> selectedDate = newDate }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
fun CalendarViewWidePreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            var selectedDate by remember { mutableStateOf(LocalDate.now().plusDays(3)) }

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> selectedDate = newDate }
            )
        }
    }
}