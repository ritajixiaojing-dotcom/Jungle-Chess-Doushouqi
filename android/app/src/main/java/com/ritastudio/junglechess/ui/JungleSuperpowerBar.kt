package com.ritastudio.junglechess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.PlayerColor

@Composable
fun JungleSuperpowerBar(
    engine: JungleGameEngine,
    onShowRules: () -> Unit,
    lang: JungleLanguageManager = JungleLanguageManager.shared,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Active Skill Banner (Fixed Height 20dp to prevent layout shift)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            val bannerText = engine.skillBannerText
            if (bannerText != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bannerText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Row 1: The Three Superpowers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Shield Skill
            Button(
                onClick = { engine.activateShieldRecallSkill(PlayerColor.BLUE) },
                enabled = engine.blueShieldSkillCount > 0 && engine.currentTurn == PlayerColor.BLUE,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
            ) {
                Text(
                    text = lang.shieldSkillText(engine.blueShieldSkillCount),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (engine.blueShieldSkillCount > 0) Color.White else Color.Gray,
                    maxLines = 1
                )
            }

            // Frenzy Skill
            Button(
                onClick = { engine.activateFrenzySkill(PlayerColor.BLUE) },
                enabled = engine.blueFrenzySkillCount > 0 && engine.currentTurn == PlayerColor.BLUE,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE65100),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
            ) {
                Text(
                    text = lang.frenzySkillText(engine.blueFrenzySkillCount),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (engine.blueFrenzySkillCount > 0) Color.White else Color.Gray,
                    maxLines = 1
                )
            }

            // Freeze Skill
            Button(
                onClick = { engine.activateFreezeSkill(PlayerColor.BLUE) },
                enabled = engine.blueFreezeSkillCount > 0 && engine.currentTurn == PlayerColor.BLUE,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00838F),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
            ) {
                Text(
                    text = lang.freezeSkillText(engine.blueFreezeSkillCount),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (engine.blueFreezeSkillCount > 0) Color.White else Color.Gray,
                    maxLines = 1
                )
            }
        }

        // Row 2: Undo, Restart, Rules
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Undo
            OutlinedButton(
                onClick = { engine.undo() },
                enabled = engine.history.isNotEmpty() && engine.winner == null,
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(2.dp)
            ) {
                Text(lang.undoBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Restart
            OutlinedButton(
                onClick = { engine.resetGame() },
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(2.dp)
            ) {
                Text(lang.restartBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Rules
            Button(
                onClick = onShowRules,
                modifier = Modifier.height(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Rules", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
