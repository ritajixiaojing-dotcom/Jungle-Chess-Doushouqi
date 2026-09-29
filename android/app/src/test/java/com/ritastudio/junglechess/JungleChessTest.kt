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
}
