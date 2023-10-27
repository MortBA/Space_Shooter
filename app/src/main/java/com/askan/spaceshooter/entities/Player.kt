package com.askan.spaceshooter.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.math.MathUtils.clamp
import com.askan.spaceshooter.Jukebox
import com.askan.spaceshooter.NUM_SHOOTING_SFX
import com.askan.spaceshooter.R
import com.askan.spaceshooter.RNG
import com.askan.spaceshooter.SFX
import com.askan.spaceshooter.STAGE_HEIGHT
import com.askan.spaceshooter.playerSpeed

const val PLAYER_HEIGHT = 60
const val ACCELERATION = 1.1f
const val MIN_VELOCITY = 0.1f
const val MAX_VELOCITY = 20f
const val GRAVITY = 2f
const val LIFT = -(GRAVITY *2f)
const val DRAG = 0.97f
const val START_HEALTH = 3
const val START_AMMO = 3
const val START_POSITION = 10f
const val ALPHA_OPAQUE = 255
const val ALPHA_TRANSPARENT = 0
const val SECOND = 1000
const val BULLET_COUNT = 5

class Player(context: Context, jukebox: Jukebox) : BitmapEntity(context, jukebox) {
    private var timeHit : Long = 0
    var laserShots = ArrayList<Laser>()
    var isBoosting = false
    var health = START_HEALTH
    var ammo = START_AMMO
    var alpha: Int = ALPHA_OPAQUE
    var startTime = System.currentTimeMillis()
    init {
        setSprite(loadBitmap(context.resources, R.drawable.player_hori_1, PLAYER_HEIGHT))
        respawn()
        for(i in 0 until BULLET_COUNT) {
            laserShots.add(Laser(context, jukebox, this))
        }
    }

    override fun respawn() {
        health = START_HEALTH
        ammo = START_AMMO
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

        if ((timeHit + SECOND) > System.currentTimeMillis()) {
            if(alpha == ALPHA_TRANSPARENT) {
                alpha = ALPHA_OPAQUE
            }else {
                alpha = ALPHA_TRANSPARENT
            }
        }else {
            alpha = ALPHA_OPAQUE
        }

        for(laser in laserShots) {
            laser.update()
        }

    }
    override fun onCollision(that: Entity) {
        if ((timeHit + SECOND) < System.currentTimeMillis()) {
            timeHit = System.currentTimeMillis()
            health--
        }
        if(health < 1) {
            jukebox.stop(SFX.engine_on)
            jukebox.play(SFX.player_destroyed, 0)
            isBoosting = false
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        for(laser in laserShots) {
            laser.render(canvas, paint)
        }
        paint.alpha = alpha
        canvas.drawBitmap(bitmap, x, y, paint)
        super.render(canvas, paint)
    }

    fun boost(isBoost: Boolean) {
        if(isBoost) {
            jukebox.play(SFX.engine_on, -1)
            isBoosting = true
        }else {
            isBoosting = false
            jukebox.stop(SFX.engine_on)
        }
    }
//TODO: RANDOM BUG WHERE SOMETIMES IT DOES NOT SHOOT
    fun shoot() {
        if(ammo > 0) {
            for(laser in laserShots) {
                if(!laser.isShot) {
                    laser.isShot = true
                    ammo--
                    jukebox.play(SFX.shootingList[RNG.nextInt(NUM_SHOOTING_SFX)], 0)
                    break
                }
            }
        }
    }
}

const val LASER_HEIGHT = 30
class Laser(context: Context, jukebox: Jukebox, player: Player) : BitmapEntity(context, jukebox) {
    private val player: Player
    var isShot = false
    var alpha = 0
    init {
        this.player = player
        var id = R.drawable.bullet_1
        when(RNG.nextInt(5)){
            0 -> id = R.drawable.bullet_1
            1 -> id = R.drawable.bullet_2
            2 -> id = R.drawable.bullet_3
            3 -> id = R.drawable.bullet_4
            4 -> id = R.drawable.bullet_5
        }
        val bmp = loadBitmap(context.resources, id, LASER_HEIGHT)
        setSprite(flipVertically(bmp))
        respawn()
    }

    override fun respawn() {
        x = player.x
        y = player.y
        alpha = 0
        velX = 0f
        isShot = false
    }

    override fun onCollision(that: Entity) {
        if(that is Enemy){
            jukebox.play(SFX.crashed, 0)
            that.respawn()
            respawn()
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        paint.alpha = alpha
        canvas.drawBitmap(bitmap, x, y, paint)
        super.render(canvas, paint)
    }

    override fun update() {
        if(isShot) {
            alpha = 255
            velX = MAX_VELOCITY + player.velX
            x += velX
        }else{
            x = player.x
            y = player.y
        }
        val screenWidth = context.resources.displayMetrics.widthPixels
        if(x >= screenWidth) {
            respawn()
        }
    }
}