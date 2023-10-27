package com.askan.spaceshooter.entities

import android.content.Context
import com.askan.spaceshooter.Jukebox
import com.askan.spaceshooter.R
import com.askan.spaceshooter.RNG
import com.askan.spaceshooter.SFX
import com.askan.spaceshooter.STAGE_HEIGHT
import com.askan.spaceshooter.STAGE_WIDTH
import com.askan.spaceshooter.playerSpeed

const val ENEMY_HEIGHT = 50
const val ENEMY_SPAWN_OFFSET = STAGE_WIDTH *2
class Enemy(context: Context, jukebox: Jukebox) : BitmapEntity(context, jukebox) {
    init {
        var id = R.drawable.enemy_1
        when(RNG.nextInt(5)){
            0 -> id = R.drawable.enemy_hori_1
            1 -> id = R.drawable.enemy_hori_2
            2 -> id = R.drawable.enemy_hori_3
            3 -> id = R.drawable.enemy_hori_4
            4 -> id = R.drawable.enemy_hori_5
        }
        val bmp = loadBitmap(context.resources, id, ENEMY_HEIGHT)
        setSprite(flipVertically(bmp))
        respawn()
    }

    override fun respawn() {
        x = (STAGE_WIDTH + RNG.nextInt(ENEMY_SPAWN_OFFSET)).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT - ENEMY_HEIGHT).toFloat()
    }

    override fun onCollision(that: Entity) {
        if(that is Player) jukebox.play(SFX.crashed, 0)
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