package com.ritastudio.junglechess.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.*

@Composable
fun JungleBoardView(
    engine: JungleGameEngine,
    lang: JungleLanguageManager = JungleLanguageManager.shared,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        val availableWidth = maxWidth
        val availableHeight = maxHeight
        val targetRatio = 7f / 9.2f

        val boardWidth: androidx.compose.ui.unit.Dp
        val boardHeight: androidx.compose.ui.unit.Dp

        if (availableWidth / availableHeight > targetRatio) {
            boardHeight = availableHeight
            boardWidth = availableHeight * targetRatio
        } else {
            boardWidth = availableWidth
            boardHeight = availableWidth / targetRatio
        }

        Card(
            modifier = Modifier.size(width = boardWidth, height = boardHeight),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = BoardWoodBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
            ) {
                for (r in 0..8) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (c in 0..6) {
                            val pos = Position(r, c)
                            val piece = engine.board[pos]
                            val isSelected = engine.selectedPos == pos
                            val isValidMove = engine.validMoves.contains(pos)
                            val isDangerous = engine.dangerousMoves.contains(pos)
                            val isThreatened = engine.threatenedPieces.contains(pos)

                            val isPlayerTurn = !engine.isVsAI || engine.currentTurn == PlayerColor.BLUE
                            val advice = engine.optimalAdvice
                            val isRecommendedSource = isPlayerTurn && (advice?.from == pos)
                            val isRecommendedTarget = isPlayerTurn && (advice?.to == pos)

                            val lastOpMove = engine.lastOpponentMove
                            val isOpponentTarget = (lastOpMove?.to == pos) && (piece != null && piece.owner != engine.currentTurn)
                            val isOpponentSource = (lastOpMove?.from == pos) && (lastOpMove.movedPiece.owner != engine.currentTurn)

                            val actionBadge = if (engine.selectedPos != null) {
                                engine.getActionBadge(pos, engine.selectedPos!!)
                            } else if (isRecommendedTarget) {
                                advice?.badgeText ?: lang.recommendedTargetBadge
                            } else null

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TileCellView(
                                    engine = engine,
                                    pos = pos,
                                    piece = piece,
                                    isSelected = isSelected,
                                    isValidMove = isValidMove,
                                    isDangerous = isDangerous,
                                    isThreatened = isThreatened,
                                    isRecommendedSource = isRecommendedSource,
                                    isRecommendedTarget = isRecommendedTarget,
                                    isOpponentTarget = isOpponentTarget,
                                    isOpponentSource = isOpponentSource,
                                    actionBadge = actionBadge,
                                    lang = lang,
                                    onClick = { engine.handleTileTap(pos) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TileCellView(
    engine: JungleGameEngine,
    pos: Position,
    piece: Piece?,
    isSelected: Boolean,
    isValidMove: Boolean,
    isDangerous: Boolean,
    isThreatened: Boolean,
    isRecommendedSource: Boolean,
    isRecommendedTarget: Boolean,
    isOpponentTarget: Boolean = false,
    isOpponentSource: Boolean = false,
    actionBadge: String?,
    lang: JungleLanguageManager,
    onClick: () -> Unit
) {
    val tileType = engine.getTileType(pos)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.03f,
        targetValue = 1.11f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val opponentGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "opponentGlowAlpha"
    )
    val opponentGlowScale by infiniteTransition.animateFloat(
        initialValue = 1.02f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "opponentGlowScale"
    )

    val tileBgColor = when (tileType) {
        is TileType.Land -> LandColor
        is TileType.River -> RiverColor
        is TileType.Trap -> if (tileType.owner == PlayerColor.RED) TrapColorRed else TrapColorBlue
        is TileType.Den -> if (tileType.owner == PlayerColor.RED) DenColorRed else DenColorBlue
    }

    val cellBorderColor = when {
        isSelected -> Color(0xFFFF9800)
        isRecommendedTarget -> Color(0xFF00C853).copy(alpha = pulseAlpha)
        isOpponentTarget -> Color(0xFFFF1744).copy(alpha = opponentGlowAlpha)
        isOpponentSource -> Color(0x99FF5252)
        isValidMove -> Color(0xFF4CAF50).copy(alpha = 0.8f)
        else -> Color(0x33795548)
    }

    val cellBorderWidth = when {
        isSelected -> 2.5.dp
        isRecommendedTarget -> 2.5.dp
        isOpponentTarget -> 2.dp
        isOpponentSource -> 1.5.dp
        isValidMove -> 1.5.dp
        else -> 0.5.dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (tileType is TileType.River) {
                    Brush.verticalGradient(listOf(RiverColor, RiverDeepColor))
                } else if (isRecommendedTarget && piece == null) {
                    Brush.verticalGradient(listOf(Color(0xFFE8F8F0), Color(0xFFD0F0E0)))
                } else if (isOpponentSource && piece == null) {
                    Brush.verticalGradient(listOf(Color(0xFFFFF0F2), Color(0xFFFFEBEE)))
                } else {
                    Brush.verticalGradient(listOf(tileBgColor, tileBgColor))
                }
            )
            .border(
                width = cellBorderWidth,
                color = cellBorderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Tile label for River, Trap, Den
        when (tileType) {
            is TileType.River -> {
                if (piece == null && !isRecommendedTarget) {
                    Text(
                        text = "〰️",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
            is TileType.Trap -> {
                if (piece == null && !isRecommendedTarget) {
                    Text(
                        text = if (tileType.owner == PlayerColor.RED) lang.redTrapLabel else lang.blueTrapLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (tileType.owner == PlayerColor.RED) RedPlayerColor.copy(alpha = 0.6f) else BluePlayerColor.copy(alpha = 0.6f)
                    )
                }
            }
            is TileType.Den -> {
                if (piece == null && !isRecommendedTarget) {
                    Text(
                        text = if (tileType.owner == PlayerColor.RED) "🏰\n${lang.redDenLabel}" else "🏰\n${lang.blueDenLabel}",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 9.5.sp,
                        color = if (tileType.owner == PlayerColor.RED) RedPlayerColor else BluePlayerColor
                    )
                }
            }
            else -> {}
        }

        // Piece Token
        if (piece != null) {
            val isRed = piece.owner == PlayerColor.RED
            val borderColor = if (isRed) RedPlayerColor else BluePlayerColor
            val tokenBg = when {
                isRecommendedSource -> Color(0xFFE8F8F0)
                isOpponentTarget -> if (isRed) Color(0xFFFFEBEE) else Color(0xFFE8EAF6)
                isRed -> Color(0xFFFFEBEE)
                else -> Color(0xFFE3F2FD)
            }

            val tokenBorderColor = when {
                isOpponentTarget -> Color(0xFFFF1744)
                isRecommendedSource -> Color(0xFF00C853)
                isSelected -> Color(0xFFFF9800)
                else -> borderColor
            }

            val tokenBorderWidth = when {
                isOpponentTarget -> 2.6.dp
                isRecommendedSource -> 2.8.dp
                isSelected -> 2.2.dp
                else -> 1.2.dp
            }

            val tokenElevation = when {
                isOpponentTarget -> 6.dp
                isRecommendedSource -> 5.dp
                isSelected -> 3.dp
                else -> 1.dp
            }

            val tokenScale = when {
                isOpponentTarget -> opponentGlowScale
                isRecommendedSource -> pulseScale
                isSelected -> 1.10f
                else -> 1.0f
            }

            if (isOpponentTarget) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.98f)
                        .scale(opponentGlowScale)
                        .clip(CircleShape)
                        .background(Color(0x22FF1744))
                        .border(
                            width = 2.dp,
                            color = Color(0xFFFF5252).copy(alpha = opponentGlowAlpha),
                            shape = CircleShape
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(0.92f)
                    .scale(tokenScale)
                    .shadow(elevation = tokenElevation, shape = CircleShape)
                    .clip(CircleShape)
                    .background(tokenBg)
                    .border(
                        width = tokenBorderWidth,
                        color = tokenBorderColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = piece.type.emoji,
                        fontSize = 15.sp,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = lang.animalName(piece.type),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isRecommendedSource) Color(0xFF00C853) else if (isOpponentTarget) Color(0xFFFF1744) else borderColor,
                        lineHeight = 9.sp,
                        maxLines = 1
                    )
                }

                // Recommended source badge "💡走它"
                if (isRecommendedSource) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .shadow(2.dp, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF00C853))
                            .padding(horizontal = 2.5.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = lang.recommendedSourceBadge,
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                // Opponent move badge "⚡敌步"
                if (isOpponentTarget) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .shadow(2.dp, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFF1744))
                            .padding(horizontal = 2.5.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = lang.lastOpponentMoveBadge,
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                // Threatened pulse icon
                if (isThreatened && piece.owner == engine.currentTurn && !isRecommendedSource && !isOpponentTarget) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(1.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("!", fontSize = 7.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Frozen piece indicator "❄️"
                if (pos == engine.frozenPiecePos) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0x5500E5FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "❄️",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Opponent Move Origin indicator "📍起点"
        if (isOpponentSource && piece == null && !isRecommendedTarget) {
            Box(
                modifier = Modifier
                    .shadow(1.dp, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFEBEE).copy(alpha = 0.9f))
                    .border(1.dp, Color(0xFFFFCDD2), RoundedCornerShape(4.dp))
                    .padding(horizontal = 2.5.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = lang.lastOpponentSourceBadge,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F),
                    maxLines = 1
                )
            }
        }

        // Recommended Target empty indicator "🎯走这"
        if (isRecommendedTarget && piece == null) {
            Box(
                modifier = Modifier
                    .shadow(3.dp, RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2E7D32))
                    .border(1.dp, Color(0xFF81C784), RoundedCornerShape(6.dp))
                    .padding(horizontal = 3.dp, vertical = 1.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = actionBadge ?: lang.recommendedTargetBadge,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }

        // Move destination dot or highlight
        if (isValidMove && !isRecommendedTarget) {
            if (piece == null) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xBB4CAF50))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, Color(0xFFE91E63), RoundedCornerShape(6.dp))
                )
            }
        }

        // Dangerous move dot
        if (isDangerous && isValidMove) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD32F2F))
            )
        }

        // Floating Action Badge for capture or destination
        if (actionBadge != null && (!isRecommendedTarget || piece != null)) {
            val badgeBg = if (isRecommendedTarget) Color(0xFF00C853) else Color(0xE6263238)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-7).dp)
                    .shadow(3.dp, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .background(badgeBg)
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Text(
                    text = actionBadge,
                    fontSize = 7.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}
