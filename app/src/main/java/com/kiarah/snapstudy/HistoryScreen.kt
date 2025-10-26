package com.kiarah.snapstudy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SampleHistoryItem(
    val timestamp: String,
    val extractedText: String,
    val aiResponse: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: SnapStudyViewModel,
    onBack: () -> Unit
) {
    val history = viewModel.history.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (history.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                items(history) { item ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(formatTimestamp(item.timestamp), fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(4.dp))
                            Text("Q: ${item.extractedText}", fontSize = 16.sp)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Ans: ${item.aiResponse.take(50)}...",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No history yet!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}

// Helper function for time formatting (put at top or separate util file)
fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("MMM dd, HH:mm")
    return format.format(date)
}
