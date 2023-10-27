package com.askan.spaceshooter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.askan.spaceshooter.saved.scores.ScoresActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showHighscore()

        startGame()

        scoresList()
    }

    fun showHighscore() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val maxDistanceTraveled = prefs.getInt(LONGEST_DIST, 0)
        val highscore = findViewById<TextView>(R.id.highscore)
        val tempHighScore = resources.getString(R.string.longest_traveled) + maxDistanceTraveled.toString()
        highscore.text = tempHighScore
    }

    fun scoresList() {
        findViewById<Button>(R.id.scoresButton)?.setOnClickListener {
            val intent = Intent(this, ScoresActivity::class.java)
            startActivity(intent)
        }
    }

    fun startGame() {
        findViewById<Button>(R.id.startGameButton)?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}