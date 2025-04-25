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
fun QuizSelectionScreen(quizzes: Map<String, List<Question>>, onQuizSelected: (String) -> Unit) {
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
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .background(
                    color = Color.White.copy(alpha = 0.10f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Sınavını Seç",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = Color(0xFF00C6FF),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(quizzes.keys.size) { index ->
                    val quizName = quizzes.keys.toList()[index]
                    var isPressed by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 8.dp)
                            .scale(scale)
                            .clickable {
                                isPressed = true
                                onQuizSelected(quizName)
                                isPressed = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.18f)
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text(
                            text = quizName,
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF00C6FF)
                        )
                    }
                }
            }
        }
    }
}
