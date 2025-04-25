package com.example.quizapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.Question

@Composable
fun QuizScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    questionTimeLeft: Int,
    quizTimeLeft: Int,
    onAnswerSelected: (Int) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(question) {
        selectedOption = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF232526), Color(0xFF414345))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .background(
                    color = Color.White.copy(alpha = 0.10f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Soru $questionNumber / $totalQuestions",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00C6FF)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { questionTimeLeft / 10f },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(10.dp),
                color = Color(0xFF00C6FF),
                trackColor = Color.White.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Kalan Süre: $questionTimeLeft sn",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f)),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF00C6FF)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(question.options.size) { index ->
                    val option = question.options[index]
                    val isSelected = selectedOption == index
                    var isPressed by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(vertical = 6.dp)
                            .scale(scale)
                            .clickable {
                                isPressed = true
                                selectedOption = index
                                onAnswerSelected(index)
                                isPressed = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF00C6FF).copy(alpha = 0.18f) else Color.White.copy(alpha = 0.12f)
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
                            fontSize = 20.sp,
                            color = if (isSelected) Color(0xFF00C6FF) else Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
