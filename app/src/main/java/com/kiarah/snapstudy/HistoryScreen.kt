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
    viewModel: SnapStudyViewModel, // for later connection to real data
    onBack: () -> Unit
) {
    // Temporary: use a normal list, no delegate
    val history = listOf(
        SampleHistoryItem(
            timestamp = "Oct 17, 21:09",
            extractedText = "Solve: 2x + 3 = 11",
            aiResponse = "Subtract 3, then divide by 2: x = 4"
        ),
        SampleHistoryItem(
            timestamp = "Oct 16, 17:55",
            extractedText = "What is photosynthesis?",
            aiResponse = "Photosynthesis is how plants convert sunlight into energy."
        ),
        SampleHistoryItem(
            timestamp = "Oct 16, 08:22",
            extractedText = "Translate ‘hello’ to Spanish.",
            aiResponse = "‘hello’ in Spanish is ‘hola’."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
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
                        Text(item.timestamp, fontSize = 12.sp, color = Color.Gray)
                        Spacer(Modifier.height(4.dp))
                        Text("Extracted: ${item.extractedText}", fontSize = 16.sp)
                        Spacer(Modifier.height(2.dp))
                        Text("AI: ${item.aiResponse}", fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            if (history.isEmpty()) {
                item {
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
    }
}
