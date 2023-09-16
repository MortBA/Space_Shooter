package com.askan.spaceshooter

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

open class BitmapEntity(context: Context, jukebox: Jukebox) : Entity() {
    lateinit var bitmap : Bitmap
    var context: Context
    var jukebox: Jukebox

    init {
        this.context = context
        this.jukebox = jukebox
    }
    fun setSprite(bmp: Bitmap) {
        bitmap = bmp
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
    }

    override fun render(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(bitmap, x, y, paint)
        super.render(canvas, paint)
    }

    fun loadBitmap(res: Resources, id: Int, height: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(res, id)
        return scaleToTargetHeight(bitmap, height)
    }
}

private val _matrix = Matrix()
fun flip(src: Bitmap, horizontally: Boolean): Bitmap {
    _matrix.reset()
    val cx = src.width / 2
    val cy = src.height / 2
    if (horizontally) {
        _matrix.postScale(1f, -1f, cx.toFloat(), cy.toFloat())
    } else {
        _matrix.postScale(-1f, 1f, cx.toFloat(), cy.toFloat())
    }
    return Bitmap.createBitmap(src, 0, 0, src.width, src.height, _matrix, true)
}
fun flipVertically(src: Bitmap) = flip(src, false)
fun flipHorizontally(src: Bitmap) = flip(src, true)


fun scaleToTargetHeight(src: Bitmap, targetHeight: Int): Bitmap {
    val ratio = targetHeight / src.height.toFloat()
    val newH = (src.height * ratio).toInt()
    val newW = (src.width * ratio).toInt()
    return Bitmap.createScaledBitmap(src, newW, newH, true)
}