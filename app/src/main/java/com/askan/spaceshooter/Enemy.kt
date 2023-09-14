package com.askan.spaceshooter

import android.content.res.Resources

const val ENEMY_HEIGHT = 60
const val ENEMY_SPAWN_OFFSET = STAGE_WIDTH*2
class Enemy(res : Resources) : BitmapEntity() {
    init {
        var id = R.drawable.enemy_1
        when(RNG.nextInt(5) + 1){
            1 -> id = R.drawable.enemy_1
            2 -> id = R.drawable.enemy_1
            3 -> id = R.drawable.enemy_1
            4 -> id = R.drawable.enemy_1
            5 -> id = R.drawable.enemy_1
        }
        val bmp = loadBitmap(res, id, ENEMY_HEIGHT)
        setSprite(flipVertically(bmp))
        respawn()
    }

    override fun respawn() {
        x = (STAGE_WIDTH + RNG.nextInt(ENEMY_SPAWN_OFFSET)).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT- ENEMY_HEIGHT).toFloat()
    }

    override fun onCollision(that: Entity) {
        respawn()
    }

    override fun update() {
        velX = -playerSpeed
        x += velX
        if(right() < 0) {
            respawn()
        }
    }
}