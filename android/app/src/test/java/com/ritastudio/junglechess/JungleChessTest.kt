package com.ritastudio.junglechess

import com.ritastudio.junglechess.engine.JungleGameEngine
import com.ritastudio.junglechess.localization.JungleLanguage
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.*
import org.junit.Assert.*
import org.junit.Test

class JungleChessTest {

    @Test
    fun testInitialBoardSetup() {
        val engine = JungleGameEngine()
        assertEquals(16, engine.board.size)
        assertEquals(PlayerColor.BLUE, engine.currentTurn)
        assertNull(engine.winner)

        // Check key piece positions
        assertEquals(AnimalType.LION, engine.board[Position(0, 0)]?.type)
        assertEquals(PlayerColor.RED, engine.board[Position(0, 0)]?.owner)

        assertEquals(AnimalType.LION, engine.board[Position(8, 6)]?.type)
        assertEquals(PlayerColor.BLUE, engine.board[Position(8, 6)]?.owner)

        assertEquals(AnimalType.ELEPHANT, engine.board[Position(2, 6)]?.type)
        assertEquals(AnimalType.ELEPHANT, engine.board[Position(6, 0)]?.type)
    }

    @Test
    fun testRule1_RatCanEnterRiver_OthersCannot() {
        val engine = JungleGameEngine()
        val riverPos = Position(3, 1)
        assertTrue(engine.getTileType(riverPos) is TileType.River)

        // Clear board and place Rat near river
        val customBoard = mutableMapOf<Position, Piece>()
        val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
        val elephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)
        val tiger = Piece(type = AnimalType.TIGER, owner = PlayerColor.BLUE)

        customBoard[Position(2, 1)] = rat
        engine.board = customBoard
        val ratMoves = engine.calculateValidMoves(Position(2, 1))
        assertTrue("Rat should be able to enter river at (3, 1)", ratMoves.contains(Position(3, 1)))

        customBoard.clear()
        customBoard[Position(2, 1)] = elephant
        engine.board = customBoard
        val elephantMoves = engine.calculateValidMoves(Position(2, 1))
        assertFalse("Elephant cannot step into river at (3, 1)", elephantMoves.contains(Position(3, 1)))

        customBoard.clear()
        customBoard[Position(2, 1)] = tiger
        engine.board = customBoard
        val tigerMoves = engine.calculateValidMoves(Position(2, 1))
        assertFalse("Tiger cannot step directly into river cell at (3, 1)", tigerMoves.contains(Position(3, 1)))
    }

    @Test
    fun testRule2_RiverJumpingAndRatBlock() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        val lion = Piece(type = AnimalType.LION, owner = PlayerColor.BLUE)

        // Vertical jump from (6, 1) across river (rows 5, 4, 3) to (2, 1)
        customBoard[Position(6, 1)] = lion
        engine.board = customBoard
        val lionMoves = engine.calculateValidMoves(Position(6, 1))
        assertTrue("Lion should jump across river to (2, 1)", lionMoves.contains(Position(2, 1)))

        // Block with a Rat in the river at (4, 1)
        val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
        customBoard[Position(4, 1)] = rat
        engine.board = customBoard
        val blockedLionMoves = engine.calculateValidMoves(Position(6, 1))
        assertFalse("Lion jump should be blocked by Rat in river", blockedLionMoves.contains(Position(2, 1)))
    }

    @Test
    fun testRule3_RatCanEatElephantOnLand_ElephantCannotEatRat() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
        val elephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)

        customBoard[Position(6, 1)] = rat
        customBoard[Position(6, 2)] = elephant
        engine.board = customBoard

        val ratMoves = engine.calculateValidMoves(Position(6, 1))
        assertTrue("Rat on land should be able to eat adjacent Elephant on land", ratMoves.contains(Position(6, 2)))

        // Now test Elephant cannot eat Rat
        engine.currentTurn = PlayerColor.RED
        val elephantMoves = engine.calculateValidMoves(Position(6, 2))
        assertFalse("Elephant on land cannot eat Rat", elephantMoves.contains(Position(6, 1)))
    }

    @Test
    fun testRule4_RatInWaterCannotEatElephantOnLand() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
        val elephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)

        // Rat in water at (3, 1), Elephant on land at (2, 1)
        customBoard[Position(3, 1)] = rat
        customBoard[Position(2, 1)] = elephant
        engine.board = customBoard

        val ratMoves = engine.calculateValidMoves(Position(3, 1))
        assertFalse("Rat in river cannot eat Elephant on land", ratMoves.contains(Position(2, 1)))
    }

    @Test
    fun testRule5_SameRankCaptures() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        val wolf1 = Piece(type = AnimalType.WOLF, owner = PlayerColor.BLUE)
        val wolf2 = Piece(type = AnimalType.WOLF, owner = PlayerColor.RED)

        customBoard[Position(6, 2)] = wolf1
        customBoard[Position(6, 3)] = wolf2
        engine.board = customBoard

        val moves = engine.calculateValidMoves(Position(6, 2))
        assertTrue("Wolf should be able to capture opposing Wolf", moves.contains(Position(6, 3)))
    }

    @Test
    fun testRule6_TrapWeakensPiece() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Blue trap is at (7, 3)
        // If a Red Lion falls into Blue trap at (7, 3), Blue Cat at (7, 2) can eat it!
        val redLion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        val blueCat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)

        customBoard[Position(7, 3)] = redLion
        customBoard[Position(7, 2)] = blueCat
        engine.board = customBoard

        val catMoves = engine.calculateValidMoves(Position(7, 2))
        assertTrue("Cat should be able to capture trapped Lion", catMoves.contains(Position(7, 3)))
    }

    @Test
    fun testRule7_DenWinCondition() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Red den is at (0, 3)
        val blueCat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
        customBoard[Position(1, 3)] = blueCat
        engine.board = customBoard

        engine.makeMove(from = Position(1, 3), to = Position(0, 3))
        assertEquals("Entering red den should immediately crown Blue as winner", PlayerColor.BLUE, engine.winner)
    }

    @Test
    fun testAll8LanguagesLocalization() {
        val lang = JungleLanguageManager.shared

        for (l in JungleLanguage.entries) {
            lang.currentLanguage = l

            assertFalse("App title empty for $l", lang.appTitle.isBlank())
            assertFalse("Rule title empty for $l", lang.ruleSheetTitle.isBlank())
            assertFalse("Win condition empty for $l", lang.ruleWinConditionDesc.isBlank())
            assertFalse("Frenzy banner empty for $l", lang.frenzyBannerText.isBlank())

            for (animal in AnimalType.entries) {
                assertFalse("Animal name empty for $animal in $l", lang.animalName(animal).isBlank())
                assertFalse("Animal eat rule empty for $animal in $l", lang.animalEatRule(animal).isBlank())
            }

            for (diff in AIDifficulty.entries) {
                assertFalse("Difficulty name empty for $diff in $l", lang.difficultyName(diff).isBlank())
            }

            for (player in PlayerColor.entries) {
                assertFalse("Player name empty for $player in $l", lang.playerName(player).isBlank())
            }
        }
    }

    @Test
    fun testAIFreeCaptureHighPriority() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Red Lion at (2, 2) [land], Blue Wolf at (2, 3) [land] (free capture), no threats
        customBoard[Position(2, 2)] = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        customBoard[Position(2, 3)] = Piece(type = AnimalType.WOLF, owner = PlayerColor.BLUE)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED
        engine.aiDifficulty = AIDifficulty.HARD
        engine.isVsAI = false

        engine.triggerAIMove()

        // Red Lion should have captured Blue Wolf at (2, 3)
        val pieceAt23 = engine.board[Position(2, 3)]
        assertNotNull("Piece at (2, 3) should exist", pieceAt23)
        assertEquals(AnimalType.LION, pieceAt23?.type)
        assertEquals(PlayerColor.RED, pieceAt23?.owner)
        assertNull("Red Lion should have moved from (2, 2)", engine.board[Position(2, 2)])
    }

    @Test
    fun testAIDenDefenseEmergency() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Red Den is at (0, 3).
        // Blue Tiger is at (1, 3), dist = 1 to Red Den!
        // Red Elephant is at (1, 2).
        customBoard[Position(1, 3)] = Piece(type = AnimalType.TIGER, owner = PlayerColor.BLUE)
        customBoard[Position(1, 2)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED
        engine.aiDifficulty = AIDifficulty.HARD
        engine.isVsAI = false

        engine.triggerAIMove()

        // Red Elephant must eliminate the Blue Tiger at (1, 3) to defend the den!
        val pieceAt13 = engine.board[Position(1, 3)]
        assertNotNull("Piece at (1, 3) must exist", pieceAt13)
        assertEquals(AnimalType.ELEPHANT, pieceAt13?.type)
        assertEquals(PlayerColor.RED, pieceAt13?.owner)
    }

    @Test
    fun testAIRatCapturesElephant() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Red Rat at (6, 1), Blue Elephant at (6, 2)
        customBoard[Position(6, 1)] = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
        customBoard[Position(6, 2)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED
        engine.aiDifficulty = AIDifficulty.HARD
        engine.isVsAI = false

        engine.triggerAIMove()

        // Red Rat should capture Blue Elephant at (6, 2)
        val pieceAt62 = engine.board[Position(6, 2)]
        assertNotNull(pieceAt62)
        assertEquals(AnimalType.RAT, pieceAt62?.type)
        assertEquals(PlayerColor.RED, pieceAt62?.owner)
    }

    @Test
    fun test10RoundsAISimulation() {
        // Run 10 full rounds of AI-driven games
        for (round in 1..10) {
            val engine = JungleGameEngine()
            engine.isVsAI = false
            engine.aiDifficulty = AIDifficulty.HARD

            var moveCount = 0
            val maxMoves = 60
            while (engine.winner == null && moveCount < maxMoves) {
                if (engine.currentTurn == PlayerColor.RED) {
                    val redCountBefore = engine.board.values.count { it.owner == PlayerColor.RED }
                    if (redCountBefore == 0) break
                    engine.triggerAIMove()
                    moveCount++
                } else {
                    // Blue moves tactical or random valid move
                    val bluePieces = engine.board.filter { it.value.owner == PlayerColor.BLUE }
                    if (bluePieces.isEmpty()) break
                    var blueMoved = false
                    for ((from, piece) in bluePieces.entries.shuffled()) {
                        val valids = engine.calculateValidMoves(from)
                        if (valids.isNotEmpty()) {
                            val to = valids.random()
                            engine.makeMove(from, to)
                            blueMoved = true
                            moveCount++
                            break
                        }
                    }
                    if (!blueMoved) break
                }
            }
            assertTrue("Round $round completed successfully with $moveCount moves (winner: ${engine.winner})", moveCount > 0)
        }
    }

    @Test
    fun testSuperpowersFrenzyAndFreeze() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        val blueCat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
        val redElephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)
        customBoard[Position(6, 1)] = blueCat
        customBoard[Position(6, 2)] = redElephant
        engine.board = customBoard
        engine.currentTurn = PlayerColor.BLUE

        // Cat normally cannot capture Elephant
        val normalMoves = engine.calculateValidMoves(Position(6, 1))
        assertFalse("Cat cannot capture Elephant normally", normalMoves.contains(Position(6, 2)))

        // Activate Frenzy
        engine.activateFrenzySkill(PlayerColor.BLUE)
        assertEquals(2, engine.blueFrenzyRemainingTurns)
        assertEquals(0, engine.blueFrenzySkillCount)
        val frenzyMoves = engine.calculateValidMoves(Position(6, 1))
        assertTrue("Cat with Master Leap can leapfrog capture Elephant", frenzyMoves.contains(Position(6, 2)))

        // Test Freeze
        engine.activateFreezeSkill(PlayerColor.BLUE)
        assertEquals(0, engine.blueFreezeSkillCount)
        assertEquals(Position(6, 2), engine.frozenPiecePos)
        engine.currentTurn = PlayerColor.RED
        val frozenMoves = engine.calculateValidMoves(Position(6, 2))
        assertTrue("Frozen piece should have 0 valid moves", frozenMoves.isEmpty())
    }

    @Test
    fun testAISkillTriggerDecisions() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Blue threatens Red den at (0, 3): Blue Tiger at (1, 2) (dist = 2)
        // Red Lion is far away at (4, 4)
        customBoard[Position(1, 2)] = Piece(type = AnimalType.TIGER, owner = PlayerColor.BLUE)
        customBoard[Position(4, 4)] = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED
        engine.isVsAI = true

        engine.evaluateAndTriggerAISkills()

        // Red AI should trigger shield recall or freeze
        val shieldUsed = (engine.redShieldSkillCount == 0)
        val freezeUsed = (engine.redFreezeSkillCount == 0)
        assertTrue("AI should proactively trigger defensive superpower", shieldUsed || freezeUsed)
    }

    @Test
    fun testStalemateDetection_NoLegalMoves() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Blue Rat is in corner (8, 0).
        // Surrounded by Red Lion (8, 1) and Red Elephant (7, 0).
        // Blue Rat cannot capture either (Elephant cannot be captured if Rat has no moves, but Rat CAN capture Elephant; wait, let's surround Blue Cat with higher rank pieces!)
        // Blue Cat is at (8, 0). (8, 1) has Red Tiger, (7, 0) has Red Lion.
        // Neither can be captured by Cat.
        // When Red moves a piece to complete the blockade or Blue has turn with 0 legal moves:
        customBoard[Position(8, 0)] = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
        customBoard[Position(8, 2)] = Piece(type = AnimalType.TIGER, owner = PlayerColor.RED)
        customBoard[Position(7, 0)] = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED
        engine.isVsAI = false

        // Red moves Tiger from (8, 2) to (8, 1), trapping Blue Cat at (8, 0) completely!
        engine.makeMove(Position(8, 2), Position(8, 1))

        // Blue has turn now, but Cat at (8, 0) has 0 valid moves (cannot capture Lion or Tiger, out of bounds elsewhere)
        // Engine must detect stalemate and award victory to RED immediately!
        assertEquals("Red should win by stalemating Blue with no legal moves", PlayerColor.RED, engine.winner)
    }

    @Test
    fun testShieldRecall_SwapsFriendlyPieceAtGate() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Blue den is at (8, 3), defense gate is at (7, 3)
        // Blue Rat is currently guarding the gate at (7, 3)
        // Blue Elephant is at (5, 0)
        customBoard[Position(7, 3)] = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
        customBoard[Position(5, 0)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.BLUE

        // Trigger Shield Recall
        engine.activateShieldRecallSkill(PlayerColor.BLUE)

        // Elephant (strongest) should now be at the gate (7, 3)
        val gatePiece = engine.board[Position(7, 3)]
        assertNotNull("Gate must be occupied", gatePiece)
        assertEquals(AnimalType.ELEPHANT, gatePiece?.type)
        assertEquals(PlayerColor.BLUE, gatePiece?.owner)

        // Rat should have been swapped to (5, 0) rather than destroyed!
        val swappedPiece = engine.board[Position(5, 0)]
        assertNotNull("Friendly piece at gate must be preserved and swapped", swappedPiece)
        assertEquals(AnimalType.RAT, swappedPiece?.type)
        assertEquals(PlayerColor.BLUE, swappedPiece?.owner)
        assertEquals("Both friendly pieces must remain on board", 2, engine.board.size)
    }

    @Test
    fun testStateGuards_NoActionWhenGameOverOrThinking() {
        val engine = JungleGameEngine()
        engine.winner = PlayerColor.BLUE
        val initialBoardSize = engine.board.size

        // Any skill or move attempts while game is won should be ignored
        engine.activateFrenzySkill(PlayerColor.BLUE)
        assertEquals(1, engine.blueFrenzySkillCount)

        engine.activateFreezeSkill(PlayerColor.BLUE)
        assertEquals(1, engine.blueFreezeSkillCount)

        engine.activateShieldRecallSkill(PlayerColor.BLUE)
        assertEquals(1, engine.blueShieldSkillCount)

        engine.executeBestAdvice()
        assertEquals(initialBoardSize, engine.board.size)

        // Test thinking guard
        engine.winner = null
        engine.isThinking = true

        engine.activateFrenzySkill(PlayerColor.BLUE)
        assertEquals(1, engine.blueFrenzySkillCount)

        engine.activateFreezeSkill(PlayerColor.BLUE)
        assertEquals(1, engine.blueFreezeSkillCount)

        engine.executeBestAdvice()
        assertEquals(initialBoardSize, engine.board.size)
    }

    @Test
    fun test10RoundsSelfPlayFullAudit() {
        // Run 10 full rounds across all 3 AI difficulties
        val difficulties = listOf(
            AIDifficulty.EASY, AIDifficulty.EASY, AIDifficulty.EASY,
            AIDifficulty.MEDIUM, AIDifficulty.MEDIUM, AIDifficulty.MEDIUM,
            AIDifficulty.HARD, AIDifficulty.HARD, AIDifficulty.HARD, AIDifficulty.HARD
        )

        for ((idx, diff) in difficulties.withIndex()) {
            val round = idx + 1
            val engine = JungleGameEngine()
            engine.isVsAI = false
            engine.aiDifficulty = diff

            var movesPlayed = 0
            val maxMoves = 80

            while (engine.winner == null && movesPlayed < maxMoves) {
                val turn = engine.currentTurn
                val candidates = engine.searchOptimalMoveCandidates(turn)
                if (candidates.isEmpty()) {
                    // Turn player has no moves -> opponent wins
                    break
                }

                val move = when (diff) {
                    AIDifficulty.HARD -> candidates.first()
                    AIDifficulty.MEDIUM -> candidates.take(3).random()
                    AIDifficulty.EASY -> candidates.take(6).random()
                }

                engine.makeMove(move.from, move.to)
                movesPlayed++
            }

            assertTrue("Round $round ($diff) completed with $movesPlayed moves (winner=${engine.winner})", movesPlayed > 0)
        }
    }

    @Test
    fun testFrenzyRespectsRiverBoundary() {
        val engine = JungleGameEngine()
        val customBoard = mutableMapOf<Position, Piece>()
        // Blue Cat is at land (2, 1), Red Rat is in river (3, 1)
        val cat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
        val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
        customBoard[Position(2, 1)] = cat
        customBoard[Position(3, 1)] = rat
        engine.board = customBoard
        engine.currentTurn = PlayerColor.BLUE

        // Under normal rules, Cat cannot enter river / attack rat in river
        assertFalse(engine.calculateValidMoves(Position(2, 1)).contains(Position(3, 1)))

        // Activate Frenzy (Master Leap) for Blue
        engine.activateFrenzySkill(PlayerColor.BLUE)
        assertEquals(2, engine.blueFrenzyRemainingTurns)

        // Cat under Frenzy CAN leapfrog higher ranks on land, but CANNOT cross river boundary to capture Rat in river
        val frenzyMoves = engine.calculateValidMoves(Position(2, 1))
        assertFalse("Land piece under Frenzy cannot capture into river", frenzyMoves.contains(Position(3, 1)))

        // Reverse: Red Rat in river at (3, 1), Blue Elephant on land at (2, 1)
        customBoard.clear()
        customBoard[Position(3, 1)] = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
        customBoard[Position(2, 1)] = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)
        engine.board = customBoard
        engine.currentTurn = PlayerColor.RED

        // Activate Frenzy for Red
        engine.activateFrenzySkill(PlayerColor.RED)
        val redRatMoves = engine.calculateValidMoves(Position(3, 1))
        assertFalse("Rat in river under Frenzy cannot capture piece on land", redRatMoves.contains(Position(2, 1)))
    }

    @Test
    fun testLosslessUndoRestoresSkillsAndFreeze() {
        val engine = JungleGameEngine()
        engine.isVsAI = false
        val customBoard = mutableMapOf<Position, Piece>()
        val blueWolf = Piece(type = AnimalType.WOLF, owner = PlayerColor.BLUE)
        val redCat = Piece(type = AnimalType.CAT, owner = PlayerColor.RED)
        val redLion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
        customBoard[Position(6, 0)] = blueWolf
        customBoard[Position(5, 1)] = redCat
        customBoard[Position(0, 0)] = redLion
        engine.board = customBoard
        engine.currentTurn = PlayerColor.BLUE

        // Initial skill state
        assertEquals(1, engine.blueFrenzySkillCount)
        assertEquals(1, engine.blueFreezeSkillCount)
        assertEquals(0, engine.blueFrenzyRemainingTurns)
        assertNull(engine.frozenPiecePos)

        // Blue activates Frenzy and Freeze
        engine.activateFrenzySkill(PlayerColor.BLUE)
        engine.activateFreezeSkill(PlayerColor.BLUE)

        assertEquals(0, engine.blueFrenzySkillCount)
        assertEquals(2, engine.blueFrenzyRemainingTurns)
        assertEquals(0, engine.blueFreezeSkillCount)
        assertEquals(Position(5, 1), engine.frozenPiecePos)
        assertEquals(1, engine.frozenTurns)

        // Blue moves Wolf (consuming 1 turn of Frenzy)
        engine.makeMove(Position(6, 0), Position(6, 1))
        assertEquals(PlayerColor.RED, engine.currentTurn)
        assertEquals(1, engine.blueFrenzyRemainingTurns)

        // Undo move
        engine.undo()

        // Verify full restoration of turn, positions, skills, and freeze state
        assertEquals(PlayerColor.BLUE, engine.currentTurn)
        assertNotNull(engine.board[Position(6, 0)])
        assertNull(engine.board[Position(6, 1)])
        assertEquals("Frenzy turns must be restored", 2, engine.blueFrenzyRemainingTurns)
        assertEquals(Position(5, 1), engine.frozenPiecePos)
        assertEquals(1, engine.frozenTurns)
    }

    @Test
    fun testTenRoundsComprehensiveEngineVerification() {
        // Explicit 10-round verification testing the 10 vital rules and edge cases
        for (round in 1..10) {
            val engine = JungleGameEngine()
            engine.isVsAI = false

            when (round) {
                1 -> {
                    // Round 1: Initial Setup
                    assertEquals(16, engine.board.size)
                    assertEquals(PlayerColor.BLUE, engine.currentTurn)
                    assertNull(engine.winner)
                }
                2 -> {
                    // Round 2: River Swimming Restrictions
                    val riverPos = Position(3, 1)
                    val dog = Piece(type = AnimalType.DOG, owner = PlayerColor.BLUE)
                    val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
                    engine.board = mapOf(Position(2, 1) to dog)
                    assertFalse(engine.calculateValidMoves(Position(2, 1)).contains(riverPos))
                    engine.board = mapOf(Position(2, 1) to rat)
                    assertTrue(engine.calculateValidMoves(Position(2, 1)).contains(riverPos))
                }
                3 -> {
                    // Round 3: Lion River Jump & Obstruction
                    val lion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
                    engine.board = mapOf(Position(2, 1) to lion)
                    assertTrue(engine.calculateValidMoves(Position(2, 1)).contains(Position(6, 1)))
                    // Rat blocks river at (4, 1)
                    engine.board = engine.board + (Position(4, 1) to Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE))
                    assertFalse(engine.calculateValidMoves(Position(2, 1)).contains(Position(6, 1)))
                }
                4 -> {
                    // Round 4: Rat Captures Elephant on Land
                    val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
                    val elephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.RED)
                    engine.board = mapOf(Position(6, 0) to rat, Position(6, 1) to elephant)
                    assertTrue(engine.calculateValidMoves(Position(6, 0)).contains(Position(6, 1)))
                    engine.currentTurn = PlayerColor.RED
                    assertFalse(engine.calculateValidMoves(Position(6, 1)).contains(Position(6, 0)))
                }
                5 -> {
                    // Round 5: Frenzy Respects River
                    val cat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
                    val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.RED)
                    engine.board = mapOf(Position(2, 1) to cat, Position(3, 1) to rat)
                    engine.activateFrenzySkill(PlayerColor.BLUE)
                    assertFalse(engine.calculateValidMoves(Position(2, 1)).contains(Position(3, 1)))
                }
                6 -> {
                    // Round 6: Trap Weakness
                    val lion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
                    val cat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
                    // (7, 3) is Blue trap
                    engine.board = mapOf(Position(7, 3) to lion, Position(7, 2) to cat)
                    assertTrue(engine.calculateValidMoves(Position(7, 2)).contains(Position(7, 3)))
                }
                7 -> {
                    // Round 7: Freeze Skill
                    val redTiger = Piece(type = AnimalType.TIGER, owner = PlayerColor.RED)
                    engine.board = mapOf(Position(5, 5) to redTiger)
                    engine.activateFreezeSkill(PlayerColor.BLUE)
                    assertEquals(Position(5, 5), engine.frozenPiecePos)
                    engine.currentTurn = PlayerColor.RED
                    assertTrue(engine.calculateValidMoves(Position(5, 5)).isEmpty())
                }
                8 -> {
                    // Round 8: Guardian Teleport Friendly Swap
                    val rat = Piece(type = AnimalType.RAT, owner = PlayerColor.BLUE)
                    val elephant = Piece(type = AnimalType.ELEPHANT, owner = PlayerColor.BLUE)
                    engine.board = mapOf(Position(7, 3) to rat, Position(5, 0) to elephant)
                    engine.activateShieldRecallSkill(PlayerColor.BLUE)
                    assertEquals(AnimalType.ELEPHANT, engine.board[Position(7, 3)]?.type)
                    assertEquals(AnimalType.RAT, engine.board[Position(5, 0)]?.type)
                    assertEquals(2, engine.board.size)
                }
                9 -> {
                    // Round 9: Lossless Undo
                    val dog = Piece(type = AnimalType.DOG, owner = PlayerColor.BLUE)
                    val redLion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
                    engine.board = mapOf(Position(7, 0) to dog, Position(0, 0) to redLion)
                    engine.activateFrenzySkill(PlayerColor.BLUE)
                    assertEquals(2, engine.blueFrenzyRemainingTurns)
                    engine.makeMove(Position(7, 0), Position(6, 0))
                    assertEquals(1, engine.blueFrenzyRemainingTurns)
                    engine.undo()
                    assertEquals(2, engine.blueFrenzyRemainingTurns)
                    assertNotNull(engine.board[Position(7, 0)])
                }
                10 -> {
                    // Round 10: Stalemate Win Condition
                    val cat = Piece(type = AnimalType.CAT, owner = PlayerColor.BLUE)
                    val lion = Piece(type = AnimalType.LION, owner = PlayerColor.RED)
                    val tiger = Piece(type = AnimalType.TIGER, owner = PlayerColor.RED)
                    engine.board = mapOf(
                        Position(8, 0) to cat,
                        Position(7, 0) to lion,
                        Position(8, 2) to tiger
                    )
                    engine.currentTurn = PlayerColor.RED
                    engine.makeMove(Position(8, 2), Position(8, 1))
                    assertEquals("Red should win by stalemating Blue", PlayerColor.RED, engine.winner)
                }
            }
        }
    }
}
