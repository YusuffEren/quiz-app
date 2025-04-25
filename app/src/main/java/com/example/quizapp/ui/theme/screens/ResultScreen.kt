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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
    onBackToSelection: () -> Unit
) {
    val stars = score  // Yıldızlar doğrudan doğru cevap sayısına bağlı
    val message = when (stars) {
        5 -> "Muhteşem! Sen bir Quiz Master’sın! 🏆"
        4 -> "Harika! Çok iyi bir performans! 🌟"
        3 -> "İyi iş! Daha da iyi olabilirsin! 💪"
        1, 2 -> "Pes etme! Bir dahaki sefere daha iyi olacak! 🚀"
        else -> "Hiç doğru cevabın yok, daha çok çalışmalısın! 📚"
    }
    var isRestartPressed by remember { mutableStateOf(false) }
    var isBackPressed by remember { mutableStateOf(false) }
    val restartScale by animateFloatAsState(if (isRestartPressed) 0.97f else 1f)
    val backScale by animateFloatAsState(if (isBackPressed) 0.97f else 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sonuç",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (totalQuestions > 0) "$score / $totalQuestions" else "Geçersiz Sınav",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
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
                            tint = if (index < stars) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if (totalQuestions > 0) message else "Sınav verileri yüklenemedi.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(64.dp)
                .scale(restartScale)
                .clickable {
                    isRestartPressed = true
                    onRestart()
                    isRestartPressed = false
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Yeniden Başla",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onBackToSelection,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(64.dp)
                .scale(backScale)
                .clickable {
                    isBackPressed = true
                    onBackToSelection()
                    isBackPressed = false
                },
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Sınav Seçimine Dön",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}