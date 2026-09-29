package com.ritastudio.junglechess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ritastudio.junglechess.sound.JungleSoundManager
import com.ritastudio.junglechess.ui.JungleChessTheme
import com.ritastudio.junglechess.ui.JungleScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JungleSoundManager.init(this)
        enableEdgeToEdge()
        setContent {
            JungleChessTheme {
                JungleScreen()
            }
        }
    }
}
