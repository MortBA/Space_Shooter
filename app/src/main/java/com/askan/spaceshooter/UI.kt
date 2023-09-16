package com.askan.spaceshooter

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class UI(context: Context, canvas: Canvas, paint: Paint) {

    private var textSize = 48f
    private var textMargin = 10f
    private var textColor = Color.WHITE
    private val context: Context
    private val canvas: Canvas
    private val paint: Paint
    private val resources : Resources
    init {
        this.context = context
        this.canvas = canvas
        this.paint = paint
        resources = context.resources
    }

    fun renderHud(isGameOver: Boolean, distanceTraveled: Int, playerHealth : Int, playerAmmo: Int) {

        paint.color = textColor
        paint.textSize = textSize

        if(!isGameOver) {
            gameplayHud(playerHealth, distanceTraveled, playerAmmo)
        }else {
            restartUI()
        }
    }

    private fun restartUI() {
        paint.textAlign = Paint.Align.CENTER
        val centerX = STAGE_WIDTH/2f
        val centerY = STAGE_HEIGHT/2f
        canvas.drawText(resources.getString(R.string.game_over), centerX, centerY, paint)
        canvas.drawText(resources.getString(R.string.restart), centerX, centerY+textSize, paint)
    }

    private fun gameplayHud(playerHealth: Int, distanceTraveled: Int, playerAmmo: Int) {
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("${resources.getString(R.string.health)} $playerHealth ${resources.getString(R.string.ammo)} $playerAmmo", textMargin, textSize, paint)
        canvas.drawText("${resources.getString(R.string.traveled)} $distanceTraveled", textMargin, textSize*2, paint)
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