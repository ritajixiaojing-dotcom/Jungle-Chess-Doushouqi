package com.ritastudio.junglechess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguage
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.AIDifficulty
import com.ritastudio.junglechess.model.PlayerColor

@Composable
fun JungleHeaderBar(
    engine: JungleGameEngine,
    lang: JungleLanguageManager = JungleLanguageManager.shared,
    modifier: Modifier = Modifier
) {
    var showLanguageMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Top Row: Title, Language, Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lang.appTitle,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2E7D32),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language Dropdown
                Box {
                    Button(
                        onClick = { showLanguageMenu = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = lang.currentLanguage.flag, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showLanguageMenu,
                        onDismissRequest = { showLanguageMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(lang.systemDefaultText)
                                    if (lang.isFollowingSystem) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
                                    }
                                }
                            },
                            onClick = {
                                lang.setFollowSystem()
                                engine.computePlayerOptimalMove()
                                showLanguageMenu = false
                            }
                        )
                        HorizontalDivider()
                        JungleLanguage.entries.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(item.displayName)
                                        if (!lang.isFollowingSystem && lang.currentLanguage == item) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
                                        }
                                    }
                                },
                                onClick = {
                                    lang.setLanguage(item)
                                    engine.computePlayerOptimalMove()
                                    showLanguageMenu = false
                                }
                            )
                        }
                    }
                }

                // VS AI Mode Button
                Button(
                    onClick = {
                        engine.isVsAI = !engine.isVsAI
                        engine.resetGame()
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = if (engine.isVsAI) Icons.Default.Person else Icons.Default.People,
                        contentDescription = "Mode",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = lang.modeName(engine.isVsAI),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Sub-row: Advantage & Doctrine
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val playerLabel = if (engine.currentTurn == PlayerColor.BLUE) {
                "🔵 ${lang.playerName(PlayerColor.BLUE)}"
            } else {
                "🔴 ${lang.playerName(PlayerColor.RED)}"
            }
            val doctrineText = lang.doctrineName(engine.currentDoctrine)

            Text(
                text = "$playerLabel: ${engine.winAdvantagePercent}% ($doctrineText)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BluePlayerColor
            )

            if (engine.isThinking) {
                Text(
                    text = "🤖 ${lang.deducingBestMove}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RedPlayerColor
                )
            }
        }

        // Difficulty Selector Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AIDifficulty.entries.forEach { diff ->
                val isSelected = engine.aiDifficulty == diff
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF2E7D32) else Color.White)
                        .clickable {
                            engine.aiDifficulty = diff
                            engine.resetGame()
                        }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lang.difficultyName(diff),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.DarkGray
                    )
                }
            }
        }
    }
}
