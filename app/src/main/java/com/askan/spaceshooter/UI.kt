package com.askan.spaceshooter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class UI(canvas: Canvas, paint: Paint) {

    private var textSize = 48f
    private var textMargin = 10f
    private var textColor = Color.WHITE
    private val canvas: Canvas
    private val paint: Paint
    init {
        this.canvas = canvas
        this.paint = paint
    }

    fun renderHud(isGameOver: Boolean, distanceTraveled: Int, playerHealth : Int) {

        paint.color = textColor
        paint.textSize = textSize

        if(!isGameOver) {
            gameplayHud(playerHealth, distanceTraveled)
        }else {
            restartUI()
        }
    }

    private fun restartUI() {
        paint.textAlign = Paint.Align.CENTER
        val centerX = STAGE_WIDTH/2f
        val centerY = STAGE_HEIGHT/2f
        canvas.drawText("GAME OVER!!!!", centerX, centerY, paint)
        canvas.drawText("(press to restart)", centerX, centerY+textSize, paint)
    }

    private fun gameplayHud(playerHealth: Int, distanceTraveled: Int) {
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Health: $playerHealth", textMargin, textSize, paint)
        canvas.drawText("Traveled: $distanceTraveled", textMargin, textSize*2, paint)
    }

    fun setTextSize(size: Float) {
        this.textSize = size
    }
    fun setTextMarginSize(size:Float) {
        this.textMargin = size
    }

    fun setTextColor(color: Int) {
        this.textColor = color
    }
}