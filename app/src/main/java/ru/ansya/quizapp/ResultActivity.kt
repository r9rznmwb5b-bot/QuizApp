package ru.ansya.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        val totalQuestions = intent.getIntExtra(EXTRA_TOTAL_QUESTIONS, 0)

        findViewById<TextView>(R.id.resultTextView).text =
            getString(R.string.result_summary, score, totalQuestions)

        findViewById<Button>(R.id.restartQuizButton).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        }
    }

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TOTAL_QUESTIONS = "extra_total_questions"
    }
}
