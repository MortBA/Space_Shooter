package com.askan.spaceshooter.saved.scores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.askan.spaceshooter.R

class ScoresActivity : AppCompatActivity() {
    private val savedScores = SavedScores(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)
        savedScores.getScores()
        populateRecyclerView()
    }

    private fun populateRecyclerView()  {
        // Set up RecyclerView in your activity
        val recyclerView: RecyclerView = findViewById(R.id.scoresList)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val itemList = listOf("Hello", "Item 2", "Item 3") // Your data
        val adapter = RecyclerAdapter(itemList)
        recyclerView.adapter = adapter
    }
}