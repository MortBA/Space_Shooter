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
val RNG = Random(uptimeMillis())
@Volatile var isBoosting = false
@Volatile var playerSpeed = 0f

const val PREFS = "com.askan.spaceshooter"
const val LONGEST_DIST = "longest_distance"


class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private lateinit var gameThread : Thread
    private var isGameOver = false
    private var entities = ArrayList<Entity>()
    private val paint = Paint()
    private val player = Player(resources)
    private var distanceTraveled = 0
    private var maxDistanceTraveled = 0
    private val jukebox = Jukebox(context.assets)


    @Volatile
    private var isRunning = false

    init {
        holder.addCallback(this)
        holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) {
            entities.add(Star())
        }
        for(i in 0 until ENEMY_COUNT) {
            entities.add(Enemy(resources))
        }
    }

    override fun run() {
        while (isRunning) {
            update()
            render()
        }
    }

    private fun render() {

        val canvas = aquireAndLockCanvas() ?: return

        canvas.drawColor(Color.BLACK)

        //draw the game to the surface

        for(entity in entities) {
            entity.render(canvas, paint)
        }

        player.render(canvas, paint)

        renderHud(canvas, paint)

        holder.unlockCanvasAndPost(canvas)
    }

    private fun renderHud(canvas: Canvas, paint: Paint) {
        val textSize = 48f
        val textMargin = 10f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = textSize
        if(!isGameOver) {
            canvas.drawText("Health: ${player.health}", textMargin, textSize, paint)
            canvas.drawText("Traveled: $distanceTraveled", textMargin, textSize*2, paint)
        }else {
            paint.textAlign = Paint.Align.CENTER
            val centerX = STAGE_WIDTH/2f
            val centerY = STAGE_HEIGHT/2f
            canvas.drawText("GAME OVER!!!!", centerX, centerY, paint)
            canvas.drawText("(press to restart)", centerX, centerY+textSize, paint)
        }


    }

    private fun aquireAndLockCanvas() : Canvas? {
        if(holder?.surface?.isValid == false) {
            return null
        }
        return holder.lockCanvas()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                isBoosting = false
                jukebox.stop(SFX.engine_on)
            }
            MotionEvent.ACTION_DOWN -> {
                if(isGameOver) {
                    restart()
                }

                jukebox.play(SFX.engine_on, -1)
                isBoosting = true

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

        for (i in STAR_COUNT until entities.size) {
            var enemy = entities[i]
            if(isColliding(enemy, player)) {

                enemy.onCollision(player)
                player.onCollision(enemy)
                jukebox.play(SFX.crashed, 0)

                if(player.health < 1) {
                    jukebox.stop(SFX.engine_on)
                    jukebox.play(SFX.player_destroyed, 0)
                    isBoosting = false
                }
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