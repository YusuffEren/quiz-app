package com.example.quizapp.ui

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import com.example.quizapp.data.Question
import com.example.quizapp.data.QuizRepository
import com.example.quizapp.ui.screens.QuizScreen
import com.example.quizapp.ui.screens.QuizSelectionScreen
import com.example.quizapp.ui.screens.ResultScreen
import com.example.quizapp.ui.screens.StartScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QuizApp() {
    val isDarkTheme = LocalConfiguration.current.isNightModeActive
    val lightColors = lightColorScheme(
        primary = Color(0xFF3F51B5), // Koyu mor
        secondary = Color(0xFFFFC107), // Altın sarısı
        background = Color(0xFFF5F5F5),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )
    val darkColors = darkColorScheme(
        primary = Color(0xFF7986CB), // Daha açık mor
        secondary = Color(0xFFFFD54F), // Daha yumuşak altın
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(colorScheme = if (isDarkTheme) darkColors else lightColors) {
        val context = LocalContext.current
        val repository = QuizRepository(context)
        var screenState by remember { mutableStateOf("start") }
        var currentQuestionIndex by remember { mutableStateOf(0) }
        var score by remember { mutableStateOf(0) }
        var userAnswers by remember { mutableStateOf(mutableListOf<Int?>()) }
        var selectedQuiz by remember { mutableStateOf<String?>(null) }
        val quizzes = repository.loadQuestionBanks()
        val questionTime = 10
        var totalQuizTime by remember { mutableStateOf(0) }
        var quizTimeLeft by remember { mutableStateOf(totalQuizTime) }
        var questionTimeLeft by remember { mutableStateOf(questionTime) }
        val coroutineScope = rememberCoroutineScope()
        var isQuizActive by remember { mutableStateOf(false) }

        LaunchedEffect(isQuizActive) {
            if (isQuizActive && selectedQuiz != null) {
                while (quizTimeLeft > 0 && isQuizActive) {
                    delay(1000)
                    quizTimeLeft--
                    if (quizTimeLeft <= 0) {
                        isQuizActive = false
                        screenState = "result"
                        Log.d("QuizApp", "Quiz ended, transitioning to result. Score: $score")
                        repository.saveScore(score, quizzes[selectedQuiz]?.size ?: 0)
                    }
                }
            }
        }

        LaunchedEffect(isQuizActive, currentQuestionIndex) {
            if (isQuizActive && selectedQuiz != null && currentQuestionIndex < (quizzes[selectedQuiz]?.size ?: 0)) {
                questionTimeLeft = questionTime
                while (questionTimeLeft > 0 && isQuizActive && currentQuestionIndex < (quizzes[selectedQuiz]?.size ?: 0)) {
                    delay(1000)
                    questionTimeLeft--
                    if (questionTimeLeft <= 0) {
                        userAnswers.add(null)
                        currentQuestionIndex++
                        if (currentQuestionIndex >= (quizzes[selectedQuiz]?.size ?: 0)) {
                            isQuizActive = false
                            screenState = "result"
                            Log.d("QuizApp", "All questions answered, transitioning to result. Score: $score")
                            repository.saveScore(score, quizzes[selectedQuiz]?.size ?: 0)
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = if (isDarkTheme) listOf(Color(0xFF1A237E), Color(0xFF283593))
                        else listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9))
                    )
                )
        ) {
            Crossfade(
                targetState = screenState,
                animationSpec = tween(500)
            ) { targetScreen ->
                when (targetScreen) {
                    "start" -> StartScreen(
                        onStart = { screenState = "selectQuiz" }
                    )
                    "selectQuiz" -> QuizSelectionScreen(
                        quizzes = quizzes,
                        onQuizSelected = { quizName ->
                            selectedQuiz = quizName
                            screenState = "quiz"
                            isQuizActive = true
                            userAnswers = MutableList(quizzes[quizName]?.size ?: 0) { null }
                            totalQuizTime = (quizzes[quizName]?.size ?: 0) * questionTime
                            quizTimeLeft = totalQuizTime
                            questionTimeLeft = questionTime
                            currentQuestionIndex = 0
                            score = 0
                            Log.d("QuizApp", "Quiz started: $quizName")
                        }
                    )
                    "quiz" -> selectedQuiz?.let { quizName ->
                        if (quizzes[quizName] != null && currentQuestionIndex < quizzes[quizName]!!.size) {
                            QuizScreen(
                                question = quizzes[quizName]!![currentQuestionIndex],
                                questionNumber = currentQuestionIndex + 1,
                                totalQuestions = quizzes[quizName]!!.size,
                                questionTimeLeft = questionTimeLeft,
                                quizTimeLeft = quizTimeLeft,
                                onAnswerSelected = { answerIndex ->
                                    userAnswers[currentQuestionIndex] = answerIndex
                                    if (answerIndex == quizzes[quizName]!![currentQuestionIndex].correctAnswer) {
                                        score++
                                    }
                                    currentQuestionIndex++
                                    if (currentQuestionIndex >= quizzes[quizName]!!.size) {
                                        isQuizActive = false
                                        screenState = "result"
                                        Log.d("QuizApp", "Quiz completed, transitioning to result. Score: $score")
                                        repository.saveScore(score, quizzes[quizName]!!.size)
                                    }
                                }
                            )
                        } else {
                            screenState = "result"
                            Log.e("QuizApp", "Invalid quiz data for $quizName")
                        }
                    }
                    "result" -> ResultScreen(
                        score = score,
                        totalQuestions = quizzes[selectedQuiz]?.size ?: 0,
                        onRestart = {
                            screenState = "start"
                            currentQuestionIndex = 0
                            score = 0
                            totalQuizTime = 0
                            quizTimeLeft = totalQuizTime
                            questionTimeLeft = questionTime
                            userAnswers = mutableListOf()
                            isQuizActive = false
                            selectedQuiz = null
                            Log.d("QuizApp", "Restarting app")
                        },
                        onBackToSelection = {
                            screenState = "selectQuiz"
                            currentQuestionIndex = 0
                            score = 0
                            totalQuizTime = 0
                            quizTimeLeft = totalQuizTime
                            questionTimeLeft = questionTime
                            userAnswers = mutableListOf()
                            isQuizActive = false
                            selectedQuiz = null
                            Log.d("QuizApp", "Returning to quiz selection")
                        }
                    )
                }
            }
        }
    }
}