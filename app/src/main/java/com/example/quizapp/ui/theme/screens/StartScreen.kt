package com.example.quizapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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

@Composable
fun StartScreen(onStart: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    scale = animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f).value

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
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .background(
                    color = Color.White.copy(alpha = 0.10f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Quiz Icon",
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF00C6FF)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Quiz Master",
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = Color(0xFF00C6FF),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bilgini Test Et, Zirveye Ulaş!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .scale(scale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C6FF)
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Başla",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 22.sp,
                    color = Color.White
                )
            }
        }
    }
}
