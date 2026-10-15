package com.ritastudio.junglechess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguageManager

@Composable
fun JungleScreen(
    engine: JungleGameEngine = remember { JungleGameEngine() },
    lang: JungleLanguageManager = JungleLanguageManager.shared
) {
    var showRules by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val screenW = maxWidth
            val screenH = maxHeight

            // Strictly compute board size once from device screen bounds, maximizing width and height to fill screen
            val maxBoardW = (screenW - 12.dp)
            val maxBoardH = (screenH - 290.dp).coerceAtLeast(300.dp)
            val cellSize = minOf(maxBoardW / 7f, maxBoardH / 9.2f)
            val boardW = cellSize * 7f
            val boardH = cellSize * 9.2f

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Title, Language, Advantage, Difficulty (fixed rows)
                JungleHeaderBar(engine = engine, lang = lang)

                // Tactical Advice & Selected Piece Guidance (Fixed Height Banner 90dp, generous display)
                JungleTacticalBanner(
                    engine = engine,
                    lang = lang,
                    modifier = Modifier.height(90.dp)
                )

                Spacer(modifier = Modifier.weight(1f, fill = true))

                // Board (9x7 Grid) - Constant immutable size
                Box(
                    modifier = Modifier.size(width = boardW, height = boardH),
                    contentAlignment = Alignment.Center
                ) {
                    JungleBoardView(engine = engine, lang = lang)
                }

                Spacer(modifier = Modifier.weight(1f, fill = true))

                // Bottom Controls & Superpower Abilities
                JungleSuperpowerBar(
                    engine = engine,
                    onShowRules = { showRules = true },
                    lang = lang
                )
            }
        }

        // Rule Guide Modal Dialog
        if (showRules) {
            JungleRuleGuideDialog(
                onDismiss = { showRules = false },
                lang = lang
            )
        }

        // Victory / Defeat Dialog
        val winner = engine.winner
        if (winner != null) {
            JungleVictoryDialog(
                winner = winner,
                isVsAI = engine.isVsAI,
                onPlayAgain = { engine.resetGame() },
                lang = lang
            )
        }
    }
}
