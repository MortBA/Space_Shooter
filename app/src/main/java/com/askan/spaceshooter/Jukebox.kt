package com.askan.spaceshooter

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.IOException
object SFX{
    var crashed = 0
    var player_destroyed = 0
    var engine_on = 0
    var game_start = 0
}
///todo: please make it so effects are placed in a queue to be played end of frame
const val MAX_STREAMS = 3
class Jukebox(assetManager: AssetManager) {
    private val TAG = "Jukebox"
    private val assetManager = assetManager
    private val soundPool: SoundPool
    private val streamIds = mutableMapOf<Int, Int>() // Map to store stream IDs


    init {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(MAX_STREAMS)
            .build()
        SFX.crashed = loadSound("hit.wav")
        SFX.player_destroyed = loadSound("crashed.wav")
        SFX.engine_on = loadSound("engineOn.wav")
        SFX.game_start = loadSound("startGame.wav")
    }

    private fun loadSound(fileName: String): Int{
        try {
            val descriptor: AssetFileDescriptor = assetManager.openFd(fileName)
            return soundPool.load(descriptor, 1)
        }catch(e: IOException){
            Log.d(TAG, "Unable to load $fileName! Check the filename, and make sure it's in the assets-folder.")
        }
        return 0
    }

    fun play(soundID: Int, loop: Int) {
        val leftVolume = 1f
        val rightVolume = 1f
        val priority = 0
        val playbackRate = 1.0f
        if (soundID > 0) {
            val streamId = soundPool.play(soundID, leftVolume, rightVolume, priority, loop, playbackRate)
            streamIds[soundID] = streamId
        }
    }

    fun stop(soundID: Int) {
        val streamId = streamIds[soundID]
        if (streamId != null) {
            soundPool.stop(streamId)
            streamIds.remove(soundID)
        }
    }

    fun destroy() {
        soundPool.release()
        //the soundpool can no longer be used! you have to create a new soundpool.
    }
}