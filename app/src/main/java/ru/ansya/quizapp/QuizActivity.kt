package ru.ansya.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {
    private val questionList = mutableListOf<Question>()
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnOptions: List<Button>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestionCount = findViewById(R.id.progressTextView)
        tvQuestionText = findViewById(R.id.questionTextView)
        btnOptions = listOf(
            findViewById(R.id.answerOneButton),
            findViewById(R.id.answerTwoButton),
            findViewById(R.id.answerThreeButton),
            findViewById(R.id.answerFourButton),
        )

        loadQuestionsFromAssets()
        showNextQuestion()

        btnOptions.forEachIndexed { index, button ->
            button.setOnClickListener { checkAnswer(index) }
        }
    }

    private fun loadQuestionsFromAssets() {
        BufferedReader(InputStreamReader(assets.open("questions.txt"))).use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                val parts = line.split("|").map(String::trim)
                if (parts.size != 6) continue

                val correctIndex = parts[5].toIntOrNull() ?: continue
                if (correctIndex !in 0..3) continue

                questionList.add(
                    Question(
                        questionText = parts[0],
                        options = parts.subList(1, 5),
                        correctIndex = correctIndex,
                    ),
                )
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex >= questionList.size) {
            val resultIntent = Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.EXTRA_SCORE, score)
                putExtra(ResultActivity.EXTRA_TOTAL_QUESTIONS, questionList.size)
            }
            startActivity(resultIntent)
            finish()
            return
        }

        val question = questionList[currentQuestionIndex]

        tvQuestionCount.text =
            getString(R.string.question_progress, currentQuestionIndex + 1, questionList.size)
        tvQuestionText.text = question.questionText

        btnOptions.forEachIndexed { index, button ->
            button.text = question.options[index]
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questionList[currentQuestionIndex]
        if (selectedIndex == currentQuestion.correctIndex) {
            score++
        }
        currentQuestionIndex++
        showNextQuestion()
    }
}
