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
    val selected = engine.selectedPos
    val piece = if (selected != null) engine.board[selected] else null
    val advice = engine.optimalAdvice

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                advice?.isEmergency == true -> Color(0xFFFFEBEE)
                advice?.isTrade == true -> Color(0xFFF3E5F5)
                else -> Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (piece != null) {
                // 1. 选中棋子模式：专属吃子规则与说明，固定3行高度（22, 18, 36）
                val ownerColor = if (piece.owner == PlayerColor.BLUE) BluePlayerColor else RedPlayerColor
                val ownerLabel = if (piece.owner == PlayerColor.BLUE) lang.playerName(PlayerColor.BLUE) else lang.playerName(PlayerColor.RED)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = piece.type.emoji,
                            fontSize = 15.sp
                        )
                        Text(
                            text = lang.formatAnimalTitle(piece.type, isFull = true),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = ownerColor,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ownerColor)
                            .padding(horizontal = 7.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = ownerLabel,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lang.animalEatRule(piece.type),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = lang.selectedPieceInstruction,
                        fontSize = 9.5.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else if (advice != null) {
                // 2. 战术指引模式：3步推演与走法建议，固定3行高度（22, 18, 36），充裕展示不吞字
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
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
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 1.5.dp)
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Smart Move Button
                    Button(
                        onClick = { engine.executeBestAdvice() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(22.dp)
                    ) {
                        Text(
                            text = lang.moveForMeBtn.replace("\n", " "),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = advice.detailReason,
                        fontSize = 10.5.sp,
                        color = Color(0xFF444444),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (advice.forecast3Steps.isNotEmpty()) "${lang.depth3DeductionTitle} ${advice.forecast3Steps}" else "${lang.depth3DeductionTitle} ${lang.depth3DefaultMotto}",
                        fontSize = 10.sp,
                        lineHeight = 14.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                // 3. AI思考/等待中状态：占位恒定高度（22, 18, 36）
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (engine.isThinking) "🤖 ${lang.aiThinkingText}" else "🤖 ${lang.deducingBestMove}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 6.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = lang.aiRealtimeBadge,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lang.waitingMoveHint,
                        fontSize = 10.5.sp,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = lang.tacticalDeepSearchHint,
                        fontSize = 9.5.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
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
                    text = "${piece.type.emoji}${lang.formatAnimalTitle(piece.type)}:",
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
