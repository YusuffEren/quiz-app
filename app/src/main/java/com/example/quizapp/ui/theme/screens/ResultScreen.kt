package com.example.quizapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity

@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
    onBackToSelection: () -> Unit
) {
    val stars = score
    val message = when (stars) {
        5 -> "Muhteşem! Sen bir Quiz Master’sın! 🏆"
        4 -> "Harika! Çok iyi bir performans! 🌟"
        3 -> "İyi iş! Daha da iyi olabilirsin! 💪"
        1, 2 -> "Pes etme! Bir dahaki sefere daha iyi olacak! 🚀"
        else -> "Hiç doğru cevabın yok, daha çok çalışmalısın! 📚"
    }
    var isRestartPressed by remember { mutableStateOf(false) }
    var isBackPressed by remember { mutableStateOf(false) }
    var isExitPressed by remember { mutableStateOf(false) }
    val restartScale by animateFloatAsState(if (isRestartPressed) 0.97f else 1f)
    val backScale by animateFloatAsState(if (isBackPressed) 0.97f else 1f)
    val exitScale by animateFloatAsState(if (isExitPressed) 0.97f else 1f)

    val context = LocalContext.current
    val activity = context as? ComponentActivity

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
                .align(Alignment.Center)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .background(
                    color = Color.White.copy(alpha = 0.10f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f)),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sonuç",
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF00C6FF)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (totalQuestions > 0) "$score / $totalQuestions" else "Geçersiz Sınav",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF00C6FF)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                modifier = Modifier.size(32.dp),
                                tint = if (index < stars) Color(0xFF00C6FF) else Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (totalQuestions > 0) message else "Sınav verileri yüklenemedi.",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .scale(restartScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C6FF)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Yeniden Başla",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 22.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onBackToSelection,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .scale(backScale),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Sınav Seçimine Dön",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                    color = Color(0xFF00C6FF)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    isExitPressed = true
                    activity?.finish()
                    isExitPressed = false
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .scale(exitScale),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Çıkış Yap",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                    color = Color(0xFF00C6FF)
                )
            }
        }
    }
}
