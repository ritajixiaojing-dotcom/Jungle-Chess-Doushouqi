package com.ritastudio.junglechess.engine

import androidx.compose.runtime.*
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.*
import com.ritastudio.junglechess.sound.JungleSoundManager
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class JungleGameEngine(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    var board by mutableStateOf<Map<Position, Piece>>(emptyMap())
    var currentTurn by mutableStateOf(PlayerColor.BLUE)
    var selectedPos by mutableStateOf<Position?>(null)
    var validMoves by mutableStateOf<List<Position>>(emptyList())
    var dangerousMoves by mutableStateOf<Set<Position>>(emptySet())
    var threatenedPieces by mutableStateOf<Set<Position>>(emptySet())
    var winner by mutableStateOf<PlayerColor?>(null)

    var optimalAdvice by mutableStateOf<OptimalMoveAdvice?>(null)
    var winAdvantagePercent by mutableStateOf(50)
    var currentDoctrine by mutableStateOf(StrategicDoctrine.LION_BLITZ)

    var blueShieldSkillCount by mutableStateOf(1)
    var redShieldSkillCount by mutableStateOf(1)
    var blueFrenzySkillCount by mutableStateOf(1)
    var redFrenzySkillCount by mutableStateOf(1)
    var blueFreezeSkillCount by mutableStateOf(1)
    var redFreezeSkillCount by mutableStateOf(1)
    var blueFrenzyRemainingTurns by mutableStateOf(0)
    var redFrenzyRemainingTurns by mutableStateOf(0)
    var frozenPiecePos by mutableStateOf<Position?>(null)
    var frozenTurns by mutableStateOf(0)
    var skillBannerText by mutableStateOf<String?>(null)

    var inspectPiece by mutableStateOf<Piece?>(null)
    var isVsAI by mutableStateOf(true)
    var aiDifficulty by mutableStateOf(AIDifficulty.HARD)
    var isThinking by mutableStateOf(false)
    val history = mutableStateListOf<MoveRecord>()
    var lastOpponentMove by mutableStateOf<MoveRecord?>(null)

    init {
        resetGame()
    }

    fun getTileType(pos: Position): TileType {
        val r = pos.row
        val c = pos.col

        if (r == 0 && c == 3) return TileType.Den(PlayerColor.RED)
        if (r == 8 && c == 3) return TileType.Den(PlayerColor.BLUE)

        if ((r == 0 && (c == 2 || c == 4)) || (r == 1 && c == 3)) {
            return TileType.Trap(PlayerColor.RED)
        }
        if ((r == 8 && (c == 2 || c == 4)) || (r == 7 && c == 3)) {
            return TileType.Trap(PlayerColor.BLUE)
        }

        if (r in 3..5 && (c == 1 || c == 2 || c == 4 || c == 5)) {
            return TileType.River
        }

        return TileType.Land
    }

    fun resetGame() {
        val newBoard = mutableMapOf<Position, Piece>()
        history.clear()
        lastOpponentMove = null
        selectedPos = null
        validMoves = emptyList()
        dangerousMoves = emptySet()
        threatenedPieces = emptySet()
        winner = null
        currentTurn = PlayerColor.BLUE

        blueShieldSkillCount = 1
        redShieldSkillCount = 1
        blueFrenzySkillCount = 1
        redFrenzySkillCount = 1
        blueFreezeSkillCount = 1
        redFreezeSkillCount = 1
        blueFrenzyRemainingTurns = 0
        redFrenzyRemainingTurns = 0
        frozenPiecePos = null
        frozenTurns = 0
        skillBannerText = null

        currentDoctrine = StrategicDoctrine.entries.random()

        // 红方初始 (Red)
        newBoard[Position(0, 0)] = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        newBoard[Position(0, 6)] = Piece(type = AnimalType.TIGER, owner = PlayerColor.RED)
        newBoard[Position(1, 1)] = Piece(type = AnimalType.DOG, owner = PlayerColor.RED)
        newBoard[Position(1, 5)] = Piece(type = AnimalType.CAT, owner = PlayerColor.RED)
        newBoard[Position(2, 0)] = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
        newBoard[Position(2, 2)] = Piece(type = AnimalType.LEOPARD, owner = PlayerColor.RED)
        newBoard[Position(2, 4)] = Piece(type = AnimalType.WOLF, owner = PlayerColor.RED)
        newBoard[Position(2, 6)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)

        // 蓝方初始 (Blue)
        newBoard[Position(8, 6)] = Piece(type = AnimalType.LION, owner = PlayerColor.BLUE)
        newBoard[Position(8, 0)] = Piece(type = AnimalType.TIGER, owner = PlayerColor.BLUE)
        newBoard[Position(7, 5)] = Piece(type = AnimalType.DOG, owner = PlayerColor.BLUE)
        newBoard[Position(7, 1)] = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
        newBoard[Position(6, 6)] = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
        newBoard[Position(6, 4)] = Piece(type = AnimalType.LEOPARD, owner = PlayerColor.BLUE)
        newBoard[Position(6, 2)] = Piece(type = AnimalType.WOLF, owner = PlayerColor.BLUE)
        newBoard[Position(6, 0)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)

        board = newBoard
        updateRoundEvaluation()
    }

    fun activateFrenzySkill(player: PlayerColor = PlayerColor.BLUE) {
        if (player == PlayerColor.BLUE) {
            if (blueFrenzySkillCount <= 0 || currentTurn != PlayerColor.BLUE) return
            blueFrenzySkillCount -= 1
            blueFrenzyRemainingTurns = 2
            skillBannerText = JungleLanguageManager.shared.frenzyBannerText
            JungleSoundManager.getOrNull()?.playJump()
            updateRoundEvaluation()
        } else {
            if (redFrenzySkillCount <= 0 || currentTurn != PlayerColor.RED) return
            redFrenzySkillCount -= 1
            redFrenzyRemainingTurns = 2
            skillBannerText = JungleLanguageManager.shared.aiFrenzyBannerText
            JungleSoundManager.getOrNull()?.playJump()
            updateRoundEvaluation()
        }
    }

    fun activateShieldRecallSkill(player: PlayerColor = PlayerColor.BLUE) {
        if (player == PlayerColor.BLUE) {
            if (blueShieldSkillCount <= 0 || currentTurn != PlayerColor.BLUE) return
            blueShieldSkillCount -= 1

            var bestPiecePos: Position? = null
            var bestPiece: Piece? = null
            val targetGate = Position(7, 3)

            for ((pos, p) in board) {
                if (p.owner == PlayerColor.BLUE && pos != targetGate) {
                    if (bestPiece == null || p.type.rank > bestPiece.type.rank) {
                        bestPiecePos = pos
                        bestPiece = p
                    }
                }
            }

            if (bestPiecePos != null && bestPiece != null) {
                val newBoard = board.toMutableMap()
                newBoard.remove(bestPiecePos)
                newBoard[targetGate] = bestPiece
                board = newBoard
                val name = JungleLanguageManager.shared.animalName(bestPiece.type)
                skillBannerText = JungleLanguageManager.shared.shieldBannerText(pieceName = name)
                JungleSoundManager.getOrNull()?.playWin()
                updateRoundEvaluation()
            }
        } else {
            if (redShieldSkillCount <= 0 || currentTurn != PlayerColor.RED) return
            redShieldSkillCount -= 1

            var bestPiecePos: Position? = null
            var bestPiece: Piece? = null
            val targetGate = Position(1, 3)

            for ((pos, p) in board) {
                if (p.owner == PlayerColor.RED && pos != targetGate) {
                    if (bestPiece == null || p.type.rank > bestPiece.type.rank) {
                        bestPiecePos = pos
                        bestPiece = p
                    }
                }
            }

            if (bestPiecePos != null && bestPiece != null) {
                val newBoard = board.toMutableMap()
                newBoard.remove(bestPiecePos)
                newBoard[targetGate] = bestPiece
                board = newBoard
                val name = JungleLanguageManager.shared.animalName(bestPiece.type)
                skillBannerText = JungleLanguageManager.shared.aiShieldBannerText(pieceName = name)
                JungleSoundManager.getOrNull()?.playWin()
                updateRoundEvaluation()
            }
        }
    }

    fun activateFreezeSkill(player: PlayerColor = PlayerColor.BLUE) {
        val opponent = player.opponent
        if (player == PlayerColor.BLUE) {
            if (blueFreezeSkillCount <= 0 || currentTurn != PlayerColor.BLUE) return
            var targetPos: Position? = null
            var highestScore = -99999

            for ((pos, p) in board) {
                if (p.owner == opponent) {
                    val distToDen = abs(pos.row - 8) + abs(pos.col - 3)
                    val score = p.type.pieceValue - (distToDen * 150)
                    if (score > highestScore) {
                        highestScore = score
                        targetPos = pos
                    }
                }
            }

            if (targetPos != null) {
                val targetPiece = board[targetPos] ?: return
                blueFreezeSkillCount -= 1
                frozenPiecePos = targetPos
                frozenTurns = 1
                val name = JungleLanguageManager.shared.animalName(targetPiece.type)
                skillBannerText = JungleLanguageManager.shared.freezeBannerText(pieceName = name)
                JungleSoundManager.getOrNull()?.playWin()
                updateRoundEvaluation()
            }
        } else {
            if (redFreezeSkillCount <= 0 || currentTurn != PlayerColor.RED) return
            var targetPos: Position? = null
            var highestScore = -99999

            for ((pos, p) in board) {
                if (p.owner == opponent) {
                    val distToDen = abs(pos.row - 0) + abs(pos.col - 3)
                    val score = p.type.pieceValue - (distToDen * 150)
                    if (score > highestScore) {
                        highestScore = score
                        targetPos = pos
                    }
                }
            }

            if (targetPos != null) {
                val targetPiece = board[targetPos] ?: return
                redFreezeSkillCount -= 1
                frozenPiecePos = targetPos
                frozenTurns = 1
                val name = JungleLanguageManager.shared.animalName(targetPiece.type)
                skillBannerText = JungleLanguageManager.shared.aiFreezeBannerText(pieceName = name)
                JungleSoundManager.getOrNull()?.playWin()
                updateRoundEvaluation()
            }
        }
    }

    fun handleTileTap(pos: Position) {
        if (winner != null || isThinking) return
        if (isVsAI && currentTurn == PlayerColor.RED) return

        val clickedPiece = board[pos]

        val selected = selectedPos
        if (selected != null && validMoves.contains(pos)) {
            makeMove(from = selected, to = pos)
            return
        }

        if (clickedPiece != null && clickedPiece.owner == currentTurn) {
            if (pos == frozenPiecePos) {
                JungleSoundManager.getOrNull()?.playSelect()
                return
            }
            selectedPos = pos
            inspectPiece = clickedPiece
            validMoves = calculateValidMoves(pos)
            dangerousMoves = calculateDangerousMoves(pos, validMoves)
            JungleSoundManager.getOrNull()?.playSelect()
            return
        }

        if (clickedPiece != null) {
            inspectPiece = clickedPiece
            selectedPos = null
            validMoves = emptyList()
            dangerousMoves = emptySet()
            return
        }

        selectedPos = null
        validMoves = emptyList()
        dangerousMoves = emptySet()
    }

    fun makeMove(from: Position, to: Position) {
        val movingPiece = board[from] ?: return
        val capturedPiece = board[to]

        val record = MoveRecord(from = from, to = to, movedPiece = movingPiece, capturedPiece = capturedPiece)
        history.add(record)
        lastOpponentMove = record

        val newBoard = board.toMutableMap()
        newBoard.remove(from)
        newBoard[to] = movingPiece
        board = newBoard

        selectedPos = null
        validMoves = emptyList()
        dangerousMoves = emptySet()
        threatenedPieces = emptySet()
        optimalAdvice = null

        val isJump = (movingPiece.type == AnimalType.LION || movingPiece.type == AnimalType.TIGER) &&
                (abs(from.row - to.row) > 1 || abs(from.col - to.col) > 1)

        if (isJump) {
            JungleSoundManager.getOrNull()?.playJump()
        } else if (capturedPiece != null) {
            JungleSoundManager.getOrNull()?.playCapture()
        } else {
            JungleSoundManager.getOrNull()?.playMove()
        }

        if (movingPiece.owner == PlayerColor.BLUE && blueFrenzyRemainingTurns > 0) {
            blueFrenzyRemainingTurns -= 1
        }
        if (movingPiece.owner == PlayerColor.RED && redFrenzyRemainingTurns > 0) {
            redFrenzyRemainingTurns -= 1
        }

        val fPos = frozenPiecePos
        if (fPos != null) {
            if (board[fPos] == null || to == fPos) {
                frozenPiecePos = null
                frozenTurns = 0
            } else {
                val fPiece = board[fPos]
                if (fPiece != null && fPiece.owner == movingPiece.owner) {
                    frozenTurns -= 1
                    if (frozenTurns <= 0) {
                        frozenPiecePos = null
                    }
                }
            }
        }

        if (blueFrenzyRemainingTurns == 0 && redFrenzyRemainingTurns == 0 && frozenPiecePos == null) {
            skillBannerText = null
        }

        if (checkWin(afterMoveTo = to, by = movingPiece.owner)) {
            winner = movingPiece.owner
            JungleSoundManager.getOrNull()?.playWin()
            return
        }

        currentTurn = currentTurn.opponent
        updateRoundEvaluation()

        if (isVsAI && currentTurn == PlayerColor.RED) {
            isThinking = true
            scope.launch {
                delay(350)
                triggerAIMove()
            }
        }
    }

    fun undo() {
        if (history.isEmpty() || winner != null) return
        val stepsToUndo = if (isVsAI && history.size >= 2) 2 else 1
        JungleSoundManager.getOrNull()?.playUndo()

        val newBoard = board.toMutableMap()
        for (i in 0 until stepsToUndo) {
            if (history.isEmpty()) break
            val last = history.removeAt(history.size - 1)
            newBoard[last.from] = last.movedPiece
            if (last.capturedPiece != null) {
                newBoard[last.to] = last.capturedPiece
            } else {
                newBoard.remove(last.to)
            }
            currentTurn = last.movedPiece.owner
        }
        board = newBoard

        selectedPos = null
        validMoves = emptyList()
        dangerousMoves = emptySet()
        threatenedPieces = emptySet()
        lastOpponentMove = history.lastOrNull { it.movedPiece.owner != currentTurn }
        updateRoundEvaluation()
    }

    fun executeBestAdvice() {
        if (currentTurn != PlayerColor.BLUE && isVsAI) return
        val advice = optimalAdvice
        if (advice != null) {
            makeMove(from = advice.from, to = advice.to)
        } else {
            computePlayerOptimalMove()
            val newAdv = optimalAdvice
            if (newAdv != null) {
                makeMove(from = newAdv.from, to = newAdv.to)
            }
        }
    }

    fun autoSelectRecommendedPiece() {
        if (currentTurn != PlayerColor.BLUE && isVsAI) return
        val advice = optimalAdvice
        if (advice != null) {
            selectedPos = advice.from
            inspectPiece = advice.piece
            validMoves = calculateValidMoves(advice.from)
            dangerousMoves = calculateDangerousMoves(advice.from, validMoves)
        }
    }

    fun updateRoundEvaluation() {
        if (winner != null) return

        val playerTarget = if (isVsAI) PlayerColor.BLUE else currentTurn
        scanThreatenedPieces(playerTarget)

        var bluePower = 0
        var redPower = 0
        var blueDistToDen = 99
        var redDistToDen = 99
        for ((pos, piece) in board) {
            val power = piece.type.pieceValue
            if (piece.owner == PlayerColor.BLUE) {
                bluePower += power
                val dist = abs(pos.row - 0) + abs(pos.col - 3)
                blueDistToDen = min(blueDistToDen, dist)
            } else {
                redPower += power
                val dist = abs(pos.row - 8) + abs(pos.col - 3)
                redDistToDen = min(redDistToDen, dist)
            }
        }
        val total = max(1, bluePower + redPower)
        var percent = ((bluePower.toDouble() / total.toDouble()) * 100.0).toInt()
        if (blueDistToDen <= 3) percent = max(percent, 85)
        if (blueDistToDen <= 1) percent = 98
        if (redDistToDen <= 2) percent = min(percent, 25)
        if (redDistToDen <= 1) percent = min(percent, 10)
        winAdvantagePercent = max(5, min(99, percent))

        computePlayerOptimalMove()

        val adv = optimalAdvice
        if (adv != null) {
            inspectPiece = adv.piece
        }
    }

    private fun scanThreatenedPieces(player: PlayerColor) {
        val threatened = mutableSetOf<Position>()
        val opponent = player.opponent
        for ((oppPos, oppPiece) in board) {
            if (oppPiece.owner == opponent) {
                val oppMoves = calculateValidMoves(oppPos)
                for (targetPos in oppMoves) {
                    val targetPiece = board[targetPos]
                    if (targetPiece != null && targetPiece.owner == player) {
                        threatened.add(targetPos)
                    }
                }
            }
        }
        threatenedPieces = threatened
    }

    private fun isPositionThreatened(pos: Position, piece: Piece, boardState: Map<Position, Piece>): Boolean {
        val opponent = piece.owner.opponent
        for ((oppPos, oppPiece) in boardState) {
            if (oppPiece.owner == opponent) {
                val oppMoves = calculateValidMovesOnBoard(boardState, oppPos)
                if (oppMoves.contains(pos)) {
                    return true
                }
            }
        }
        return false
    }

    // 检查重复往复循环步数 (禁长捉/破死循环 - 智能版：逃命与吃子绝不惩罚)
    private fun calculateRepetitionPenalty(from: Position, to: Position, piece: Piece, isUnderThreat: Boolean, isCapture: Boolean): Int {
        if (isUnderThreat || isCapture) return 0

        var penalty = 0
        val recentMoves = history.takeLast(10).filter { it.movedPiece.owner == piece.owner }

        var backAndForthCount = 0
        var visitedCount = 0

        for (past in recentMoves) {
            if (past.from == to && past.to == from && past.movedPiece.type == piece.type) {
                backAndForthCount += 1
            }
            if (past.to == to && past.movedPiece.type == piece.type) {
                visitedCount += 1
            }
        }

        if (backAndForthCount >= 1) {
            penalty += 2500 * backAndForthCount
        }
        if (visitedCount >= 2) {
            penalty += 1200 * (visitedCount - 1)
        }
        return penalty
    }

    // MARK: - 🧠 棋局估值函数 (宗师级位置热力图与战术矩阵)
    fun evaluateBoardScore(customBoard: Map<Position, Piece>, forPlayer: PlayerColor): Int {
        val blueDen = Position(8, 3)
        val redDen = Position(0, 3)

        if (customBoard[redDen]?.owner == PlayerColor.BLUE) {
            return if (forPlayer == PlayerColor.BLUE) 1_000_000 else -1_000_000
        }
        if (customBoard[blueDen]?.owner == PlayerColor.RED) {
            return if (forPlayer == PlayerColor.RED) 1_000_000 else -1_000_000
        }

        val myDen = if (forPlayer == PlayerColor.RED) redDen else blueDen
        val enemyDen = if (forPlayer == PlayerColor.RED) blueDen else redDen
        val myTraps = if (forPlayer == PlayerColor.RED)
            setOf(Position(0, 2), Position(0, 4), Position(1, 3))
        else
            setOf(Position(8, 2), Position(8, 4), Position(7, 3))
        val enemyTraps = if (forPlayer == PlayerColor.RED)
            setOf(Position(8, 2), Position(8, 4), Position(7, 3))
        else
            setOf(Position(0, 2), Position(0, 4), Position(1, 3))

        fun pieceBaseValue(type: AnimalType): Int {
            return when (type) {
                AnimalType.RAT -> 3800       // 下水控河、捕杀巨象、侦察突破
                AnimalType.CAT -> 2000
                AnimalType.DOG -> 3000       // 守护犬、捕鼠护象
                AnimalType.WOLF -> 4200
                AnimalType.LEOPARD -> 5800   // 中期强力战力
                AnimalType.TIGER -> 8800     // 纵横飞河伏击
                AnimalType.LION -> 10000     // 万兽之王，绝对控盘核心
                AnimalType.ELEPHANT -> 9200  // 陆地重炮，需防老鼠
            }
        }

        var score = 0
        var myElephantPos: Position? = null
        var oppRatPos: Position? = null
        var oppElephantPos: Position? = null
        var myRatPos: Position? = null

        for ((pos, piece) in customBoard) {
            if (piece.owner == forPlayer) {
                if (piece.type == AnimalType.ELEPHANT) myElephantPos = pos
                if (piece.type == AnimalType.RAT) myRatPos = pos
            } else {
                if (piece.type == AnimalType.ELEPHANT) oppElephantPos = pos
                if (piece.type == AnimalType.RAT) oppRatPos = pos
            }

            val isMine = (piece.owner == forPlayer)
            val baseVal = pieceBaseValue(piece.type)
            var posBonus = 0

            val targetDen = if (isMine) enemyDen else myDen
            val distToTarget = abs(pos.row - targetDen.row) + abs(pos.col - targetDen.col)

            // 1. 战略推进动力 (平滑梯度)
            posBonus += (12 - distToTarget) * 50

            // 2. 攻入大本营绝杀威胁
            if (distToTarget == 1) {
                posBonus += 4000
            } else if (distToTarget == 2) {
                posBonus += 1800
            } else if (distToTarget <= 3) {
                posBonus += 700
            }

            // 3. 狮虎抢占跳河发射平台 (河岸关键发射阵位)
            if (piece.type == AnimalType.LION || piece.type == AnimalType.TIGER) {
                if (pos.row == 2 || pos.row == 6) {
                    posBonus += 600
                    if (pos.col in listOf(1, 2, 4, 5)) {
                        posBonus += 700 // 正对河道发射阵地
                    }
                }
            }

            // 4. 老鼠入河战术价值 (封锁狮虎跳河 + 隐蔽机动)
            if (piece.type == AnimalType.RAT && getTileType(pos) is TileType.River) {
                posBonus += 750
            }

            // 5. 陷阱禁区与防守认知
            if (isMine) {
                if (enemyTraps.contains(pos)) {
                    posBonus -= 3500
                }
                if (myTraps.contains(pos)) {
                    posBonus += 400
                }
                score += (baseVal + posBonus)
            } else {
                if (myTraps.contains(pos)) {
                    posBonus -= 3500
                }
                if (enemyTraps.contains(pos)) {
                    posBonus += 400
                }
                score -= (baseVal + posBonus)
            }
        }

        // 6. 象鼠克制动态加权
        if (myElephantPos != null && oppRatPos != null) {
            val dist = abs(myElephantPos.row - oppRatPos.row) + abs(myElephantPos.col - oppRatPos.col)
            if (getTileType(oppRatPos) !is TileType.River) {
                if (dist == 1) score -= 5000
                else if (dist == 2) score -= 2200
            }
        }
        if (myRatPos != null && oppElephantPos != null) {
            val dist = abs(myRatPos.row - oppElephantPos.row) + abs(myRatPos.col - oppElephantPos.col)
            if (getTileType(myRatPos) !is TileType.River) {
                if (dist == 1) score += 5000
                else if (dist == 2) score += 2200
            }
        }

        return score
    }

    private fun isSquareCoveredByFriendly(boardState: Map<Position, Piece>, pos: Position, owner: PlayerColor): Boolean {
        for ((friendPos, friendPiece) in boardState) {
            if (friendPiece.owner == owner && friendPos != pos) {
                val moves = calculateValidMovesOnBoard(boardState, friendPos)
                if (moves.contains(pos)) {
                    return true
                }
            }
        }
        return false
    }

    // MARK: - 👑 宗师级 Alpha-Beta 搜索核心辅助模型与算法
    data class SimMove(
        val from: Position,
        val to: Position,
        val piece: Piece,
        val captured: Piece?,
        val isWin: Boolean = false
    )

    private fun getAllSimMovesForPlayer(boardState: Map<Position, Piece>, player: PlayerColor): List<SimMove> {
        val moves = mutableListOf<SimMove>()
        for ((pos, piece) in boardState) {
            if (piece.owner == player) {
                val dests = calculateValidMovesOnBoard(boardState, pos)
                for (dest in dests) {
                    val cap = boardState[dest]
                    val destTile = getTileType(dest)
                    val isWin = destTile is TileType.Den && destTile.owner != player
                    moves.add(SimMove(from = pos, to = dest, piece = piece, captured = cap, isWin = isWin))
                }
            }
        }
        return moves
    }

    private fun orderSimMoves(moves: List<SimMove>, boardState: Map<Position, Piece>, player: PlayerColor): List<SimMove> {
        return moves.sortedByDescending { m ->
            var s = 0
            if (m.isWin) s += 1_000_000
            if (m.captured != null) {
                s += m.captured.type.rank * 1000 - m.piece.type.rank * 50
            }
            if ((m.piece.type == AnimalType.LION || m.piece.type == AnimalType.TIGER) && abs(m.from.row - m.to.row) > 1) {
                s += 3000
            }
            s
        }
    }

    // 静态吃子延伸搜索 (Quiescence Search: 彻底消灭地平线效应与贪吃被杀)
    private fun quiescenceSearch(boardState: Map<Position, Piece>, alpha: Int, beta: Int, player: PlayerColor, maxQDepth: Int): Int {
        var currentAlpha = alpha
        val standPat = evaluateBoardScore(boardState, player)
        if (standPat >= beta) return beta
        if (standPat > currentAlpha) currentAlpha = standPat
        if (maxQDepth <= 0) return standPat

        val allMoves = getAllSimMovesForPlayer(boardState, player)
        val tacticalMoves = allMoves.filter { it.captured != null || it.isWin }
        if (tacticalMoves.isEmpty()) return standPat

        val ordered = orderSimMoves(tacticalMoves, boardState, player)
        for (m in ordered) {
            if (m.isWin) return 900_000
            val nextBoard = boardState.toMutableMap()
            nextBoard.remove(m.from)
            nextBoard[m.to] = m.piece

            val score = -quiescenceSearch(nextBoard, -beta, -currentAlpha, player.opponent, maxQDepth - 1)
            if (score >= beta) return beta
            if (score > currentAlpha) currentAlpha = score
        }
        return currentAlpha
    }

    // Alpha-Beta 剪枝搜索
    private fun alphaBetaSearch(boardState: Map<Position, Piece>, depth: Int, alpha: Int, beta: Int, player: PlayerColor): Int {
        if (depth <= 0) {
            return quiescenceSearch(boardState, alpha, beta, player, maxQDepth = 2)
        }

        var currentAlpha = alpha
        val allMoves = getAllSimMovesForPlayer(boardState, player)
        if (allMoves.isEmpty()) return -900_000

        val ordered = orderSimMoves(allMoves, boardState, player)
        for (m in ordered) {
            if (m.isWin) return 900_000 + depth
            val nextBoard = boardState.toMutableMap()
            nextBoard.remove(m.from)
            nextBoard[m.to] = m.piece

            val score = -alphaBetaSearch(nextBoard, depth - 1, -beta, -currentAlpha, player.opponent)
            if (score >= beta) return beta
            if (score > currentAlpha) currentAlpha = score
        }
        return currentAlpha
    }

    data class CandidateMove(
        val from: Position,
        val to: Position,
        val piece: Piece,
        val score: Int,
        val advice: OptimalMoveAdvice
    )

    // MARK: - 🏆 核心最优解推演中枢 (红方 AI & 蓝方教学建议 共享的宗师博弈引擎)
    fun searchOptimalMoveCandidates(playerSide: PlayerColor): List<CandidateMove> {
        val enemyDen = if (playerSide == PlayerColor.BLUE) Position(0, 3) else Position(8, 3)
        val myDen = if (playerSide == PlayerColor.BLUE) Position(8, 3) else Position(0, 3)
        val myChokePoints: Set<Position> = if (playerSide == PlayerColor.BLUE) {
            setOf(Position(8, 2), Position(8, 4), Position(7, 3), Position(7, 2), Position(7, 4))
        } else {
            setOf(Position(0, 2), Position(0, 4), Position(1, 3), Position(1, 2), Position(1, 4))
        }

        // 🚨 1. 大本营受威胁检测
        data class Invader(val pos: Position, val piece: Piece, val dist: Int)
        val urgentHomeInvaders = mutableListOf<Invader>()
        for ((rPos, rPiece) in board) {
            if (rPiece.owner != playerSide) {
                val dist = abs(rPos.row - myDen.row) + abs(rPos.col - myDen.col)
                if (dist <= 3) {
                    urgentHomeInvaders.add(Invader(rPos, rPiece, dist))
                }
            }
        }
        val isHomeUnderSiege = urgentHomeInvaders.isNotEmpty()
        val closestInvader = urgentHomeInvaders.minByOrNull { it.dist }

        // 🚨 2. 我方大象受敌鼠威胁检测
        var myElephantPos: Position? = null
        var oppRatPos: Position? = null
        for ((pos, piece) in board) {
            if (piece.owner == playerSide && piece.type == AnimalType.ELEPHANT) myElephantPos = pos
            if (piece.owner != playerSide && piece.type == AnimalType.RAT) oppRatPos = pos
        }
        var ratDistToMyElephant = 99
        if (myElephantPos != null && oppRatPos != null) {
            ratDistToMyElephant = abs(myElephantPos.row - oppRatPos.row) + abs(myElephantPos.col - oppRatPos.col)
        }

        // 🌊 3. 水中敌鼠封锁检测
        val isOppRatInRiver = if (oppRatPos != null) getTileType(oppRatPos) is TileType.River else false

        val evaluatedMoves = mutableListOf<CandidateMove>()

        for ((from, piece) in board) {
            if (piece.owner != playerSide) continue
            val moves = calculateValidMoves(from)
            val isCurrentPieceUnderThreat = isPositionThreatened(from, piece, board)

            for (to in moves) {
                val pieceName = JungleLanguageManager.shared.animalName(piece.type)
                // 🏆 一步直接占穴获胜 (绝对终局最优解)
                if (to == enemyDen) {
                    val winnerName = JungleLanguageManager.shared.playerName(playerSide)
                    val winAdv = OptimalMoveAdvice(
                        from = from, to = to, piece = piece,
                        headline = JungleLanguageManager.shared.adviceWinHeadline,
                        detailReason = JungleLanguageManager.shared.adviceWinDetail,
                        forecast3Steps = JungleLanguageManager.shared.adviceWinForecast(piece = pieceName, winner = winnerName),
                        badgeText = JungleLanguageManager.shared.adviceWinBadge,
                        isEmergency = false, isWin = true, isTrade = false
                    )
                    return listOf(CandidateMove(from, to, piece, 10_000_000, winAdv))
                }

                // 1. 模拟 Step 1：我方走棋
                val simBoard1 = board.toMutableMap()
                val captured1 = simBoard1[to]
                simBoard1.remove(from)
                simBoard1[to] = piece

                // 2. Alpha-Beta + Quiescence 主力战术评估 (深度 3 步 + 延伸吃子)
                val minimaxScore = -alphaBetaSearch(simBoard1, depth = 2, alpha = -9_999_999, beta = 9_999_999, player = playerSide.opponent)

                // 3. 提取主变例 (敌方最强战术反制预测)
                val oppMoves = getAllSimMovesForPlayer(simBoard1, playerSide.opponent)
                val bestOppMove = orderSimMoves(oppMoves, simBoard1, playerSide.opponent).firstOrNull()

                val isDangerousDest = calculateDangerousMoves(from, listOf(to)).contains(to)
                val toTile = getTileType(to)
                val curDist = abs(from.row - enemyDen.row) + abs(from.col - enemyDen.col)
                val newDist = abs(to.row - enemyDen.row) + abs(to.col - enemyDen.col)

                var totalScore = minimaxScore
                var headline = JungleLanguageManager.shared.recommendedMoveHeadline(piece.type)
                var detail = JungleLanguageManager.shared.recommendedMoveDetail
                var badge = JungleLanguageManager.shared.recommendedMoveBadge
                var isEm = false
                var isTr = false
                var forecastText = ""

                // 🚨 4. 【大本营生死保卫战】
                if (isHomeUnderSiege && closestInvader != null) {
                    val inv = closestInvader
                    val invName = JungleLanguageManager.shared.animalName(inv.piece.type)
                    val curDistToDen = abs(from.row - myDen.row) + abs(from.col - myDen.col)
                    val newDistToDen = abs(to.row - myDen.row) + abs(to.col - myDen.col)
                    val curDistToInv = abs(from.row - inv.pos.row) + abs(from.col - inv.pos.col)
                    val newDistToInv = abs(to.row - inv.pos.row) + abs(to.col - inv.pos.col)

                    if (to == inv.pos) {
                        totalScore += 25000
                        headline = JungleLanguageManager.shared.adviceCounterKillHeadline
                        detail = JungleLanguageManager.shared.adviceCounterKillDetail(piece = pieceName, inv = invName)
                        badge = JungleLanguageManager.shared.adviceCounterKillBadge
                        isEm = true
                    } else if (to == myDen || myChokePoints.contains(to)) {
                        totalScore += 18000
                        headline = JungleLanguageManager.shared.adviceBlockGateHeadline
                        detail = JungleLanguageManager.shared.adviceBlockGateDetail(piece = pieceName, inv = invName)
                        badge = JungleLanguageManager.shared.adviceBlockGateBadge
                        isEm = true
                    } else if (newDistToDen < curDistToDen || newDistToInv < curDistToInv) {
                        totalScore += 10000
                        headline = JungleLanguageManager.shared.adviceFallBackHeadline
                        detail = JungleLanguageManager.shared.adviceFallBackDetail(piece = pieceName, inv = invName)
                        badge = JungleLanguageManager.shared.adviceFallBackBadge
                        isEm = true
                    }
                    forecastText = JungleLanguageManager.shared.adviceHomeDefenseForecast(piece = pieceName)
                }
                // 🛡️ 5. 【己方受威胁棋子紧急避险/逃入河中】
                else if (isCurrentPieceUnderThreat && !isDangerousDest) {
                    totalScore += 8000 + piece.type.pieceValue / 2
                    isEm = true

                    if (toTile is TileType.River && piece.type == AnimalType.RAT) {
                        headline = JungleLanguageManager.shared.adviceRatSwimEvadeHeadline
                        detail = JungleLanguageManager.shared.adviceRatSwimEvadeDetail
                        badge = JungleLanguageManager.shared.adviceRatSwimEvadeBadge
                        forecastText = JungleLanguageManager.shared.adviceRatSwimEvadeForecast
                    } else if (piece.type == AnimalType.RAT) {
                        headline = JungleLanguageManager.shared.adviceRatFleeHeadline
                        detail = JungleLanguageManager.shared.adviceRatFleeDetail
                        badge = JungleLanguageManager.shared.adviceRatFleeBadge
                        forecastText = JungleLanguageManager.shared.adviceRatFleeForecast
                    } else {
                        headline = JungleLanguageManager.shared.advicePieceRetreatHeadline(piece = pieceName)
                        detail = JungleLanguageManager.shared.advicePieceRetreatDetail(piece = pieceName)
                        badge = JungleLanguageManager.shared.advicePieceRetreatBadge
                        forecastText = JungleLanguageManager.shared.advicePieceRetreatForecast(piece = pieceName)
                    }
                }
                // 🌊 6. 【水中清障·解除跳河封锁】
                else if (isOppRatInRiver && oppRatPos != null && piece.type == AnimalType.RAT) {
                    val rPos = oppRatPos
                    if (to == rPos) {
                        totalScore += 15000
                        headline = JungleLanguageManager.shared.adviceHuntRiverRatHeadline
                        detail = JungleLanguageManager.shared.adviceHuntRiverRatDetail
                        badge = JungleLanguageManager.shared.adviceHuntRiverRatBadge
                        forecastText = JungleLanguageManager.shared.adviceHuntRiverRatForecast
                    } else {
                        val curD = abs(from.row - rPos.row) + abs(from.col - rPos.col)
                        val newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if (newD < curD) {
                            totalScore += 4000
                            headline = JungleLanguageManager.shared.adviceUnderwaterPursuitHeadline
                            detail = JungleLanguageManager.shared.adviceUnderwaterPursuitDetail
                            badge = JungleLanguageManager.shared.adviceUnderwaterPursuitBadge
                            forecastText = JungleLanguageManager.shared.adviceUnderwaterPursuitForecast
                        }
                    }
                }
                // 🐘 7. 【大象遭遇老鼠生死危机】
                else if (ratDistToMyElephant <= 2 && oppRatPos != null) {
                    val rPos = oppRatPos
                    if (piece.type != AnimalType.ELEPHANT && to == rPos) {
                        totalScore += 18000
                        headline = JungleLanguageManager.shared.adviceProtectElephantHeadline
                        detail = JungleLanguageManager.shared.adviceProtectElephantDetail(piece = pieceName)
                        badge = JungleLanguageManager.shared.adviceProtectElephantBadge
                        forecastText = JungleLanguageManager.shared.adviceProtectElephantForecast(piece = pieceName)
                    } else if (ratDistToMyElephant == 1 && piece.type == AnimalType.ELEPHANT) {
                        val newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if (newD > 1 && !isDangerousDest) {
                            totalScore += 16000
                            headline = JungleLanguageManager.shared.adviceElephantEscapeHeadline
                            detail = JungleLanguageManager.shared.adviceElephantEscapeDetail
                            badge = JungleLanguageManager.shared.adviceElephantEscapeBadge
                            forecastText = JungleLanguageManager.shared.adviceElephantEscapeForecast
                        }
                    } else if (ratDistToMyElephant == 2 && piece.type != AnimalType.ELEPHANT) {
                        val curD = abs(from.row - rPos.row) + abs(from.col - rPos.col)
                        val newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if (newD < curD) {
                            totalScore += 5000
                            headline = JungleLanguageManager.shared.adviceFlankRatHeadline
                            detail = JungleLanguageManager.shared.adviceFlankRatDetail(piece = pieceName)
                            badge = JungleLanguageManager.shared.adviceFlankRatBadge
                            forecastText = JungleLanguageManager.shared.adviceFlankRatForecast(piece = pieceName)
                        }
                    }
                }
                // ⚖️ 8. 【正常吃子与战术兑子分析】
                else if (captured1 != null) {
                    val cap = captured1
                    val capName = JungleLanguageManager.shared.animalName(cap.type)
                    val netProfit = cap.type.pieceValue - piece.type.pieceValue

                    if (isDangerousDest) {
                        if (netProfit > 0) {
                            val isRatElephant = (piece.type == AnimalType.RAT && cap.type == AnimalType.ELEPHANT)
                            headline = JungleLanguageManager.shared.adviceProfitableTradeHeadline(piece = pieceName, cap = capName, isRatElephant = isRatElephant)
                            detail = JungleLanguageManager.shared.adviceProfitableTradeDetail(cap = capName, netProfit = netProfit)
                            badge = JungleLanguageManager.shared.adviceProfitableTradeBadge(netProfit = netProfit)
                            isTr = true
                            val oppKillerName = if (bestOppMove != null) JungleLanguageManager.shared.animalName(bestOppMove.piece.type) else JungleLanguageManager.shared.enemyStrongPiece
                            forecastText = JungleLanguageManager.shared.adviceProfitableTradeForecast(
                                piece = pieceName,
                                cap = capName,
                                capVal = cap.type.pieceValue,
                                oppKiller = oppKillerName,
                                pieceVal = piece.type.pieceValue,
                                netProfit = netProfit
                            )
                        } else {
                            totalScore -= 6000
                            headline = JungleLanguageManager.shared.adviceBadTradeHeadline
                            detail = JungleLanguageManager.shared.adviceBadTradeDetail(cap = capName, loss = abs(netProfit))
                            badge = JungleLanguageManager.shared.adviceBadTradeBadge
                            forecastText = JungleLanguageManager.shared.adviceBadTradeForecast(cap = capName)
                        }
                    } else {
                        val isRiverJump = (piece.type == AnimalType.LION || piece.type == AnimalType.TIGER) && (abs(from.row - to.row) > 1)
                        headline = JungleLanguageManager.shared.adviceFreeCaptureHeadline(cap = capName, isJumpRiver = isRiverJump)
                        detail = JungleLanguageManager.shared.adviceFreeCaptureDetail(piece = pieceName, cap = capName, value = cap.type.pieceValue)
                        badge = JungleLanguageManager.shared.adviceFreeCaptureBadge(cap = capName)
                        forecastText = JungleLanguageManager.shared.adviceFreeCaptureForecast(piece = pieceName, cap = capName)
                    }
                }
                // 🚶 9. 正常推进
                else {
                    if (newDist < curDist && !isDangerousDest) {
                        headline = if (newDist <= 2) JungleLanguageManager.shared.adviceAssaultDenHeadline else JungleLanguageManager.shared.adviceApproachDenHeadline
                        detail = if (newDist <= 2) JungleLanguageManager.shared.adviceAssaultDenDetail else JungleLanguageManager.shared.adviceApproachDenDetail(piece = pieceName)
                        badge = if (newDist <= 2) JungleLanguageManager.shared.adviceAssaultDenBadge else JungleLanguageManager.shared.adviceApproachDenBadge
                    }
                }

                if (forecastText.isEmpty()) {
                    val step1Desc = JungleLanguageManager.shared.forecastStep1(piece.type)
                    val step2Desc = JungleLanguageManager.shared.forecastStep2(bestOppMove?.piece?.type)
                    val step3Desc = JungleLanguageManager.shared.forecastStep3
                    forecastText = "$step1Desc ➔ $step2Desc ➔ $step3Desc"
                }

                // 🛑 10. 智能禁长捉防循环死锁惩罚 (逃命与吃子绝不惩罚)
                val repPenalty = calculateRepetitionPenalty(from, to, piece, isCurrentPieceUnderThreat, captured1 != null)
                totalScore -= repPenalty

                val adv = OptimalMoveAdvice(
                    from = from, to = to, piece = piece,
                    headline = headline, detailReason = detail,
                    forecast3Steps = forecastText, badgeText = badge,
                    isEmergency = isEm, isWin = false, isTrade = isTr
                )
                evaluatedMoves.add(CandidateMove(from, to, piece, totalScore, adv))
            }
        }

        return evaluatedMoves.sortedByDescending { it.score }
    }

    // 🎯 玩家最优解决策 (全面覆盖：大本营保卫 + 己方受威胁紧急逃生/入河避险 + 水中清障 + 战术进攻)
    fun computePlayerOptimalMove() {
        val playerSide = if (isVsAI) PlayerColor.BLUE else currentTurn
        val candidates = searchOptimalMoveCandidates(playerSide)
        this.optimalAdvice = candidates.firstOrNull()?.advice
    }

    // MARK: - 🤖 红方电脑走棋与技能决策
    fun evaluateAndTriggerAISkills() {
        if (!isVsAI || currentTurn != PlayerColor.RED || winner != null) return

        // 1. 🛡️ 守家绝技：当蓝方逼近兽穴 (距离 <= 2) 且门前防守空虚时，召回最强猛兽
        if (redShieldSkillCount > 0) {
            val blueThreatsNearDen = board.filter { (pos, piece) ->
                piece.owner == PlayerColor.BLUE && (abs(pos.row - 0) + abs(pos.col - 3) <= 2)
            }
            val gate = Position(1, 3)
            val gateDefender = board[gate]
            val needGateHelp = gateDefender == null || (gateDefender.owner == PlayerColor.RED && gateDefender.type.rank < 6)
            if (blueThreatsNearDen.isNotEmpty() && needGateHelp) {
                activateShieldRecallSkill(PlayerColor.RED)
                return
            }
        }

        // 2. ❄️ 控场冰封：当蓝方逼近红方兽穴，或者蓝方开启了狂暴，红方冰封蓝方最有威胁的核心
        if (redFreezeSkillCount > 0 && frozenPiecePos == null) {
            val playerNearRedDen = board.entries.firstOrNull { (pos, piece) ->
                piece.owner == PlayerColor.BLUE && (abs(pos.row - 0) + abs(pos.col - 3) <= 2)
            }
            if (playerNearRedDen != null) {
                activateFreezeSkill(PlayerColor.RED)
                return
            } else if (blueFrenzyRemainingTurns > 0) {
                activateFreezeSkill(PlayerColor.RED)
                return
            }
        }

        // 3. ✨ 逆袭越级：当红方弱兽 (鼠猫狗狼) 与蓝方大兽 (狮虎象) 相邻，开启越级完成斩杀
        if (redFrenzySkillCount > 0 && redFrenzyRemainingTurns == 0) {
            for ((redPos, redPiece) in board) {
                if (redPiece.owner == PlayerColor.RED && redPiece.type.rank <= 4) {
                    val neighbors = listOf(
                        Position(redPos.row - 1, redPos.col),
                        Position(redPos.row + 1, redPos.col),
                        Position(redPos.row, redPos.col - 1),
                        Position(redPos.row, redPos.col + 1)
                    )
                    for (nPos in neighbors) {
                        val bluePiece = board[nPos]
                        if (bluePiece != null && bluePiece.owner == PlayerColor.BLUE && bluePiece.type.rank >= 6) {
                            activateFrenzySkill(PlayerColor.RED)
                            return
                        }
                    }
                }
            }
        }
    }

    fun triggerAIMove() {
        evaluateAndTriggerAISkills()
        val candidates = searchOptimalMoveCandidates(PlayerColor.RED)
        this.isThinking = false

        val first = candidates.firstOrNull()
        if (first == null) {
            this.winner = PlayerColor.BLUE
            return
        }

        val chosen = when (aiDifficulty) {
            AIDifficulty.HARD -> first
            AIDifficulty.MEDIUM -> {
                val topGroup = candidates.filter { it.score >= first.score - 300 }
                topGroup.randomOrNull() ?: first
            }
            AIDifficulty.EASY -> {
                val topGroup = candidates.filter { it.score >= first.score - 750 }
                topGroup.randomOrNull() ?: first
            }
        }

        this.makeMove(chosen.from, chosen.to)
    }

    private fun calculateValidMovesOnBoard(boardState: Map<Position, Piece>, from: Position): List<Position> {
        if (from == frozenPiecePos) return emptyList()
        val piece = boardState[from] ?: return emptyList()
        val result = mutableListOf<Position>()
        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        for ((dr, dc) in directions) {
            val nr = from.row + dr
            val nc = from.col + dc
            val targetPos = Position(nr, nc)
            if (nr !in 0..8 || nc !in 0..6) continue

            val targetTile = getTileType(targetPos)
            if (targetTile is TileType.Den && targetTile.owner == piece.owner) continue

            if (targetTile is TileType.River) {
                if (piece.type == AnimalType.RAT) {
                    if (canOccupyOrCaptureOnBoard(boardState, piece, from, targetPos)) {
                        result.add(targetPos)
                    }
                } else if (piece.type == AnimalType.LION || piece.type == AnimalType.TIGER) {
                    val landing = getRiverJumpLandingOnBoard(boardState, from, dr, dc)
                    if (landing != null && canOccupyOrCaptureOnBoard(boardState, piece, from, landing)) {
                        result.add(landing)
                    }
                }
            } else {
                if (canOccupyOrCaptureOnBoard(boardState, piece, from, targetPos)) {
                    result.add(targetPos)
                }
            }
        }
        return result
    }

    private fun getRiverJumpLandingOnBoard(boardState: Map<Position, Piece>, from: Position, dr: Int, dc: Int): Position? {
        var currR = from.row + dr
        var currC = from.col + dc
        while (currR in 0..8 && currC in 0..6) {
            val pos = Position(currR, currC)
            if (getTileType(pos) is TileType.River) {
                if (boardState[pos] != null) return null
                currR += dr
                currC += dc
            } else {
                return pos
            }
        }
        return null
    }

    private fun canOccupyOrCaptureOnBoard(boardState: Map<Position, Piece>, attacker: Piece, from: Position, to: Position): Boolean {
        val defender = boardState[to] ?: return true
        if (defender.owner == attacker.owner) return false

        if (attacker.owner == PlayerColor.BLUE && blueFrenzyRemainingTurns > 0) {
            return true
        }
        if (attacker.owner == PlayerColor.RED && redFrenzyRemainingTurns > 0) {
            return true
        }

        val fromTile = getTileType(from)
        val toTile = getTileType(to)

        if (toTile is TileType.Trap && toTile.owner == attacker.owner) {
            return true
        }
        if (attacker.type == AnimalType.RAT && defender.type == AnimalType.ELEPHANT) {
            if (fromTile is TileType.River && toTile !is TileType.River) return false
            return true
        }
        if (attacker.type == AnimalType.ELEPHANT && defender.type == AnimalType.RAT) {
            return false
        }
        if (fromTile is TileType.River && toTile !is TileType.River) return false
        if (fromTile !is TileType.River && toTile is TileType.River) return false

        return attacker.type.rank >= defender.type.rank
    }

    fun getActionBadge(pos: Position, from: Position): String? {
        val movingPiece = board[from] ?: return null
        if (movingPiece.owner != PlayerColor.BLUE && isVsAI) return null
        if (!validMoves.contains(pos)) return null

        if (optimalAdvice?.from == from && optimalAdvice?.to == pos) {
            return optimalAdvice?.badgeText
        }

        val target = board[pos]
        if (target != null && target.owner != movingPiece.owner) {
            if (movingPiece.type == AnimalType.RAT && target.type == AnimalType.ELEPHANT) {
                return "⚡️ ${JungleLanguageManager.shared.eatElephantBadge}"
            }
            if (target.type == AnimalType.RAT) {
                return "🛡️ ${JungleLanguageManager.shared.protectElephantBadge}"
            }
            return "🎯 ${JungleLanguageManager.shared.captureTargetBadge(target.type)}"
        }

        if ((movingPiece.type == AnimalType.LION || movingPiece.type == AnimalType.TIGER) && (abs(from.row - pos.row) > 1 || abs(from.col - pos.col) > 1)) {
            return "🌊 ${JungleLanguageManager.shared.jumpRiverBadge}"
        }

        val tile = getTileType(pos)
        if (tile is TileType.Den && tile.owner != movingPiece.owner) {
            return "🏆 ${JungleLanguageManager.shared.winGameBadge}"
        }

        return null
    }

    fun calculateDangerousMoves(from: Position, moves: List<Position>): Set<Position> {
        val movingPiece = board[from] ?: return emptySet()
        val dangerous = mutableSetOf<Position>()
        val opponent = movingPiece.owner.opponent

        for (dest in moves) {
            for ((oppPos, oppPiece) in board) {
                if (oppPiece.owner == opponent && oppPos != dest) {
                    val oppMoves = calculateValidMovesHypothetical(oppPos, oppPiece, from, dest to movingPiece)
                    if (oppMoves.contains(dest)) {
                        dangerous.add(dest)
                        break
                    }
                }
            }
        }
        return dangerous
    }

    private fun calculateValidMovesHypothetical(
        from: Position,
        piece: Piece,
        ignorePos: Position,
        replacePos: Pair<Position, Piece>
    ): List<Position> {
        val result = mutableListOf<Position>()
        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        val simBoard = board.toMutableMap()
        simBoard.remove(ignorePos)
        simBoard[replacePos.first] = replacePos.second

        for ((dr, dc) in directions) {
            val nr = from.row + dr
            val nc = from.col + dc
            val targetPos = Position(nr, nc)
            if (nr !in 0..8 || nc !in 0..6) continue

            val targetTile = getTileType(targetPos)
            if (targetTile is TileType.Den && targetTile.owner == piece.owner) continue

            val destPiece: Piece? = if (targetPos == replacePos.first) {
                replacePos.second
            } else if (targetPos != ignorePos) {
                simBoard[targetPos]
            } else {
                null
            }

            if (targetTile is TileType.River) {
                if (piece.type == AnimalType.RAT) {
                    if (destPiece != null) {
                        if (destPiece.owner != piece.owner && piece.type.rank >= destPiece.type.rank) {
                            result.add(targetPos)
                        }
                    } else {
                        result.add(targetPos)
                    }
                } else if (piece.type == AnimalType.LION || piece.type == AnimalType.TIGER) {
                    val landing = getRiverJumpLandingOnBoard(simBoard, from, dr, dc)
                    if (landing != null && canOccupyOrCaptureOnBoard(simBoard, piece, from, landing)) {
                        result.add(landing)
                    }
                }
            } else {
                if (destPiece != null) {
                    if (destPiece.owner != piece.owner) {
                        if (targetTile is TileType.Trap && targetTile.owner == piece.owner) {
                            result.add(targetPos)
                        } else if (piece.type == AnimalType.RAT && destPiece.type == AnimalType.ELEPHANT) {
                            result.add(targetPos)
                        } else if (piece.type == AnimalType.ELEPHANT && destPiece.type == AnimalType.RAT) {
                            // Elephant cannot eat rat
                        } else if (piece.type.rank >= destPiece.type.rank) {
                            result.add(targetPos)
                        }
                    }
                } else {
                    result.add(targetPos)
                }
            }
        }
        return result
    }

    fun calculateValidMoves(from: Position): List<Position> {
        if (from == frozenPiecePos) return emptyList()
        val piece = board[from] ?: return emptyList()
        val result = mutableListOf<Position>()
        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        for ((dr, dc) in directions) {
            val nextR = from.row + dr
            val nextC = from.col + dc
            val targetPos = Position(nextR, nextC)
            if (nextR !in 0..8 || nextC !in 0..6) continue

            val targetTile = getTileType(targetPos)
            if (targetTile is TileType.Den && targetTile.owner == piece.owner) continue

            if (targetTile is TileType.River) {
                if (piece.type == AnimalType.RAT) {
                    if (canOccupyOrCapture(piece, from, targetPos)) {
                        result.add(targetPos)
                    }
                } else if (piece.type == AnimalType.LION || piece.type == AnimalType.TIGER) {
                    val landingPos = getRiverJumpLanding(from, dr, dc)
                    if (landingPos != null && canOccupyOrCapture(piece, from, landingPos)) {
                        result.add(landingPos)
                    }
                }
            } else {
                if (canOccupyOrCapture(piece, from, targetPos)) {
                    result.add(targetPos)
                }
            }
        }
        return result
    }

    private fun getRiverJumpLanding(from: Position, dr: Int, dc: Int): Position? {
        var currR = from.row + dr
        var currC = from.col + dc
        while (currR in 0..8 && currC in 0..6) {
            val pos = Position(currR, currC)
            if (getTileType(pos) is TileType.River) {
                if (board[pos] != null) return null
                currR += dr
                currC += dc
            } else {
                return pos
            }
        }
        return null
    }

    private fun canOccupyOrCapture(attacker: Piece, from: Position, to: Position): Boolean {
        val defender = board[to] ?: return true
        if (defender.owner == attacker.owner) return false

        if (attacker.owner == PlayerColor.BLUE && blueFrenzyRemainingTurns > 0) {
            return true
        }
        if (attacker.owner == PlayerColor.RED && redFrenzyRemainingTurns > 0) {
            return true
        }

        val fromTile = getTileType(from)
        val toTile = getTileType(to)

        if (toTile is TileType.Trap && toTile.owner == attacker.owner) {
            return true
        }
        if (attacker.type == AnimalType.RAT && defender.type == AnimalType.ELEPHANT) {
            if (fromTile is TileType.River && toTile !is TileType.River) return false
            return true
        }
        if (attacker.type == AnimalType.ELEPHANT && defender.type == AnimalType.RAT) {
            return false
        }
        if (fromTile is TileType.River && toTile !is TileType.River) return false
        if (fromTile !is TileType.River && toTile is TileType.River) return false

        return attacker.type.rank >= defender.type.rank
    }

    private fun checkWin(afterMoveTo: Position, by: PlayerColor): Boolean {
        val tile = getTileType(afterMoveTo)
        if (tile is TileType.Den && tile.owner != by) {
            return true
        }
        val opponentHasPieces = board.values.any { it.owner == by.opponent }
        return !opponentHasPieces
    }
}
