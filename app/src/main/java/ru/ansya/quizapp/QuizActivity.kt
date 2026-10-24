package ru.ansya.quizapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {
    private val questionList = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        loadQuestionsFromAssets()
        showFirstQuestion()
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

    private fun showFirstQuestion() {
        val question = questionList.firstOrNull() ?: return

        findViewById<TextView>(R.id.progressTextView).text =
            getString(R.string.question_progress, 1, questionList.size)
        findViewById<TextView>(R.id.questionTextView).text = question.questionText

        val answerButtons = listOf(
            findViewById<Button>(R.id.answerOneButton),
            findViewById<Button>(R.id.answerTwoButton),
            findViewById<Button>(R.id.answerThreeButton),
            findViewById<Button>(R.id.answerFourButton),
        )
        answerButtons.forEachIndexed { index, button ->
            button.text = question.options[index]
        }
    }
}
