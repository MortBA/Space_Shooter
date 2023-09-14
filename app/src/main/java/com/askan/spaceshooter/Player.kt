package com.askan.spaceshooter

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.math.MathUtils.clamp

const val PLAYER_HEIGHT = 60
const val ACCELERATION = 1.1f
const val MIN_VELOCITY = 0.1f
const val MAX_VELOCITY = 20f
const val GRAVITY = 2f
const val LIFT = -(GRAVITY*2f)
const val DRAG = 0.97f
const val START_HEALTH = 3
const val START_POSITION = 10f

class Player(res: Resources) : BitmapEntity() {
    private var timeHit : Long = 0
    var health = START_HEALTH
    var alpha: Int = 255
    init {
        setSprite(loadBitmap(res, R.drawable.player_hori_1, PLAYER_HEIGHT))
        respawn()
    }

    override fun respawn() {
        health = START_HEALTH
        x = START_POSITION
    }

    override fun update() {
        velX *= DRAG
        velY += GRAVITY
        if(isBoosting) {
            velX *= ACCELERATION
            velY += LIFT
        }
        velX = clamp(velX, MIN_VELOCITY, MAX_VELOCITY)
        velY = clamp(velY, -MAX_VELOCITY, MAX_VELOCITY)

        y += velY
        playerSpeed = velX

        if(bottom() > STAGE_HEIGHT) {
            setBottom(STAGE_HEIGHT.toFloat())
        }else if(top() < 0f) {
            setTop(0f)
        }

        if ((timeHit + 1000) < System.currentTimeMillis()) {
            alpha = 255
        }

    }
    override fun onCollision(that: Entity) {
        alpha = 100
        if ((timeHit + 1000) < System.currentTimeMillis()) {
            timeHit = System.currentTimeMillis()
            health--
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        paint.alpha = alpha // Set the alpha value before drawing the bitmap
        canvas.drawBitmap(bitmap, x, y, paint)
        super.render(canvas, paint)
    }
}