package com.askan.spaceshooter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.startGameButton)?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val maxDistanceTraveled = prefs.getInt(LONGEST_DIST, 0)
        val highscore = findViewById<TextView>(R.id.highscore)
        val tempHighScore = resources.getString(R.string.longest_traveled) + maxDistanceTraveled.toString()
        highscore.text = tempHighScore
    }
}