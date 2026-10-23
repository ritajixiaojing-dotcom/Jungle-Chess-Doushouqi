package com.ritastudio.junglechess.model

import java.util.UUID

enum class PlayerColor {
    BLUE, RED;

    val opponent: PlayerColor
        get() = if (this == BLUE) RED else BLUE
}

enum class AnimalType(val rank: Int) {
    RAT(1),       // 鼠
    CAT(2),       // 猫
    DOG(3),       // 狗
    WOLF(4),      // 狼
    LEOPARD(5),   // 豹
    TIGER(6),     // 虎
    LION(7),      // 狮
    ELEPHANT(8);  // 象

    val emoji: String
        get() = when (this) {
            RAT -> "🐭"
            CAT -> "🐱"
            DOG -> "🐶"
            WOLF -> "🐺"
            LEOPARD -> "🐆"
            TIGER -> "🐯"
            LION -> "🦁"
            ELEPHANT -> "🐘"
        }

    val pieceValue: Int
        get() = when (this) {
            RAT -> 320
            CAT -> 200
            DOG -> 280
            WOLF -> 380
            LEOPARD -> 500
            TIGER -> 720
            LION -> 850
            ELEPHANT -> 950
        }

    companion object {
        fun fromRank(rank: Int): AnimalType? = entries.find { it.rank == rank }
    }
}

data class Position(val row: Int, val col: Int)

data class Piece(
    val id: String = UUID.randomUUID().toString(),
    val type: AnimalType,
    val owner: PlayerColor
)

sealed class TileType {
    object Land : TileType()
    object River : TileType()
    data class Trap(val owner: PlayerColor) : TileType()
    data class Den(val owner: PlayerColor) : TileType()
}

data class MoveRecord(
    val from: Position,
    val to: Position,
    val movedPiece: Piece,
    val capturedPiece: Piece?,
    val blueShieldCount: Int = 1,
    val redShieldCount: Int = 1,
    val blueFrenzyCount: Int = 1,
    val redFrenzyCount: Int = 1,
    val blueFreezeCount: Int = 1,
    val redFreezeCount: Int = 1,
    val blueFrenzyTurns: Int = 0,
    val redFrenzyTurns: Int = 0,
    val frozenPos: Position? = null,
    val frozenTurnsCount: Int = 0,
    val bannerText: String? = null
)

data class OptimalMoveAdvice(
    val from: Position,
    val to: Position,
    val piece: Piece,
    val headline: String,
    val detailReason: String,
    val forecast3Steps: String,
    val badgeText: String,
    val isEmergency: Boolean = false,
    val isWin: Boolean = false,
    val isTrade: Boolean = false
)

enum class AIDifficulty {
    EASY, MEDIUM, HARD
}

enum class StrategicDoctrine {
    LION_BLITZ,
    TIGER_LEAP,
    RAT_SUBMERGE,
    ELEPHANT_PUSH,
    PACK_HUNT
}
