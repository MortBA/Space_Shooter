package com.askan.spaceshooter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock.uptimeMillis
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.Random

const val STAGE_WIDTH = 1080
const val STAGE_HEIGHT = 720
const val STAR_COUNT = 40
const val ENEMY_COUNT = 3
const val POWER_UP_COUNT = 2
val RNG = Random(uptimeMillis())
@Volatile var playerSpeed = 0f

const val PREFS = "com.askan.spaceshooter"
const val LONGEST_DIST = "longest_distance"
//TODO: Game design changes: Power-Ups spawn rate, Speed over time, Enemy spawn rate over time

class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private lateinit var gameThread : Thread
    private var isGameOver = false
    private var entities = ArrayList<Entity>()
    private lateinit var canvas: Canvas
    private val paint = Paint()
    private var distanceTraveled = 0
    private var maxDistanceTraveled = 0
    private val jukebox = Jukebox(context.assets)
    private val player = Player(context, jukebox)
    private lateinit var UI: UI


    @Volatile
    private var isRunning = false

    init {
        holder.addCallback(this)
        holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) {
            entities.add(Star())
        }
        for(i in 0 until ENEMY_COUNT) {
            entities.add(Enemy(context, jukebox))
        }

        entities.add(Ammo(context, jukebox))
        entities.add(Repair(context, jukebox))

    }

    override fun run() {
        while (isRunning) {
            update()
            render()
        }
    }

    private fun render() {

        canvas = aquireAndLockCanvas() ?: return
        canvas.drawColor(Color.BLACK)

        for(entity in entities) {
            entity.render(canvas, paint)
        }

        player.render(canvas, paint)

        UI = UI(context, canvas, paint)
        UI.renderHud(isGameOver, distanceTraveled, player.health, player.ammo)

        holder.unlockCanvasAndPost(canvas)
    }

    private fun aquireAndLockCanvas() : Canvas? {
        if(holder?.surface?.isValid == false) {
            return null
        }
        return holder.lockCanvas()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val screenWidth = resources.displayMetrics.widthPixels
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                if (event.pointerCount < 2) {
                    player.boost(false)
                }
                if (event.x > screenWidth/2) {
                    player.shoot()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (event.x < screenWidth/2) {
                    player.boost(true)
                }
                if(isGameOver) {
                    restart()
                }
            }
        }
        return true
    }

    private fun restart() {
        for(entity in entities) {
            entity.respawn()
        }
        player.respawn()
        distanceTraveled = 0
        maxDistanceTraveled = prefs.getInt(LONGEST_DIST, 0)
        isGameOver = false
        jukebox.play(SFX.game_start, 0)
    }

    private fun update() {
        distanceTraveled += playerSpeed.toInt()
        player.update()
        for(entity in entities) {
            entity.update()
        }
        checkCollision()
        checkGameOver()
    }

    private fun checkGameOver() {
        if(player.health < 1) {
            isGameOver = true
            if(maxDistanceTraveled < distanceTraveled) {
                maxDistanceTraveled = distanceTraveled
                editor.putInt(LONGEST_DIST, distanceTraveled)
                editor.apply()
            }
        }

    }

    private fun checkCollision() {

        for (i in STAR_COUNT until STAR_COUNT + ENEMY_COUNT) {
            val enemy = entities[i]
            if(isColliding(enemy, player)) {
                enemy.onCollision(player)
                player.onCollision(enemy)
            }
            for(laser in player.laserShots) {
                if(isColliding(laser, enemy)) {
                    laser.onCollision(enemy)
                }
            }
        }
        for (i in STAR_COUNT+ENEMY_COUNT until STAR_COUNT+ENEMY_COUNT+POWER_UP_COUNT){
            val powerUp = entities[i]
            if(isColliding(powerUp, player)) {
                powerUp.onCollision(player)
            }
        }


    }

    fun pause() {
        isRunning = false
        try{
            gameThread.join()
        }
        catch(e: Exception){/*swallow exception, we're exiting anyway*/}
    }

    fun resume() {
        isRunning = true
        gameThread = Thread(this)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        gameThread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

}