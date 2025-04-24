package com.example.quizapp
// İMPORTLAR GÖZDEN GEÇİRİLDİ
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp()
        }
    }
}

@Composable
fun QuizApp() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("QuizResults", Context.MODE_PRIVATE)
    var screenState by remember { mutableStateOf("start") }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(mutableListOf<Int?>()) }
    var selectedQuiz by remember { mutableStateOf<String?>(null) }
    val quizzes = loadQuestionBanks()
    val totalQuizTime = 60 // her bölüm için üç soru var her soru içinde 10 saniye. Burada mantık hatası var geri dönüp bi bak
    val questionTime = 10
    var quizTimeLeft by remember { mutableStateOf(totalQuizTime) }
    var questionTimeLeft by remember { mutableStateOf(questionTime) }
    val coroutineScope = rememberCoroutineScope()
    var isQuizActive by remember { mutableStateOf(false) }

    LaunchedEffect(isQuizActive) {
        if (isQuizActive) {
            while (quizTimeLeft > 0 && isQuizActive) {
                delay(1000)
                quizTimeLeft--
                if (quizTimeLeft <= 0) {
                    isQuizActive = false
                    screenState = "result"
                    saveScore(sharedPreferences, score, quizzes[selectedQuiz]?.length() ?: 0)
                }
            }
        }
    }

    LaunchedEffect(isQuizActive, currentQuestionIndex) {
        if (isQuizActive && selectedQuiz != null && currentQuestionIndex < (quizzes[selectedQuiz]?.length() ?: 0)) {
            questionTimeLeft = questionTime
            while (questionTimeLeft > 0 && isQuizActive && currentQuestionIndex < (quizzes[selectedQuiz]?.length() ?: 0)) {
                delay(1000)
                questionTimeLeft--
                if (questionTimeLeft <= 0) {
                    userAnswers.add(null) // bu kısma dön. yazdım ama yapıyı anlamadan yazdım öğren burayı 2. videoda var.
                    currentQuestionIndex++
                    if (currentQuestionIndex >= (quizzes[selectedQuiz]?.length() ?: 0)) {
                        isQuizActive = false
                        screenState = "result"
                        saveScore(sharedPreferences, score, quizzes[selectedQuiz]?.length() ?: 0)
                    }
                }
            }
        }
    }

    when (screenState) {
        "start" -> StartScreen(
            onStart = {
                screenState = "selectQuiz"
            }
        )
        "selectQuiz" -> QuizSelectionScreen(
            quizzes = quizzes,
            onQuizSelected = { quizName ->
                selectedQuiz = quizName
                screenState = "quiz"
                isQuizActive = true
                userAnswers = MutableList(quizzes[quizName]?.length() ?: 0) { null }
                quizTimeLeft = totalQuizTime
                questionTimeLeft = questionTime
                currentQuestionIndex = 0
                score = 0
            }
        )
        "quiz" -> selectedQuiz?.let { quizName ->
            QuizScreen(
                question = quizzes[quizName]!!.getJSONObject(currentQuestionIndex),
                questionNumber = currentQuestionIndex + 1,
                totalQuestions = quizzes[quizName]!!.length(),
                questionTimeLeft = questionTimeLeft,
                quizTimeLeft = quizTimeLeft,
                onAnswerSelected = { answerIndex ->
                    userAnswers[currentQuestionIndex] = answerIndex
                    if (answerIndex == quizzes[quizName]!!.getJSONObject(currentQuestionIndex).getInt("correctAnswer")) {
                        score++
                    }
                    currentQuestionIndex++
                    if (currentQuestionIndex >= quizzes[quizName]!!.length()) {
                        isQuizActive = false
                        screenState = "result"
                        saveScore(sharedPreferences, score, quizzes[quizName]!!.length())
                    }
                }
            )
        }
        "result" -> ResultScreen(
            score = score,
            totalQuestions = quizzes[selectedQuiz]?.length() ?: 0,
            onRestart = {
                screenState = "start"
                currentQuestionIndex = 0
                score = 0
                quizTimeLeft = totalQuizTime
                questionTimeLeft = questionTime
                userAnswers = mutableListOf()
                isQuizActive = false
                selectedQuiz = null
            }
        )
    }
}

@Composable
fun StartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Uygulamasına Hoş Geldiniz!", //burası böyle iyi fazla oynamaya gerek yok.
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Başla", fontSize = 18.sp)
        }
    }
}

@Composable
fun QuizSelectionScreen(quizzes: Map<String, JSONArray>, onQuizSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // ilk seferde ortalamamışım her şey çok üstteydi bu ortalama işine dikkat et diğer sayfalarda da.
    ) {
        Text(
            text = "Bir Sınav Seçin",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(quizzes.keys.toList().size) { index ->
                val quizName = quizzes.keys.toList()[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onQuizSelected(quizName) },
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Text(
                        text = quizName,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    question: JSONObject,
    questionNumber: Int,
    totalQuestions: Int,
    questionTimeLeft: Int,
    quizTimeLeft: Int,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Soru $questionNumber / $totalQuestions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kalan Süre: $quizTimeLeft saniye",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = "Soru için kalan süre: $questionTimeLeft saniye",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = question.getString("question"),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        val options = question.getJSONArray("options")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp), // 300 yapınca ekrana sığmıyor. 4 şıkka göre şuan ideali bu 5 şıkka çıkarsa burayı düzenle.
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(options.length()) { index ->
                val option = options.getString(index)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onAnswerSelected(index) },
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Text(
                        text = option,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, totalQuestions: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Skorunuz: $score / $totalQuestions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart) {
            Text("Yeniden Başla", fontSize = 18.sp)
        }
    }
}

fun loadQuestionBanks(): Map<String, JSONArray> {
    val generalKnowledge = """
        [
            {
                "question": "Türkiye'nin başkenti neresidir?",
                "options": ["İstanbul", "Ankara", "İzmir", "Bursa"],
                "correctAnswer": 1
            },
            {
                "question": "Hangi hayvan 'ormanlar kralı' olarak bilinir?",
                "options": ["Kaplan", "Aslan", "Fil", "Ayı"],
                "correctAnswer": 1
            },
            {
                "question": "Bir yılda kaç gün vardır?",
                "options": ["365", "366", "360", "364"],
                "correctAnswer": 0
            }
        ]
    """
    val math = """
        [
            {
                "question": "2 + 2 kaç eder?",
                "options": ["3", "4", "5", "6"],
                "correctAnswer": 1
            },
            {
                "question": "5 x 3 kaç eder?",
                "options": ["12", "15", "18", "20"],
                "correctAnswer": 1
            },
            {
                "question": "Bir karenin çevresi 16 cm ise, bir kenarı kaç cm’dir?",
                "options": ["2", "4", "6", "8"],
                "correctAnswer": 1
            }
        ]
    """
    val science = """
        [
            {
                "question": "Hangi gezegen 'Kızıl Gezegen' olarak bilinir?",
                "options": ["Jüpiter", "Venüs", "Mars", "Merkür"],
                "correctAnswer": 2
            },
            {
                "question": "İnsan vücudunda kaç kemik vardır?",
                "options": ["206", "180", "250", "300"],
                "correctAnswer": 0
            },
            {
                "question": "Fotosentez sırasında bitkiler ne üretir?",
                "options": ["Karbondioksit", "Oksijen", "Azot", "Hidrojen"],
                "correctAnswer": 1
            }
        ]
    """
    return mapOf(
        "Genel Kültür" to JSONArray(generalKnowledge),
        "Matematik" to JSONArray(math),
        "Bilim" to JSONArray(science)
    )
}

fun saveScore(sharedPreferences: SharedPreferences, score: Int, totalQuestions: Int) {
    val editor = sharedPreferences.edit()
    val previousScores = sharedPreferences.getString("scores", "") ?: ""
    val newScore = "Skor: $score/$totalQuestions, Tarih: ${java.time.LocalDateTime.now()}" //burda bir hata var çözemedim teslimden önce tekrar bak.
    editor.putString("scores", "$previousScores\n$newScore")
    editor.apply()
}