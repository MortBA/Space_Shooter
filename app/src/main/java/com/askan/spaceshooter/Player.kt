package com.askan.spaceshooter

import android.content.res.Resources
import androidx.core.math.MathUtils.clamp

const val PLAYER_HEIGHT = 80
const val ACCELERATION = 1.1f
const val MIN_VELOCITY = 0.1f
const val MAX_VELOCITY = 20f
const val GRAVITY = 1.1f
const val LIFT = -(GRAVITY*2f)
const val DRAG = 0.97f
const val START_HEALTH = 3
const val START_POSITION = 10f

class Player(res: Resources) : BitmapEntity() {
    var health = START_HEALTH
    init {
        setSprite(loadBitmap(res, R.drawable.player, PLAYER_HEIGHT))
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

    }

    override fun onCollision(that: Entity) {
        health--
    }
}