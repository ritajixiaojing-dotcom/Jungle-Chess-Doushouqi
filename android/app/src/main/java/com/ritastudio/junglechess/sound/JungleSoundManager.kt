package com.ritastudio.junglechess.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import com.ritastudio.junglechess.R

class JungleSoundManager private constructor(private val context: Context) {
    private val soundPool: SoundPool
    private var moveSoundId: Int = 0
    private var captureSoundId: Int = 0
    private var selectSoundId: Int = 0
    private var jumpSoundId: Int = 0
    private var undoSoundId: Int = 0
    private var winSoundId: Int = 0

    private val vibrator: Vibrator? by lazy {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            moveSoundId = soundPool.load(context, R.raw.sound_move, 1)
            captureSoundId = soundPool.load(context, R.raw.sound_capture, 1)
            selectSoundId = soundPool.load(context, R.raw.sound_select, 1)
            jumpSoundId = soundPool.load(context, R.raw.sound_jump, 1)
            undoSoundId = soundPool.load(context, R.raw.sound_undo, 1)
            winSoundId = soundPool.load(context, R.raw.sound_win, 1)
        } catch (e: Exception) {
            Log.e("JungleSoundManager", "Failed to load audio files", e)
        }
    }

    fun playMove() {
        if (moveSoundId != 0) soundPool.play(moveSoundId, 0.9f, 0.9f, 1, 0, 1.0f)
        vibrate(30)
    }

    fun playCapture() {
        if (captureSoundId != 0) soundPool.play(captureSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        vibrate(60)
    }

    fun playSelect() {
        if (selectSoundId != 0) soundPool.play(selectSoundId, 0.6f, 0.6f, 1, 0, 1.0f)
        vibrate(15)
    }

    fun playJump() {
        if (jumpSoundId != 0) soundPool.play(jumpSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        vibrate(50)
    }

    fun playUndo() {
        if (undoSoundId != 0) soundPool.play(undoSoundId, 0.8f, 0.8f, 1, 0, 1.0f)
        vibrate(25)
    }

    fun playWin() {
        if (winSoundId != 0) soundPool.play(winSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        vibrate(100)
    }

    private fun vibrate(milliseconds: Long) {
        try {
            vibrator?.let { v ->
                if (v.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        v.vibrate(milliseconds)
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }

    companion object {
        @Volatile
        private var instance: JungleSoundManager? = null

        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = JungleSoundManager(context.applicationContext)
                    }
                }
            }
        }

        val shared: JungleSoundManager
            get() = instance ?: throw IllegalStateException("JungleSoundManager must be initialized via init(context)")

        fun getOrNull(): JungleSoundManager? = instance
    }
}
