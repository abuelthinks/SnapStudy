package com.kiarah.snapstudy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SnapStudyViewModel,
    onTakePhoto: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val userMode = viewModel.userMode.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Top bar with Title and History Button
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SnapStudy",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Scan History",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Centered Mode Selector
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Mode: ${userMode.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 10.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { viewModel.toggleUserMode() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Switch to ${if (userMode == UserMode.STUDENT) "Parent" else "Student"} Mode")
                }

                // Parent/Student Tip Card
                val parentTips = listOf(
                    "Ask your child to explain their homework solutions aloud.",
                    "Encourage independent problem solving, but assist when really needed.",
                    "Praise progress, not just right answers."
                )
                val studentTips = listOf(
                    "Read instructions carefully before starting homework.",
                    "Take short breaks after solving a few problems.",
                    "Don't be afraid to ask for help from parents or teachers."
                )
                val currentTip = if (userMode == UserMode.PARENT)
                    parentTips[0]
                else
                    studentTips[0]

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            when (userMode) {
                                UserMode.PARENT -> "Parent Tip"
                                UserMode.STUDENT -> "Student Tip"
                            },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            currentTip,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Camera Icon Button - Bottom Center
        FloatingActionButton(
            onClick = onTakePhoto,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 44.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = "Take Photo",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
