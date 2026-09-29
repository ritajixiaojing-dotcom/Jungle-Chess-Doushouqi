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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Title, Language, Advantage, Difficulty
            JungleHeaderBar(engine = engine, lang = lang)

            // Tactical Advice Banner
            JungleTacticalBanner(engine = engine, lang = lang)

            // Board (9x7 Grid)
            Box(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                JungleBoardView(engine = engine, lang = lang)
            }

            // Bottom Controls & Superpower Abilities
            JungleSuperpowerBar(
                engine = engine,
                onShowRules = { showRules = true },
                lang = lang
            )
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
