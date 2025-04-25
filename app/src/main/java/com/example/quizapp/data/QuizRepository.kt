package com.example.quizapp.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray

class QuizRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("QuizResults", Context.MODE_PRIVATE)

    fun loadQuestionBanks(): Map<String, List<Question>> {
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
                },
                {
                    "question": "Dünyanın en uzun nehri hangisidir?",
                    "options": ["Amazon", "Nil", "Yangtze", "Mississippi"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi ülke en fazla nüfusa sahiptir?",
                    "options": ["ABD", "Hindistan", "Çin", "Rusya"],
                    "correctAnswer": 2
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
                },
                {
                    "question": "Bir dairenin alanı nasıl hesaplanır?",
                    "options": ["πr", "2πr", "πr²", "r²"],
                    "correctAnswer": 2
                },
                {
                    "question": "12’nin karekökü kaçtır?",
                    "options": ["2", "3", "4", "6"],
                    "correctAnswer": 2
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
                },
                {
                    "question": "Yerçekimi kuvvetini ilk kim tanımlamıştır?",
                    "options": ["Einstein", "Newton", "Galileo", "Tesla"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi element periyodik tabloda 'H' sembolü ile gösterilir?",
                    "options": ["Helyum", "Hidrojen", "Halojen", "Hafniyum"],
                    "correctAnswer": 1
                }
            ]
        """
        val history = """
            [
                {
                    "question": "Osmanlı Devleti'nin kurucusu kimdir?",
                    "options": ["Fatih Sultan Mehmet", "Yavuz Sultan Selim", "Osman Gazi", "Kanuni Sultan Süleyman"],
                    "correctAnswer": 2
                },
                {
                    "question": "Fransız Devrimi hangi yılda başlamıştır?",
                    "options": ["1789", "1815", "1756", "1848"],
                    "correctAnswer": 0
                },
                {
                    "question": "İlk Türk devletlerinden hangisi Göktürkler'dir?",
                    "options": ["Hunlar", "Göktürkler", "Uygurlar", "Avarlar"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi savaş Birinci Dünya Savaşı'nı sona erdirmiştir?",
                    "options": ["Versay Antlaşması", "Çanakkale Savaşı", "Mondros Ateşkesi", "Lozan Antlaşması"],
                    "correctAnswer": 2
                },
                {
                    "question": "Magna Carta hangi ülkede imzalanmıştır?",
                    "options": ["Fransa", "İngiltere", "Almanya", "İtalya"],
                    "correctAnswer": 1
                }
            ]
        """
        val geography = """
            [
                {
                    "question": "Dünyanın en yüksek dağı hangisidir?",
                    "options": ["Kilimanjaro", "Everest", "Alpler", "Aconcagua"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi okyanus dünyanın en büyüğüdür?",
                    "options": ["Atlantik", "Hint", "Pasifik", "Arktik"],
                    "correctAnswer": 2
                },
                {
                    "question": "Türkiye'nin en uzun kıyı şeridi hangi denizdedir?",
                    "options": ["Karadeniz", "Akdeniz", "Ege Denizi", "Marmara Denizi"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi şehir iki kıta üzerine kurulmuştur?",
                    "options": ["Lizbon", "İstanbul", "Kahire", "Bangkok"],
                    "correctAnswer": 1
                },
                {
                    "question": "Amazon yağmur ormanları hangi kıtadadır?",
                    "options": ["Afrika", "Asya", "Güney Amerika", "Avustralya"],
                    "correctAnswer": 2
                }
            ]
        """
        val sports = """
            [
                {
                    "question": "Futbol Dünya Kupası'nı en çok hangi ülke kazanmıştır?",
                    "options": ["Almanya", "Brezilya", "İtalya", "Arjantin"],
                    "correctAnswer": 1
                },
                {
                    "question": "Olimpiyat Oyunları kaç yılda bir düzenlenir?",
                    "options": ["2", "3", "4", "5"],
                    "correctAnswer": 2
                },
                {
                    "question": "Hangi spor 'kral sporu' olarak bilinir?",
                    "options": ["Basketbol", "Futbol", "Tenis", "Atletizm"],
                    "correctAnswer": 3
                },
                {
                    "question": "Wimbledon turnuvası hangi sporda düzenlenir?",
                    "options": ["Golf", "Tenis", "Kriket", "Rugby"],
                    "correctAnswer": 1
                },
                {
                    "question": "Hangi Türk sporcu NBA'de oynamıştır?",
                    "options": ["Hidayet Türkoğlu", "Arda Turan", "burak yılmaz", "Rüştü Reçber"],
                    "correctAnswer": 0
                }
            ]
        """
        return mapOf(
            "Genel Kültür" to jsonArrayToQuestions(JSONArray(generalKnowledge)),
            "Matematik" to jsonArrayToQuestions(JSONArray(math)),
            "Bilim" to jsonArrayToQuestions(JSONArray(science)),
            "Tarih" to jsonArrayToQuestions(JSONArray(history)),
            "Coğrafya" to jsonArrayToQuestions(JSONArray(geography)),
            "Spor" to jsonArrayToQuestions(JSONArray(sports))
        )
    }

    private fun jsonArrayToQuestions(jsonArray: JSONArray): List<Question> {
        val questions = mutableListOf<Question>()
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            val optionsArray = json.getJSONArray("options")
            val options = List(optionsArray.length()) { optionsArray.getString(it) }
            questions.add(
                Question(
                    question = json.getString("question"),
                    options = options,
                    correctAnswer = json.getInt("correctAnswer")
                )
            )
        }
        return questions
    }

    fun saveScore(score: Int, totalQuestions: Int) {
        try {
            val editor = sharedPreferences.edit()
            val previousScores = sharedPreferences.getString("scores", "") ?: ""
            val newScore = "Skor: $score/$totalQuestions"
            editor.putString("scores", "$previousScores\n$newScore")
            editor.apply()
            Log.d("QuizRepository", "Score saved: $newScore")
        } catch (e: Exception) {
            Log.e("QuizRepository", "Error saving score: ${e.message}")
        }
    }
}
