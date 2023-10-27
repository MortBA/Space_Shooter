package com.askan.spaceshooter.saved.scores

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

const val FILE_NAME = "scores.txt"
class SavedScores(context: Context) {
    private val context: Context
    init {
        this.context = context
    }
    fun setNewScore(): Int {
        return 0
    }

    fun getScores(): Map<String, Int>? {
        return null
    }

    fun readFromInternalStorage(fileName: String): String? {
        try {
            val fileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var text: String? = null
            while (bufferedReader.readLine().also { text = it } != null) {
                stringBuilder.append(text)
            }
            bufferedReader.close()
            return stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun writeToInternalStorage(fileName: String, content: String): Boolean {
        try {
            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(content)
            bufferedWriter.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}