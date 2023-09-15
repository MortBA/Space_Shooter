package com.askan.spaceshooter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

const val MAX_RGB_VAL = 256
const val MAX_STAR_SIZE = 6
const val MIN_STAR_SIZE = 2
class Star : Entity() {
    private val color = Color.argb(ALPHA_OPAQUE, RNG.nextInt(MAX_RGB_VAL), RNG.nextInt(MAX_RGB_VAL), RNG.nextInt(
        MAX_RGB_VAL))
    private val radius = (RNG.nextInt(MAX_STAR_SIZE)+ MIN_STAR_SIZE).toFloat()

    init {
        x = RNG.nextInt(STAGE_WIDTH).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT).toFloat()
        width = radius * 2f
        height = width
    }

    override fun respawn() {
        x = RNG.nextInt(STAGE_WIDTH).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT).toFloat()
        width = radius * 2f
        height = width
    }

    override fun update() {
        super.update()
        x += -playerSpeed * (radius / MAX_STAR_SIZE + 1)
        if (right() < 0) {
            setLeft(STAGE_WIDTH.toFloat())
            setTop(RNG.nextInt(STAGE_HEIGHT - height.toInt()).toFloat())
        }
        if (top() > STAGE_HEIGHT) setBottom(0f)
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        paint.color = color
        canvas.drawCircle(x, y, radius, paint)
    }
}