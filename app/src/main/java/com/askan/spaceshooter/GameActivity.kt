package com.askan.spaceshooter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {
    private lateinit var game : Game
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = Game(this)
        setContentView(game)
    }

    override fun onPause() {
        game.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        game.resume()
    }
}