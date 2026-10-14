package com.ritastudio.junglechess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.PlayerColor

@Composable
fun JungleTacticalBanner(
    engine: JungleGameEngine,
    lang: JungleLanguageManager = JungleLanguageManager.shared,
    modifier: Modifier = Modifier
) {
    val advice = engine.optimalAdvice

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 10.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (advice != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row: Badge, Headline, Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        val badgeBg = when {
                            advice.isWin -> Color(0xFF4CAF50)
                            advice.isEmergency -> Color(0xFFE53935)
                            advice.isTrade -> Color(0xFF9C27B0)
                            else -> Color(0xFF2E7D32)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = advice.badgeText,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = advice.headline,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            maxLines = 1
                        )
                    }

                    // Smart Move Button
                    Button(
                        onClick = { engine.executeBestAdvice() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(
                            text = lang.moveForMeBtn.replace("\n", " "),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Detail Reason
                Text(
                    text = advice.detailReason,
                    fontSize = 10.5.sp,
                    color = Color(0xFF444444),
                    lineHeight = 13.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // 3-Step Forecast Row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (advice.forecast3Steps.isNotEmpty()) "${lang.depth3DeductionTitle} ${advice.forecast3Steps}" else "${lang.depth3DeductionTitle} 洞悉先机 步步为营",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20),
                        lineHeight = 11.sp,
                        maxLines = 1
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🤖 ${lang.deducingBestMove}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AI 实时演算",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Text(
                    text = "落子后将在此实时展示 3 步大师级战术推演",
                    fontSize = 10.5.sp,
                    color = Color.DarkGray,
                    lineHeight = 12.sp,
                    maxLines = 1
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "💡 运筹帷幄 · 智勇双全 · 决胜千里",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20),
                        lineHeight = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun JungleRuleCheatBar(
    engine: JungleGameEngine,
    lang: JungleLanguageManager = JungleLanguageManager.shared,
    modifier: Modifier = Modifier
) {
    val piece = engine.inspectPiece

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(horizontal = 10.dp, vertical = 1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (piece != null) {
                val ownerColor = if (piece.owner == PlayerColor.BLUE) BluePlayerColor else RedPlayerColor
                Text(
                    text = "${piece.type.emoji}【${lang.animalName(piece.type)}】:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = ownerColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = lang.animalEatRule(piece.type),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 2,
                    lineHeight = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = "💡 ${lang.rulePromptHint}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
