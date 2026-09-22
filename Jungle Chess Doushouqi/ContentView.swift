import SwiftUI
import Combine
import AudioToolbox

// MARK: - 1. 基础数据模型

enum Player: String, Codable {
    case red = "红方"
    case blue = "蓝方"
    
    var color: Color {
        self == .red ? Color(red: 0.92, green: 0.25, blue: 0.2) : Color(red: 0.18, green: 0.52, blue: 0.95)
    }
    
    var opponent: Player {
        self == .red ? .blue : .red
    }
}

enum AIDifficulty: String, CaseIterable, Identifiable {
    case easy = "🌱 轻松入门"
    case normal = "🌿 棋艺进阶"
    case hard = "👑 智谋大师"
    
    var id: String { rawValue }
    
    var shortName: String {
        switch self {
        case .easy: return "🌱 入门"
        case .normal: return "🌿 进阶"
        case .hard: return "👑 大师"
        }
    }
}

enum StrategicDoctrine: String, CaseIterable {
    case lionBlitz = "🦁 狮王飞河突击"
    case elephantTank = "🐘 象王稳步推进"
    case ratStealth = "🐭 灵鼠潜河入穴"
    case packHunt = "🐺 狼豹多路包抄"
}

enum AnimalType: Int, CaseIterable, Identifiable {
    case rat = 1      // 鼠
    case cat = 2      // 猫
    case dog = 3      // 狗
    case wolf = 4     // 狼
    case leopard = 5  // 豹
    case tiger = 6    // 虎
    case lion = 7     // 狮
    case elephant = 8 // 象
    
    var id: Int { rawValue }
    
    var name: String {
        return JungleLanguageManager.shared.animalName(self)
    }
    
    var emoji: String {
        switch self {
        case .rat: return "🐭"
        case .cat: return "🐱"
        case .dog: return "🐶"
        case .wolf: return "🐺"
        case .leopard: return "🐆"
        case .tiger: return "🐯"
        case .lion: return "🦁"
        case .elephant: return "🐘"
        }
    }
    
    var pieceValue: Int {
        switch self {
        case .rat: return 320
        case .cat: return 200
        case .dog: return 280
        case .wolf: return 380
        case .leopard: return 500
        case .tiger: return 720
        case .lion: return 850
        case .elephant: return 950
        }
    }
    
    var ruleCanEatDescription: String {
        return JungleLanguageManager.shared.animalEatRule(self)
    }
}

struct Piece: Identifiable, Equatable {
    let id = UUID()
    let type: AnimalType
    let owner: Player
}

struct Position: Hashable, Equatable {
    let row: Int
    let col: Int
}

enum TileType: Equatable {
    case land
    case river
    case trap(owner: Player)
    case den(owner: Player)
}

struct MoveRecord: Equatable {
    let from: Position
    let to: Position
    let movedPiece: Piece
    let capturedPiece: Piece?
}

// 战术最优解模型
struct OptimalMoveAdvice: Equatable {
    let from: Position
    let to: Position
    let piece: Piece
    let headline: String
    let detailReason: String
    let forecast3Steps: String
    let badgeText: String
    let isEmergency: Bool
    let isWin: Bool
    let isTrade: Bool
}

// MARK: - 2. 核心智能游戏引擎 (全员受威胁紧急逃生/入河避险优先体系)

@MainActor
class JungleGameEngine: ObservableObject {
    @Published var board: [Position: Piece] = [:]
    @Published var currentTurn: Player = .blue
    @Published var selectedPos: Position? = nil
    @Published var validMoves: [Position] = []
    @Published var dangerousMoves: Set<Position> = []
    @Published var threatenedPieces: Set<Position> = []
    @Published var winner: Player? = nil
    
    @Published var optimalAdvice: OptimalMoveAdvice? = nil
    @Published var winAdvantagePercent: Int = 50
    @Published var currentDoctrine: StrategicDoctrine = .lionBlitz
    
    @Published var frenzyRemainingTurns: Int = 0
    @Published var shieldSkillCount: Int = 1
    @Published var frenzySkillCount: Int = 1
    @Published var skillBannerText: String? = nil
    
    @Published var inspectPiece: Piece? = nil
    @Published var isVsAI: Bool = true
    @Published var aiDifficulty: AIDifficulty = .hard
    @Published var isThinking: Bool = false
    @Published var history: [MoveRecord] = []
    @Published var lastOpponentMove: MoveRecord? = nil
    
    init() {
        resetGame()
        if CommandLine.arguments.contains("-mockOpponentMove") {
            setupMockOpponentMove()
        } else if CommandLine.arguments.contains("-mockMidGame") {
            setupMockMidGame()
        }
    }
    
    func setupMockOpponentMove() {
        let from = Position(row: 2, col: 2)
        let to = Position(row: 3, col: 2)
        let piece = Piece(type: .leopard, owner: .red)
        board.removeValue(forKey: from)
        board[to] = piece
        lastOpponentMove = MoveRecord(from: from, to: to, movedPiece: piece, capturedPiece: nil)
        currentTurn = .blue
        updateRoundEvaluation()
    }
    
    func setupMockMidGame() {
        board.removeAll()
        board[Position(row: 0, col: 0)] = Piece(type: .lion, owner: .red)
        board[Position(row: 0, col: 6)] = Piece(type: .tiger, owner: .red)
        board[Position(row: 1, col: 1)] = Piece(type: .dog, owner: .red)
        board[Position(row: 3, col: 3)] = Piece(type: .wolf, owner: .red)
        board[Position(row: 2, col: 6)] = Piece(type: .elephant, owner: .red)
        board[Position(row: 3, col: 1)] = Piece(type: .rat, owner: .red)
        
        board[Position(row: 4, col: 2)] = Piece(type: .leopard, owner: .blue)
        board[Position(row: 5, col: 4)] = Piece(type: .rat, owner: .blue)
        board[Position(row: 6, col: 0)] = Piece(type: .elephant, owner: .blue)
        board[Position(row: 5, col: 6)] = Piece(type: .lion, owner: .blue)
        board[Position(row: 7, col: 1)] = Piece(type: .cat, owner: .blue)
        board[Position(row: 7, col: 5)] = Piece(type: .dog, owner: .blue)
        board[Position(row: 8, col: 0)] = Piece(type: .tiger, owner: .blue)
        
        currentTurn = .blue
        selectedPos = Position(row: 4, col: 2)
        validMoves = [Position(row: 3, col: 2), Position(row: 4, col: 3), Position(row: 5, col: 2)]
        dangerousMoves = [Position(row: 3, col: 3)]
        updateRoundEvaluation()
    }
    
    func getTileType(at pos: Position) -> TileType {
        let r = pos.row
        let c = pos.col
        
        if r == 0 && c == 3 { return .den(owner: .red) }
        if r == 8 && c == 3 { return .den(owner: .blue) }
        
        if (r == 0 && (c == 2 || c == 4)) || (r == 1 && c == 3) {
            return .trap(owner: .red)
        }
        if (r == 8 && (c == 2 || c == 4)) || (r == 7 && c == 3) {
            return .trap(owner: .blue)
        }
        
        if (3...5).contains(r) && (c == 1 || c == 2 || c == 4 || c == 5) {
            return .river
        }
        
        return .land
    }
    
    func resetGame() {
        board.removeAll()
        history.removeAll()
        lastOpponentMove = nil
        selectedPos = nil
        validMoves = []
        dangerousMoves = []
        threatenedPieces = []
        winner = nil
        currentTurn = .blue
        
        frenzyRemainingTurns = 0
        shieldSkillCount = 1
        frenzySkillCount = 1
        skillBannerText = nil
        
        currentDoctrine = StrategicDoctrine.allCases.randomElement() ?? .lionBlitz
        
        // 红方初始
        board[Position(row: 0, col: 0)] = Piece(type: .lion, owner: .red)
        board[Position(row: 0, col: 6)] = Piece(type: .tiger, owner: .red)
        board[Position(row: 1, col: 1)] = Piece(type: .dog, owner: .red)
        board[Position(row: 1, col: 5)] = Piece(type: .cat, owner: .red)
        board[Position(row: 2, col: 0)] = Piece(type: .rat, owner: .red)
        board[Position(row: 2, col: 2)] = Piece(type: .leopard, owner: .red)
        board[Position(row: 2, col: 4)] = Piece(type: .wolf, owner: .red)
        board[Position(row: 2, col: 6)] = Piece(type: .elephant, owner: .red)
        
        // 蓝方初始
        board[Position(row: 8, col: 6)] = Piece(type: .lion, owner: .blue)
        board[Position(row: 8, col: 0)] = Piece(type: .tiger, owner: .blue)
        board[Position(row: 7, col: 5)] = Piece(type: .dog, owner: .blue)
        board[Position(row: 7, col: 1)] = Piece(type: .cat, owner: .blue)
        board[Position(row: 6, col: 6)] = Piece(type: .rat, owner: .blue)
        board[Position(row: 6, col: 4)] = Piece(type: .leopard, owner: .blue)
        board[Position(row: 6, col: 2)] = Piece(type: .wolf, owner: .blue)
        board[Position(row: 6, col: 0)] = Piece(type: .elephant, owner: .blue)
        
        updateRoundEvaluation()
    }
    
    func activateFrenzySkill() {
        guard frenzySkillCount > 0, currentTurn == .blue else { return }
        frenzySkillCount -= 1
        frenzyRemainingTurns = 2
        skillBannerText = JungleLanguageManager.shared.frenzyBannerText
        playSound(type: .win)
        updateRoundEvaluation()
    }
    
    func activateShieldRecallSkill() {
        guard shieldSkillCount > 0, currentTurn == .blue else { return }
        shieldSkillCount -= 1
        
        var bestPiecePos: Position? = nil
        var bestPiece: Piece? = nil
        let targetGate = Position(row: 7, col: 3)
        
        for (pos, p) in board where p.owner == .blue {
            if pos != targetGate && (bestPiece == nil || p.type.rawValue > bestPiece!.type.rawValue) {
                bestPiecePos = pos
                bestPiece = p
            }
        }
        
        if let fromPos = bestPiecePos, let piece = bestPiece {
            board.removeValue(forKey: fromPos)
            board[targetGate] = piece
            skillBannerText = JungleLanguageManager.shared.shieldBannerText(pieceName: piece.type.name)
            playSound(type: .win)
            updateRoundEvaluation()
        }
    }
    
    func handleTileTap(at pos: Position) {
        guard winner == nil && !isThinking else { return }
        if isVsAI && currentTurn == .red { return }
        
        let clickedPiece = board[pos]
        
        if let selected = selectedPos, validMoves.contains(pos) {
            makeMove(from: selected, to: pos)
            return
        }
        
        if let piece = clickedPiece, piece.owner == currentTurn {
            selectedPos = pos
            inspectPiece = piece
            validMoves = calculateValidMoves(from: pos)
            dangerousMoves = calculateDangerousMoves(from: pos, moves: validMoves)
            playSound(type: .select)
            return
        }
        
        if let piece = clickedPiece {
            inspectPiece = piece
            selectedPos = nil
            validMoves = []
            dangerousMoves = []
            return
        }
        
        selectedPos = nil
        validMoves = []
        dangerousMoves = []
    }
    
    func makeMove(from: Position, to: Position) {
        guard let movingPiece = board[from] else { return }
        let capturedPiece = board[to]
        
        let record = MoveRecord(from: from, to: to, movedPiece: movingPiece, capturedPiece: capturedPiece)
        history.append(record)
        lastOpponentMove = record
        
        board.removeValue(forKey: from)
        board[to] = movingPiece
        
        if capturedPiece != nil {
            playSound(type: .capture)
        } else {
            playSound(type: .move)
        }
        
        selectedPos = nil
        validMoves = []
        dangerousMoves = []
        threatenedPieces = []
        optimalAdvice = nil
        
        if movingPiece.owner == .blue && frenzyRemainingTurns > 0 {
            frenzyRemainingTurns -= 1
            if frenzyRemainingTurns == 0 {
                skillBannerText = nil
            }
        }
        
        if checkWin(afterMoveTo: to, by: movingPiece.owner) {
            winner = movingPiece.owner
            playSound(type: .win)
            return
        }
        
        currentTurn = currentTurn.opponent
        updateRoundEvaluation()
        
        if isVsAI && currentTurn == .red {
            isThinking = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                self.triggerAIMove()
            }
        }
    }
    
    func undo() {
        guard !history.isEmpty, winner == nil else { return }
        let stepsToUndo = (isVsAI && history.count >= 2) ? 2 : 1
        
        for _ in 0..<stepsToUndo {
            guard let last = history.popLast() else { break }
            board[last.from] = last.movedPiece
            if let captured = last.capturedPiece {
                board[last.to] = captured
            } else {
                board.removeValue(forKey: last.to)
            }
            currentTurn = last.movedPiece.owner
        }
        
        selectedPos = nil
        validMoves = []
        dangerousMoves = []
        threatenedPieces = []
        lastOpponentMove = history.last(where: { $0.movedPiece.owner != currentTurn })
        updateRoundEvaluation()
        playSound(type: .undo)
    }
    
    func executeBestAdvice() {
        guard currentTurn == .blue || !isVsAI else { return }
        if let advice = optimalAdvice {
            makeMove(from: advice.from, to: advice.to)
        } else {
            computePlayerOptimalMove()
            if let advice = optimalAdvice {
                makeMove(from: advice.from, to: advice.to)
            }
        }
    }
    
    func autoSelectRecommendedPiece() {
        guard currentTurn == .blue || !isVsAI else { return }
        if let advice = optimalAdvice {
            selectedPos = advice.from
            inspectPiece = advice.piece
            validMoves = calculateValidMoves(from: advice.from)
            dangerousMoves = calculateDangerousMoves(from: advice.from, moves: validMoves)
        }
    }
    
    func updateRoundEvaluation() {
        guard winner == nil else { return }
        
        let playerTarget: Player = isVsAI ? .blue : currentTurn
        scanThreatenedPieces(for: playerTarget)
        
        var bluePower = 0
        var redPower = 0
        var blueDistToDen = 99
        var redDistToDen = 99
        for (pos, piece) in board {
            let power = piece.type.pieceValue
            if piece.owner == .blue {
                bluePower += power
                let dist = abs(pos.row - 0) + abs(pos.col - 3)
                blueDistToDen = min(blueDistToDen, dist)
            } else {
                redPower += power
                let dist = abs(pos.row - 8) + abs(pos.col - 3)
                redDistToDen = min(redDistToDen, dist)
            }
        }
        let total = max(1, bluePower + redPower)
        var percent = Int((Double(bluePower) / Double(total)) * 100.0)
        if blueDistToDen <= 3 { percent = max(percent, 85) }
        if blueDistToDen <= 1 { percent = 98 }
        if redDistToDen <= 2 { percent = min(percent, 25) }
        if redDistToDen <= 1 { percent = min(percent, 10) }
        winAdvantagePercent = max(5, min(99, percent))
        
        computePlayerOptimalMove()
        
        if let advice = optimalAdvice {
            inspectPiece = advice.piece
        }
    }
    
    private func scanThreatenedPieces(for player: Player) {
        var threatened = Set<Position>()
        let opponent = player.opponent
        for (oppPos, oppPiece) in board where oppPiece.owner == opponent {
            let oppMoves = calculateValidMoves(from: oppPos)
            for targetPos in oppMoves {
                if let targetPiece = board[targetPos], targetPiece.owner == player {
                    threatened.insert(targetPos)
                }
            }
        }
        self.threatenedPieces = threatened
    }
    
    // 检查某个位置的棋子是否正在被敌人直接威胁
    private func isPositionThreatened(pos: Position, piece: Piece, boardState: [Position: Piece]) -> Bool {
        let opponent = piece.owner.opponent
        for (oppPos, oppPiece) in boardState where oppPiece.owner == opponent {
            let oppMoves = calculateValidMovesOnBoard(boardState: boardState, from: oppPos)
            if oppMoves.contains(pos) {
                return true
            }
        }
        return false
    }
    
    // 检查重复往复循环步数 (禁长捉/破死循环)
    private func calculateRepetitionPenalty(from: Position, to: Position, piece: Piece) -> Int {
        var penalty = 0
        let recentMoves = history.suffix(12).filter { $0.movedPiece.owner == piece.owner }
        
        var backAndForthCount = 0
        var visitedCount = 0
        
        for past in recentMoves {
            if past.from == to && past.to == from && past.movedPiece.type == piece.type {
                backAndForthCount += 1
            }
            if past.to == to && past.movedPiece.type == piece.type {
                visitedCount += 1
            }
        }
        
        if backAndForthCount >= 1 {
            penalty += 150000 * backAndForthCount
        }
        if visitedCount >= 2 {
            penalty += 90000 * (visitedCount - 1)
        }
        return penalty
    }
    
    // MARK: - 🧠 棋局估值函数
    func evaluateBoardScore(customBoard: [Position: Piece], for player: Player) -> Int {
        let blueDen = Position(row: 8, col: 3)
        let redDen = Position(row: 0, col: 3)
        
        if customBoard[redDen]?.owner == .blue {
            return (player == .blue ? 10_000_000 : -10_000_000)
        }
        if customBoard[blueDen]?.owner == .red {
            return (player == .red ? 10_000_000 : -10_000_000)
        }
        
        let myDen = (player == .red ? redDen : blueDen)
        let enemyDen = (player == .red ? blueDen : redDen)
        let myTraps: Set<Position> = (player == .red ?
            [Position(row: 0, col: 2), Position(row: 0, col: 4), Position(row: 1, col: 3)] :
            [Position(row: 8, col: 2), Position(row: 8, col: 4), Position(row: 7, col: 3)])
        let enemyTraps: Set<Position> = (player == .red ?
            [Position(row: 8, col: 2), Position(row: 8, col: 4), Position(row: 7, col: 3)] :
            [Position(row: 0, col: 2), Position(row: 0, col: 4), Position(row: 1, col: 3)])
        
        var score = 0
        
        func pieceBaseValue(_ type: AnimalType) -> Int {
            switch type {
            case .rat: return 3500
            case .cat: return 2000
            case .dog: return 3000
            case .wolf: return 4000
            case .leopard: return 5500
            case .tiger: return 8000
            case .lion: return 9500
            case .elephant: return 10000
            }
        }
        
        var myElephantPos: Position? = nil
        var oppRatPos: Position? = nil
        var oppElephantPos: Position? = nil
        var myRatPos: Position? = nil
        
        for (pos, piece) in customBoard {
            if piece.owner == player {
                if piece.type == .elephant { myElephantPos = pos }
                if piece.type == .rat { myRatPos = pos }
            } else {
                if piece.type == .elephant { oppElephantPos = pos }
                if piece.type == .rat { oppRatPos = pos }
            }
        }
        
        for (pos, piece) in customBoard {
            let isMine = (piece.owner == player)
            let baseVal = pieceBaseValue(piece.type)
            var positionalBonus = 0
            
            let targetDen = isMine ? enemyDen : myDen
            let distToTarget = abs(pos.row - targetDen.row) + abs(pos.col - targetDen.col)
            
            if isMine {
                // A. 进攻推进动力：全盘所有格子，越靠近敌营分数越高！
                positionalBonus += (12 - distToTarget) * 200
                
                // B. 兵临城下绝杀冲刺
                if distToTarget == 1 {
                    positionalBonus += 30000
                } else if distToTarget == 2 {
                    positionalBonus += 12000
                } else if distToTarget <= 4 {
                    positionalBonus += (5 - distToTarget) * 3000
                }
                
                // C. 关键地形控制
                if pos.col == 3 {
                    positionalBonus += 400
                }
                if (piece.type == .lion || piece.type == .tiger) && (pos.row == 2 || pos.row == 6) {
                    positionalBonus += 1500
                }
                if piece.type == .rat && getTileType(at: pos) == .river {
                    positionalBonus += 1800
                }
                if enemyTraps.contains(pos) {
                    positionalBonus -= 8000
                }
                
                score += (baseVal + positionalBonus)
            } else {
                // 敌方棋子
                positionalBonus += (12 - distToTarget) * 200
                
                if distToTarget == 1 {
                    positionalBonus += 35000
                } else if distToTarget == 2 {
                    positionalBonus += 15000
                } else if distToTarget <= 4 {
                    positionalBonus += (5 - distToTarget) * 3500
                }
                if myTraps.contains(pos) {
                    positionalBonus -= 7000
                }
                
                score -= (baseVal + positionalBonus)
            }
        }
        
        // D. 象鼠克制动态加权
        if let ePos = myElephantPos, let rPos = oppRatPos {
            let dist = abs(ePos.row - rPos.row) + abs(ePos.col - rPos.col)
            if getTileType(at: rPos) != .river {
                if dist == 1 { score -= 15000 }
                else if dist == 2 { score -= 6000 }
            }
        }
        if let rPos = myRatPos, let ePos = oppElephantPos {
            let dist = abs(rPos.row - ePos.row) + abs(rPos.col - ePos.col)
            if getTileType(at: rPos) != .river {
                if dist == 1 { score += 15000 }
                else if dist == 2 { score += 6000 }
            }
        }
        
        return score
    }
    
    func isSquareCoveredByFriendly(boardState: [Position: Piece], pos: Position, owner: Player) -> Bool {
        for (friendPos, friendPiece) in boardState where friendPiece.owner == owner && friendPos != pos {
            let moves = calculateValidMovesOnBoard(boardState: boardState, from: friendPos)
            if moves.contains(pos) {
                return true
            }
        }
        return false
    }
    
    // 🎯 玩家最优解决策 (全面覆盖：大本营保卫 + 己方受威胁紧急逃生/入河避险 + 水中清障 + 战术进攻)
    func computePlayerOptimalMove() {
        let playerSide: Player = isVsAI ? .blue : currentTurn
        let enemyDen = (playerSide == .blue ? Position(row: 0, col: 3) : Position(row: 8, col: 3))
        let myDen = (playerSide == .blue ? Position(row: 8, col: 3) : Position(row: 0, col: 3))
        let myChokePoints: Set<Position> = (playerSide == .blue ?
            [Position(row: 8, col: 2), Position(row: 8, col: 4), Position(row: 7, col: 3), Position(row: 7, col: 2), Position(row: 7, col: 4)] :
            [Position(row: 0, col: 2), Position(row: 0, col: 4), Position(row: 1, col: 3), Position(row: 1, col: 2), Position(row: 1, col: 4)])
        
        // 🚨 1. 大本营受威胁检测
        var urgentHomeInvaders: [(pos: Position, piece: Piece, dist: Int)] = []
        for (rPos, rPiece) in board where rPiece.owner != playerSide {
            let dist = abs(rPos.row - myDen.row) + abs(rPos.col - myDen.col)
            if dist <= 3 {
                urgentHomeInvaders.append((rPos, rPiece, dist))
            }
        }
        let isHomeUnderSiege = !urgentHomeInvaders.isEmpty
        let closestInvader = urgentHomeInvaders.min(by: { $0.dist < $1.dist })
        
        // 🚨 2. 我方大象受敌鼠威胁检测
        var myElephantPos: Position? = nil
        var oppRatPos: Position? = nil
        for (pos, piece) in board {
            if piece.owner == playerSide && piece.type == .elephant { myElephantPos = pos }
            if piece.owner != playerSide && piece.type == .rat { oppRatPos = pos }
        }
        var ratDistToMyElephant = 99
        if let ePos = myElephantPos, let rPos = oppRatPos {
            ratDistToMyElephant = abs(ePos.row - rPos.row) + abs(ePos.col - rPos.col)
        }
        
        // 🌊 3. 水中敌鼠封锁检测
        let isOppRatInRiver: Bool = {
            if let rPos = oppRatPos {
                return getTileType(at: rPos) == .river
            }
            return false
        }()
        
        var evaluatedAdvices: [(advice: OptimalMoveAdvice, score: Int)] = []
        
        for (from, piece) in board where piece.owner == playerSide {
            let moves = calculateValidMoves(from: from)
            let inEnemyTerritory = (playerSide == .blue ? from.row <= 3 : from.row >= 5)
            let isCurrentPieceUnderThreat = isPositionThreatened(pos: from, piece: piece, boardState: board)
            
            for to in moves {
                // 1. 模拟 Step 1：我方走棋
                var simBoard1 = board
                let captured1 = simBoard1[to]
                simBoard1.removeValue(forKey: from)
                simBoard1[to] = piece
                
                // 🏆 一步直接占穴获胜
                if to == enemyDen {
                    let winAdv = OptimalMoveAdvice(
                        from: from, to: to, piece: piece,
                        headline: JungleLanguageManager.shared.adviceWinHeadline,
                        detailReason: JungleLanguageManager.shared.adviceWinDetail,
                        forecast3Steps: JungleLanguageManager.shared.adviceWinForecast(piece: piece.type.name, winner: JungleLanguageManager.shared.playerName(playerSide)),
                        badgeText: JungleLanguageManager.shared.adviceWinBadge,
                        isEmergency: false, isWin: true, isTrade: false
                    )
                    self.optimalAdvice = winAdv
                    return
                }
                
                // 2. 模拟 Step 2：预测敌方最强反制
                var bestOppMove: (from: Position, to: Position, piece: Piece, cap: Piece?)? = nil
                var maxOppGain = -9999999
                
                let oppPieces = simBoard1.filter { $0.value.owner == playerSide.opponent }
                for (oppFrom, oppPiece) in oppPieces {
                    let oppMoves = calculateValidMovesOnBoard(boardState: simBoard1, from: oppFrom)
                    for oppTo in oppMoves {
                        var simBoard2 = simBoard1
                        let oppCap = simBoard2[oppTo]
                        simBoard2.removeValue(forKey: oppFrom)
                        simBoard2[oppTo] = oppPiece
                        
                        let oppEval = evaluateBoardScore(customBoard: simBoard2, for: playerSide.opponent)
                        if oppEval > maxOppGain {
                            maxOppGain = oppEval
                            bestOppMove = (oppFrom, oppTo, oppPiece, oppCap)
                        }
                    }
                }
                
                // 3. 模拟 Step 3：我方后续最佳反击
                var maxFollowScore = -9999999
                
                var simBoard2 = simBoard1
                if let oppM = bestOppMove {
                    simBoard2.removeValue(forKey: oppM.from)
                    simBoard2[oppM.to] = oppM.piece
                }
                
                let myFollowPieces = simBoard2.filter { $0.value.owner == playerSide }
                for (mFrom, mPiece) in myFollowPieces {
                    let myNextMoves = calculateValidMovesOnBoard(boardState: simBoard2, from: mFrom)
                    for mTo in myNextMoves {
                        var simBoard3 = simBoard2
                        simBoard3.removeValue(forKey: mFrom)
                        simBoard3[mTo] = mPiece
                        
                        let eval3 = evaluateBoardScore(customBoard: simBoard3, for: playerSide)
                        if eval3 > maxFollowScore {
                            maxFollowScore = eval3
                        }
                    }
                }
                
                var totalScore = maxFollowScore - (maxOppGain / 2)
                
                let isDangerousDest = calculateDangerousMoves(from: from, moves: [to]).contains(to)
                let curDist = abs(from.row - enemyDen.row) + abs(from.col - enemyDen.col)
                let newDist = abs(to.row - enemyDen.row) + abs(to.col - enemyDen.col)
                let distReduction = curDist - newDist
                let toTile = getTileType(at: to)
                
                var headline = JungleLanguageManager.shared.recommendedMoveHeadline(piece.type)
                var detail = JungleLanguageManager.shared.recommendedMoveDetail
                var badge = JungleLanguageManager.shared.recommendedMoveBadge
                var isEm = false
                var isTr = false
                var forecastText = ""
                
                // 🚨 4. 【大本营生死保卫战】
                if isHomeUnderSiege, let inv = closestInvader {
                    let curDistToDen = abs(from.row - myDen.row) + abs(from.col - myDen.col)
                    let newDistToDen = abs(to.row - myDen.row) + abs(to.col - myDen.col)
                    let curDistToInv = abs(from.row - inv.pos.row) + abs(from.col - inv.pos.col)
                    let newDistToInv = abs(to.row - inv.pos.row) + abs(to.col - inv.pos.col)
                    
                    if to == inv.pos {
                        totalScore += 500000
                        headline = JungleLanguageManager.shared.adviceCounterKillHeadline
                        detail = JungleLanguageManager.shared.adviceCounterKillDetail(piece: piece.type.name, inv: inv.piece.type.name)
                        badge = JungleLanguageManager.shared.adviceCounterKillBadge
                        isEm = true
                    } else if to == myDen || myChokePoints.contains(to) {
                        totalScore += 400000
                        headline = JungleLanguageManager.shared.adviceBlockGateHeadline
                        detail = JungleLanguageManager.shared.adviceBlockGateDetail(piece: piece.type.name, inv: inv.piece.type.name)
                        badge = JungleLanguageManager.shared.adviceBlockGateBadge
                        isEm = true
                    } else if newDistToDen < curDistToDen || newDistToInv < curDistToInv {
                        totalScore += 300000
                        headline = JungleLanguageManager.shared.adviceFallBackHeadline
                        detail = JungleLanguageManager.shared.adviceFallBackDetail(piece: piece.type.name, inv: inv.piece.type.name)
                        badge = JungleLanguageManager.shared.adviceFallBackBadge
                        isEm = true
                    } else {
                        totalScore -= 400000
                    }
                    forecastText = JungleLanguageManager.shared.adviceHomeDefenseForecast(piece: piece.type.name)
                }
                // 🛡️ 5. 【己方受威胁棋子紧急避险/逃入河中 (高危保命铁律)】
                else if isCurrentPieceUnderThreat && !isDangerousDest {
                    totalScore += 380000 + piece.type.pieceValue * 30
                    isEm = true
                    
                    if toTile == .river && piece.type == .rat {
                        headline = JungleLanguageManager.shared.adviceRatSwimEvadeHeadline
                        detail = JungleLanguageManager.shared.adviceRatSwimEvadeDetail
                        badge = JungleLanguageManager.shared.adviceRatSwimEvadeBadge
                        forecastText = JungleLanguageManager.shared.adviceRatSwimEvadeForecast
                    } else if piece.type == .rat {
                        headline = JungleLanguageManager.shared.adviceRatFleeHeadline
                        detail = JungleLanguageManager.shared.adviceRatFleeDetail
                        badge = JungleLanguageManager.shared.adviceRatFleeBadge
                        forecastText = JungleLanguageManager.shared.adviceRatFleeForecast
                    } else {
                        headline = JungleLanguageManager.shared.advicePieceRetreatHeadline(piece: piece.type.name)
                        detail = JungleLanguageManager.shared.advicePieceRetreatDetail(piece: piece.type.name)
                        badge = JungleLanguageManager.shared.advicePieceRetreatBadge
                        forecastText = JungleLanguageManager.shared.advicePieceRetreatForecast(piece: piece.type.name)
                    }
                }
                // 🌊 6. 【水中清障·解除跳河封锁】
                else if isOppRatInRiver, let rPos = oppRatPos, piece.type == .rat {
                    if to == rPos {
                        totalScore += 480000
                        headline = JungleLanguageManager.shared.adviceHuntRiverRatHeadline
                        detail = JungleLanguageManager.shared.adviceHuntRiverRatDetail
                        badge = JungleLanguageManager.shared.adviceHuntRiverRatBadge
                        forecastText = JungleLanguageManager.shared.adviceHuntRiverRatForecast
                    } else {
                        let curD = abs(from.row - rPos.row) + abs(from.col - rPos.col)
                        let newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if newD < curD {
                            totalScore += 220000 + (curD - newD) * 20000
                            headline = JungleLanguageManager.shared.adviceUnderwaterPursuitHeadline
                            detail = JungleLanguageManager.shared.adviceUnderwaterPursuitDetail
                            badge = JungleLanguageManager.shared.adviceUnderwaterPursuitBadge
                            forecastText = JungleLanguageManager.shared.adviceUnderwaterPursuitForecast
                        }
                    }
                }
                // 🐘 7. 【大象遭遇老鼠生死危机】
                else if ratDistToMyElephant <= 2, let rPos = oppRatPos {
                    if piece.type != .elephant && to == rPos {
                        totalScore += 450000
                        headline = JungleLanguageManager.shared.adviceProtectElephantHeadline
                        detail = JungleLanguageManager.shared.adviceProtectElephantDetail(piece: piece.type.name)
                        badge = JungleLanguageManager.shared.adviceProtectElephantBadge
                        forecastText = JungleLanguageManager.shared.adviceProtectElephantForecast(piece: piece.type.name)
                    } else if ratDistToMyElephant == 1 && piece.type == .elephant {
                        let newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if newD > 1 && !isDangerousDest {
                            totalScore += 420000
                            headline = JungleLanguageManager.shared.adviceElephantEscapeHeadline
                            detail = JungleLanguageManager.shared.adviceElephantEscapeDetail
                            badge = JungleLanguageManager.shared.adviceElephantEscapeBadge
                            forecastText = JungleLanguageManager.shared.adviceElephantEscapeForecast
                        }
                    } else if ratDistToMyElephant == 2 && piece.type != .elephant {
                        let curD = abs(from.row - rPos.row) + abs(from.col - rPos.col)
                        let newD = abs(to.row - rPos.row) + abs(to.col - rPos.col)
                        if newD < curD {
                            totalScore += 180000 + (curD - newD) * 15000
                            headline = JungleLanguageManager.shared.adviceFlankRatHeadline
                            detail = JungleLanguageManager.shared.adviceFlankRatDetail(piece: piece.type.name)
                            badge = JungleLanguageManager.shared.adviceFlankRatBadge
                            forecastText = JungleLanguageManager.shared.adviceFlankRatForecast(piece: piece.type.name)
                        }
                    }
                }
                // ⚖️ 8. 【正常吃子与战术兑子分析】
                else if let cap = captured1 {
                    let netProfit = cap.type.pieceValue - piece.type.pieceValue
                    
                    if isDangerousDest {
                        if netProfit > 0 {
                            totalScore += 180000 + netProfit * 40
                            let isRatElephant = (piece.type == .rat && cap.type == .elephant)
                            headline = JungleLanguageManager.shared.adviceProfitableTradeHeadline(piece: piece.type.name, cap: cap.type.name, isRatElephant: isRatElephant)
                            detail = JungleLanguageManager.shared.adviceProfitableTradeDetail(cap: cap.type.name, netProfit: netProfit)
                            badge = JungleLanguageManager.shared.adviceProfitableTradeBadge(netProfit: netProfit)
                            isTr = true
                            let oppKillerName = bestOppMove?.piece.type.name ?? JungleLanguageManager.shared.enemyStrongPiece
                            forecastText = JungleLanguageManager.shared.adviceProfitableTradeForecast(
                                piece: piece.type.name,
                                cap: cap.type.name,
                                capVal: cap.type.pieceValue,
                                oppKiller: oppKillerName,
                                pieceVal: piece.type.pieceValue,
                                netProfit: netProfit
                            )
                        } else {
                            totalScore -= 200000
                            headline = JungleLanguageManager.shared.adviceBadTradeHeadline
                            detail = JungleLanguageManager.shared.adviceBadTradeDetail(cap: cap.type.name, loss: abs(netProfit))
                            badge = JungleLanguageManager.shared.adviceBadTradeBadge
                            forecastText = JungleLanguageManager.shared.adviceBadTradeForecast(cap: cap.type.name)
                        }
                    } else {
                        totalScore += 150000 + cap.type.pieceValue * 50
                        let isRiverJump = (piece.type == .lion || piece.type == .tiger) && (abs(from.row - to.row) > 1)
                        headline = JungleLanguageManager.shared.adviceFreeCaptureHeadline(cap: cap.type.name, isJumpRiver: isRiverJump)
                        detail = JungleLanguageManager.shared.adviceFreeCaptureDetail(piece: piece.type.name, cap: cap.type.name, value: cap.type.pieceValue)
                        badge = JungleLanguageManager.shared.adviceFreeCaptureBadge(cap: cap.type.name)
                        forecastText = JungleLanguageManager.shared.adviceFreeCaptureForecast(piece: piece.type.name, cap: cap.type.name)
                    }
                }
                // 🚶 9. 正常推进
                else {
                    if inEnemyTerritory && distReduction > 0 && !isDangerousDest {
                        totalScore += 45000 + (10 - newDist) * 3500
                        headline = (newDist <= 2) ? JungleLanguageManager.shared.adviceAssaultDenHeadline : JungleLanguageManager.shared.adviceApproachDenHeadline
                        detail = (newDist <= 2) ? JungleLanguageManager.shared.adviceAssaultDenDetail : JungleLanguageManager.shared.adviceApproachDenDetail(piece: piece.type.name)
                        badge = (newDist <= 2) ? JungleLanguageManager.shared.adviceAssaultDenBadge : JungleLanguageManager.shared.adviceApproachDenBadge
                    }
                    let step1Desc = JungleLanguageManager.shared.forecastStep1(piece.type)
                    let step2Desc = JungleLanguageManager.shared.forecastStep2(bestOppMove?.piece.type)
                    let step3Desc = JungleLanguageManager.shared.forecastStep3
                    forecastText = "\(step1Desc) ➔ \(step2Desc) ➔ \(step3Desc)"
                }
                
                // 🛑 10. 禁长捉惩罚
                let repPenalty = calculateRepetitionPenalty(from: from, to: to, piece: piece)
                totalScore -= repPenalty
                
                let adv = OptimalMoveAdvice(
                    from: from, to: to, piece: piece,
                    headline: headline, detailReason: detail,
                    forecast3Steps: forecastText, badgeText: badge,
                    isEmergency: isEm, isWin: false, isTrade: isTr
                )
                evaluatedAdvices.append((adv, totalScore))
            }
        }
        
        if let best = evaluatedAdvices.max(by: { $0.score < $1.score }) {
            self.optimalAdvice = best.advice
        }
    }
    
    // MARK: - 🤖 红方电脑走棋
    func triggerAIMove() {
        var allCandidateMoves: [(from: Position, to: Position, piece: Piece, score: Int)] = []
        let blueDen = Position(row: 8, col: 3)
        let redDen = Position(row: 0, col: 3)
        let redPieces = board.filter { $0.value.owner == .red }
        
        for (from, piece) in redPieces {
            let moves = calculateValidMoves(from: from)
            
            for to in moves {
                // 🏆 制胜判定：一步进穴直接赢
                if to == blueDen {
                    self.isThinking = false
                    self.makeMove(from: from, to: to)
                    return
                }
                
                var simBoard1 = board
                let cap1 = simBoard1[to]
                simBoard1.removeValue(forKey: from)
                simBoard1[to] = piece
                
                // 2-Ply Minimax: 预测蓝方玩家的最强反制
                let bluePieces = simBoard1.filter { $0.value.owner == .blue }
                var worstRedScoreAfterBlue = Int.max
                var bestBlueCounter: (from: Position, to: Position, piece: Piece)? = nil
                
                for (bFrom, bPiece) in bluePieces {
                    let bMoves = calculateValidMovesOnBoard(boardState: simBoard1, from: bFrom)
                    for bTo in bMoves {
                        var simBoard2 = simBoard1
                        simBoard2.removeValue(forKey: bFrom)
                        simBoard2[bTo] = bPiece
                        
                        // 若蓝方一步进红营，红方直接输
                        if bTo == redDen {
                            worstRedScoreAfterBlue = -10_000_000
                            bestBlueCounter = (bFrom, bTo, bPiece)
                            break
                        }
                        
                        let redEval = evaluateBoardScore(customBoard: simBoard2, for: .red)
                        if redEval < worstRedScoreAfterBlue {
                            worstRedScoreAfterBlue = redEval
                            bestBlueCounter = (bFrom, bTo, bPiece)
                        }
                    }
                    if worstRedScoreAfterBlue == -10_000_000 { break }
                }
                
                if bluePieces.isEmpty {
                    worstRedScoreAfterBlue = 10_000_000
                }
                
                var redScore = worstRedScoreAfterBlue
                
                // 3-Ply 深度反击推演 (智谋大师专属)
                if aiDifficulty == .hard, let blueMove = bestBlueCounter, worstRedScoreAfterBlue > -10_000_000 {
                    var simBoard2 = simBoard1
                    simBoard2.removeValue(forKey: blueMove.from)
                    simBoard2[blueMove.to] = blueMove.piece
                    
                    var redFollowUpBest = -9999999
                    let redP3 = simBoard2.filter { $0.value.owner == .red }
                    for (r3From, r3Piece) in redP3 {
                        let r3Moves = calculateValidMovesOnBoard(boardState: simBoard2, from: r3From)
                        for r3To in r3Moves {
                            var simBoard3 = simBoard2
                            simBoard3.removeValue(forKey: r3From)
                            simBoard3[r3To] = r3Piece
                            
                            if r3To == blueDen {
                                redFollowUpBest = 10_000_000
                                break
                            }
                            let r3Score = evaluateBoardScore(customBoard: simBoard3, for: .red)
                            if r3Score > redFollowUpBest {
                                redFollowUpBest = r3Score
                            }
                        }
                        if redFollowUpBest == 10_000_000 { break }
                    }
                    if redFollowUpBest > -9999999 {
                        redScore += (redFollowUpBest - worstRedScoreAfterBlue) / 3
                    }
                }
                
                // 战术吃子加成 (优先吃掉高战力敌子)
                if let target = cap1 {
                    redScore += (target.type.rawValue * 300)
                }
                
                // 狮虎隔河飞跃主动进攻奖励
                let isRiverJump = (piece.type == .lion || piece.type == .tiger) && (abs(from.row - to.row) > 1 || abs(from.col - to.col) > 1)
                if isRiverJump {
                    redScore += 800
                }
                
                // 防重复来回巡逻惩罚
                let repPenalty = calculateRepetitionPenalty(from: from, to: to, piece: piece)
                redScore -= repPenalty
                
                // 难度微调扰动
                switch aiDifficulty {
                case .easy: redScore += Int.random(in: -600...600)
                case .normal: redScore += Int.random(in: -150...150)
                case .hard: redScore += 0
                }
                
                allCandidateMoves.append((from, to, piece, redScore))
            }
        }
        
        self.isThinking = false
        if let best = allCandidateMoves.max(by: { $0.score < $1.score }) {
            self.makeMove(from: best.from, to: best.to)
        } else {
            self.winner = .blue
        }
    }
    
    // MARK: - 辅助模拟函数 (用于 Minimax 棋盘状态推演)
    func calculateValidMovesOnBoard(boardState: [Position: Piece], from: Position) -> [Position] {
        guard let piece = boardState[from] else { return [] }
        var result: [Position] = []
        let directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        
        for (dr, dc) in directions {
            let nr = from.row + dr
            let nc = from.col + dc
            let targetPos = Position(row: nr, col: nc)
            guard (0...8).contains(nr) && (0...6).contains(nc) else { continue }
            
            let targetTile = getTileType(at: targetPos)
            if case .den(let owner) = targetTile, owner == piece.owner { continue }
            
            if targetTile == .river {
                if piece.type == .rat {
                    if canOccupyOrCaptureOnBoard(boardState: boardState, attacker: piece, from: from, to: targetPos) {
                        result.append(targetPos)
                    }
                } else if piece.type == .lion || piece.type == .tiger {
                    if let landing = getRiverJumpLandingOnBoard(boardState: boardState, from: from, dr: dr, dc: dc) {
                        if canOccupyOrCaptureOnBoard(boardState: boardState, attacker: piece, from: from, to: landing) {
                            result.append(landing)
                        }
                    }
                }
            } else {
                if canOccupyOrCaptureOnBoard(boardState: boardState, attacker: piece, from: from, to: targetPos) {
                    result.append(targetPos)
                }
            }
        }
        return result
    }
    
    private func getRiverJumpLandingOnBoard(boardState: [Position: Piece], from: Position, dr: Int, dc: Int) -> Position? {
        var currR = from.row + dr
        var currC = from.col + dc
        while (0...8).contains(currR) && (0...6).contains(currC) {
            let pos = Position(row: currR, col: currC)
            let tile = getTileType(at: pos)
            if tile != .river {
                return pos
            }
            if let p = boardState[pos], p.type == .rat {
                return nil
            }
            currR += dr
            currC += dc
        }
        return nil
    }
    
    private func canOccupyOrCaptureOnBoard(boardState: [Position: Piece], attacker: Piece, from: Position, to: Position) -> Bool {
        guard let defender = boardState[to] else { return true }
        guard defender.owner != attacker.owner else { return false }
        
        let toTile = getTileType(at: to)
        if case .trap(let trapOwner) = toTile, trapOwner == attacker.owner {
            return true
        }
        
        let fromTile = getTileType(at: from)
        if fromTile == .river && toTile != .river {
            return false
        }
        if fromTile != .river && toTile == .river {
            return false
        }
        
        if attacker.type == .rat && defender.type == .elephant {
            return true
        }
        if attacker.type == .elephant && defender.type == .rat {
            return false
        }
        
        return attacker.type.rawValue >= defender.type.rawValue
    }
    
    func getActionBadge(for pos: Position, from: Position) -> String? {
        guard let movingPiece = board[from] else { return nil }
        guard movingPiece.owner == .blue || !isVsAI else { return nil }
        guard validMoves.contains(pos) else { return nil }
        
        if optimalAdvice?.from == from && optimalAdvice?.to == pos {
            return optimalAdvice?.badgeText
        }
        
        if let target = board[pos], target.owner != movingPiece.owner {
            if movingPiece.type == .rat && target.type == .elephant {
                return "⚡️ \(JungleLanguageManager.shared.eatElephantBadge)"
            }
            if target.type == .rat {
                return "🛡️ \(JungleLanguageManager.shared.protectElephantBadge)"
            }
            return "🎯 \(JungleLanguageManager.shared.captureTargetBadge(target.type))"
        }
        
        if (movingPiece.type == .lion || movingPiece.type == .tiger) && (abs(from.row - pos.row) > 1 || abs(from.col - pos.col) > 1) {
            return "🌊 \(JungleLanguageManager.shared.jumpRiverBadge)"
        }
        
        if case .den(let owner) = getTileType(at: pos), owner != movingPiece.owner {
            return "🏆 \(JungleLanguageManager.shared.winGameBadge)"
        }
        
        return nil
    }
    
    func calculateDangerousMoves(from: Position, moves: [Position]) -> Set<Position> {
        guard let movingPiece = board[from] else { return [] }
        var dangerous = Set<Position>()
        let opponent = movingPiece.owner.opponent
        
        for dest in moves {
            for (oppPos, oppPiece) in board where oppPiece.owner == opponent && oppPos != dest {
                let oppMoves = calculateValidMovesHypothetical(from: oppPos, piece: oppPiece, ignorePos: from, replacePos: (dest, movingPiece))
                if oppMoves.contains(dest) {
                    dangerous.insert(dest)
                    break
                }
            }
        }
        return dangerous
    }
    
    private func calculateValidMovesHypothetical(from: Position, piece: Piece, ignorePos: Position, replacePos: (Position, Piece)) -> [Position] {
        var result: [Position] = []
        let directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        
        var simBoard = board
        simBoard.removeValue(forKey: ignorePos)
        simBoard[replacePos.0] = replacePos.1
        
        for (dr, dc) in directions {
            let nr = from.row + dr
            let nc = from.col + dc
            let targetPos = Position(row: nr, col: nc)
            guard (0...8).contains(nr) && (0...6).contains(nc) else { continue }
            
            let targetTile = getTileType(at: targetPos)
            if case .den(let owner) = targetTile, owner == piece.owner { continue }
            
            var destPiece: Piece? = nil
            if targetPos == replacePos.0 {
                destPiece = replacePos.1
            } else if targetPos != ignorePos {
                destPiece = simBoard[targetPos]
            }
            
            if targetTile == .river {
                if piece.type == .rat {
                    if let dest = destPiece {
                        if dest.owner != piece.owner && piece.type.rawValue >= dest.type.rawValue {
                            result.append(targetPos)
                        }
                    } else {
                        result.append(targetPos)
                    }
                } else if piece.type == .lion || piece.type == .tiger {
                    if let landing = getRiverJumpLandingOnBoard(boardState: simBoard, from: from, dr: dr, dc: dc) {
                        if canOccupyOrCaptureOnBoard(boardState: simBoard, attacker: piece, from: from, to: landing) {
                            result.append(landing)
                        }
                    }
                }
            } else {
                if let dest = destPiece {
                    if dest.owner != piece.owner {
                        if case .trap(let trapOwner) = targetTile, trapOwner == piece.owner {
                            result.append(targetPos)
                        } else if piece.type == .rat && dest.type == .elephant {
                            result.append(targetPos)
                        } else if piece.type == .elephant && dest.type == .rat {
                        } else if piece.type.rawValue >= dest.type.rawValue {
                            result.append(targetPos)
                        }
                    }
                } else {
                    result.append(targetPos)
                }
            }
        }
        return result
    }
    
    func calculateValidMoves(from: Position) -> [Position] {
        guard let piece = board[from] else { return [] }
        var result: [Position] = []
        let directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        
        for (dr, dc) in directions {
            let nextR = from.row + dr
            let nextC = from.col + dc
            let targetPos = Position(row: nextR, col: nextC)
            guard (0...8).contains(nextR) && (0...6).contains(nextC) else { continue }
            
            let targetTile = getTileType(at: targetPos)
            if case .den(let owner) = targetTile, owner == piece.owner { continue }
            
            if targetTile == .river {
                if piece.type == .rat {
                    if canOccupyOrCapture(attacker: piece, from: from, to: targetPos) {
                        result.append(targetPos)
                    }
                } else if piece.type == .lion || piece.type == .tiger {
                    if let landingPos = getRiverJumpLanding(from: from, dr: dr, dc: dc) {
                        if canOccupyOrCapture(attacker: piece, from: from, to: landingPos) {
                            result.append(landingPos)
                        }
                    }
                }
            } else {
                if canOccupyOrCapture(attacker: piece, from: from, to: targetPos) {
                    result.append(targetPos)
                }
            }
        }
        return result
    }
    
    private func getRiverJumpLanding(from: Position, dr: Int, dc: Int) -> Position? {
        var currR = from.row + dr
        var currC = from.col + dc
        while (0...8).contains(currR) && (0...6).contains(currC) {
            let pos = Position(row: currR, col: currC)
            if getTileType(at: pos) == .river {
                if board[pos] != nil { return nil }
                currR += dr
                currC += dc
            } else {
                return pos
            }
        }
        return nil
    }
    
    private func canOccupyOrCapture(attacker: Piece, from: Position, to: Position) -> Bool {
        guard let defender = board[to] else { return true }
        if defender.owner == attacker.owner { return false }
        
        if attacker.owner == .blue && frenzyRemainingTurns > 0 {
            return true
        }
        
        let fromTile = getTileType(at: from)
        let toTile = getTileType(at: to)
        
        if case .trap(let trapOwner) = toTile, trapOwner == attacker.owner {
            return true
        }
        if attacker.type == .rat && defender.type == .elephant {
            if fromTile == .river && toTile != .river { return false }
            return true
        }
        if attacker.type == .elephant && defender.type == .rat {
            return false
        }
        if fromTile == .river && toTile != .river { return false }
        if fromTile != .river && toTile == .river { return false }
        
        return attacker.type.rawValue >= defender.type.rawValue
    }
    
    private func checkWin(afterMoveTo to: Position, by player: Player) -> Bool {
        if case .den(let owner) = getTileType(at: to), owner != player {
            return true
        }
        let opponentHasPieces = board.values.contains { $0.owner == player.opponent }
        return !opponentHasPieces
    }
    
    enum SoundEffect { case select, move, capture, undo, win }
    private func playSound(type: SoundEffect) {
        #if os(iOS)
        switch type {
        case .select:
            AudioServicesPlaySystemSound(1104)
            UIImpactFeedbackGenerator(style: .light).impactOccurred()
        case .move:
            AudioServicesPlaySystemSound(1057)
            UIImpactFeedbackGenerator(style: .medium).impactOccurred()
        case .capture:
            AudioServicesPlaySystemSound(1054)
            UIImpactFeedbackGenerator(style: .heavy).impactOccurred()
        case .undo:
            AudioServicesPlaySystemSound(1105)
            UINotificationFeedbackGenerator().notificationOccurred(.warning)
        case .win:
            AudioServicesPlaySystemSound(1025)
            UINotificationFeedbackGenerator().notificationOccurred(.success)
        }
        #endif
    }
}

// MARK: - 3. 界面设计

struct ContentView: View {
    @StateObject private var engine = JungleGameEngine()
    @ObservedObject private var lang = JungleLanguageManager.shared
    @State private var showRules = CommandLine.arguments.contains("-showRules")
    
    var body: some View {
        ZStack {
            LinearGradient(colors: [Color(red: 0.94, green: 0.98, blue: 0.93), Color(red: 0.88, green: 0.94, blue: 0.86)],
                           startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            VStack(spacing: 5) {
                headerView
                difficultySelectorBar
                skillBannerView
                optimalMoveGuidanceCard
                ruleHelperCheatBar
                
                BoardView(engine: engine)
                    .padding(.horizontal, 6)
                
                superpowerBar
                bottomControls
            }
            .padding(.vertical, 4)
            
            if let winner = engine.winner {
                VictoryOverlay(winner: winner) {
                    engine.resetGame()
                }
            }
        }
        .sheet(isPresented: $showRules) {
            RuleGuideView()
        }
    }
    
    private var headerView: some View {
        HStack {
            VStack(alignment: .leading, spacing: 1) {
                Text(lang.appTitle)
                    .font(.system(size: 18, weight: .black, design: .rounded))
                    .foregroundColor(Color(red: 0.15, green: 0.38, blue: 0.15))
                    .lineLimit(1)
                    .minimumScaleFactor(0.65)
                    .layoutPriority(1)
                
                Text("🔵 \(lang.playerName(.blue)): \(engine.winAdvantagePercent)% (\(lang.doctrineName(engine.currentDoctrine)))")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(.blue)
                    .lineLimit(1)
            }
            
            Spacer()
            
            Menu {
                Button(action: {
                    lang.setFollowSystem()
                    engine.computePlayerOptimalMove()
                }) {
                    HStack {
                        Text(lang.systemDefaultText)
                        if lang.isFollowingSystem {
                            Image(systemName: "checkmark")
                        }
                    }
                }
                
                Divider()
                
                ForEach(JungleLanguage.allCases) { item in
                    Button(action: {
                        lang.setLanguage(item)
                        engine.computePlayerOptimalMove()
                    }) {
                        HStack {
                            Text(item.displayName)
                            if !lang.isFollowingSystem && lang.currentLanguage == item {
                                Image(systemName: "checkmark")
                            }
                        }
                    }
                }
            } label: {
                HStack(spacing: 3) {
                    Text(lang.currentLanguage.flag)
                    Image(systemName: "globe")
                        .font(.system(size: 12, weight: .bold))
                }
                .padding(.horizontal, 8)
                .padding(.vertical, 5)
                .background(Color.white)
                .cornerRadius(16)
                .shadow(color: .black.opacity(0.06), radius: 2)
            }
            
            Button(action: {
                engine.isVsAI.toggle()
                engine.resetGame()
            }) {
                HStack(spacing: 4) {
                    Image(systemName: engine.isVsAI ? "person.fill" : "person.2.fill")
                    Text(lang.modeName(isVsAI: engine.isVsAI))
                }
                .font(.system(size: 12, weight: .bold))
                .padding(.horizontal, 8)
                .padding(.vertical, 5)
                .background(Color.white)
                .cornerRadius(16)
                .shadow(color: .black.opacity(0.06), radius: 2)
            }
        }
        .padding(.horizontal, 14)
    }
    
    private var difficultySelectorBar: some View {
        HStack(spacing: 6) {
            ForEach(AIDifficulty.allCases) { diff in
                Button(action: {
                    engine.aiDifficulty = diff
                    engine.resetGame()
                }) {
                    Text(lang.difficultyName(diff))
                        .font(.system(size: 11, weight: engine.aiDifficulty == diff ? .black : .bold))
                        .foregroundColor(engine.aiDifficulty == diff ? .white : .primary)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 5)
                        .background(
                            engine.aiDifficulty == diff ?
                            (diff == .hard ? Color.red : (diff == .normal ? Color.orange : Color.green)) :
                            Color.white.opacity(0.85)
                        )
                        .cornerRadius(8)
                        .shadow(color: .black.opacity(0.04), radius: 1)
                }
            }
        }
        .padding(.horizontal, 14)
    }
    
    @ViewBuilder
    private var skillBannerView: some View {
        if let msg = engine.skillBannerText {
            Text(msg)
                .font(.system(size: 11, weight: .black))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 4)
                .background(LinearGradient(colors: [Color.purple, Color.indigo], startPoint: .leading, endPoint: .trailing))
                .cornerRadius(6)
                .padding(.horizontal, 14)
                .transition(.scale)
        }
    }
    
    private var optimalMoveGuidanceCard: some View {
        VStack(alignment: .leading, spacing: 3) {
            if let advice = engine.optimalAdvice {
                HStack(spacing: 6) {
                    Text(advice.isEmergency ? "🚨" : (advice.isWin ? "🏆" : (advice.isTrade ? "⚡️" : "👑")))
                        .font(.system(size: 15))
                    
                    Text(advice.headline)
                        .font(.system(size: 13, weight: .black))
                        .foregroundColor(advice.isEmergency ? .red : (advice.isWin ? .orange : (advice.isTrade ? Color.purple : Color(red: 0.1, green: 0.55, blue: 0.15))))
                    
                    Spacer()
                    
                    Button(action: {
                        engine.autoSelectRecommendedPiece()
                    }) {
                        Text(lang.highlightBtn)
                            .font(.system(size: 10, weight: .bold))
                            .foregroundColor(.blue)
                            .padding(.horizontal, 6)
                            .padding(.vertical, 2)
                            .background(Color.blue.opacity(0.1))
                            .cornerRadius(4)
                    }
                }
                
                Text(advice.detailReason)
                    .font(.system(size: 12, weight: .bold))
                    .foregroundColor(.primary)
                    .lineLimit(2)
                    .fixedSize(horizontal: false, vertical: true)
                
                HStack(spacing: 4) {
                    Text(lang.depth3DeductionTitle)
                        .font(.system(size: 10, weight: .black))
                        .foregroundColor(.purple)
                    Text(advice.forecast3Steps)
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(Color(red: 0.3, green: 0.2, blue: 0.5))
                        .lineLimit(1)
                }
                .padding(.top, 2)
            } else {
                Text(lang.deducingBestMove)
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(.secondary)
            }
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
        .background(engine.optimalAdvice?.isEmergency == true ? Color.red.opacity(0.15) :
                    (engine.optimalAdvice?.isTrade == true ? Color.purple.opacity(0.15) : Color.white.opacity(0.95)))
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(engine.optimalAdvice?.isEmergency == true ? Color.red :
                        (engine.optimalAdvice?.isTrade == true ? Color.purple : Color.green.opacity(0.7)), lineWidth: 1.5)
        )
        .shadow(color: .black.opacity(0.04), radius: 2)
        .padding(.horizontal, 14)
    }
    
    private var ruleHelperCheatBar: some View {
        HStack(spacing: 6) {
            if let p = engine.inspectPiece {
                Text("\(p.type.emoji)【\(lang.animalName(p.type))】:")
                    .font(.system(size: 12, weight: .black))
                    .foregroundColor(p.owner.color)
                
                Text("\(lang.animalEatRule(p.type))")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(.primary)
                    .lineLimit(1)
            } else {
                Text(lang.rulePromptHint)
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            Spacer()
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 4)
        .background(Color.white.opacity(0.8))
        .cornerRadius(6)
        .padding(.horizontal, 14)
    }
    
    private var superpowerBar: some View {
        HStack(spacing: 8) {
            Button(action: { engine.activateShieldRecallSkill() }) {
                HStack(spacing: 4) {
                    Image(systemName: "shield.righthalf.filled")
                    Text(lang.shieldSkillText(engine.shieldSkillCount))
                }
                .font(.system(size: 11, weight: .black))
                .foregroundColor(engine.shieldSkillCount > 0 ? .white : .secondary)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 7)
                .background(engine.shieldSkillCount > 0 ? Color.indigo : Color.gray.opacity(0.3))
                .cornerRadius(10)
                .shadow(color: .indigo.opacity(0.3), radius: 2)
            }
            .disabled(engine.shieldSkillCount == 0)
            
            Button(action: { engine.activateFrenzySkill() }) {
                HStack(spacing: 4) {
                    Image(systemName: "sparkles")
                    Text(lang.frenzySkillText(engine.frenzySkillCount))
                }
                .font(.system(size: 11, weight: .black))
                .foregroundColor(engine.frenzySkillCount > 0 ? .white : .secondary)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 7)
                .background(engine.frenzySkillCount > 0 ? Color.purple : Color.gray.opacity(0.3))
                .cornerRadius(10)
                .shadow(color: .purple.opacity(0.3), radius: 2)
            }
            .disabled(engine.frenzySkillCount == 0)
        }
        .padding(.horizontal, 14)
    }
    
    private var bottomControls: some View {
        HStack(spacing: 6) {
            Button(action: { engine.executeBestAdvice() }) {
                HStack(spacing: 4) {
                    Image(systemName: engine.optimalAdvice?.isEmergency == true ? "shield.fill" : (engine.optimalAdvice?.isWin == true ? "trophy.fill" : "sparkles"))
                    Text(lang.moveForMeBtn)
                        .multilineTextAlignment(.center)
                        .font(.system(size: 12, weight: .black))
                        .lineLimit(2)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 8)
                .background(
                    engine.optimalAdvice?.isEmergency == true ?
                    LinearGradient(colors: [Color.red, Color.orange], startPoint: .topLeading, endPoint: .bottomTrailing) :
                    (engine.optimalAdvice?.isTrade == true ?
                     LinearGradient(colors: [Color.purple, Color.indigo], startPoint: .topLeading, endPoint: .bottomTrailing) :
                     LinearGradient(colors: [Color.green, Color(red: 0.1, green: 0.6, blue: 0.3)], startPoint: .topLeading, endPoint: .bottomTrailing))
                )
                .foregroundColor(.white)
                .cornerRadius(12)
                .shadow(color: .green.opacity(0.35), radius: 4, y: 2)
            }
            
            Button(action: { engine.undo() }) {
                Label(lang.undoBtn, systemImage: "arrow.uturn.backward.circle.fill")
                    .font(.system(size: 13, weight: .bold))
                    .padding(.horizontal, 8)
                    .padding(.vertical, 10)
                    .background(Color.white)
                    .foregroundColor(.blue)
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.06), radius: 2)
            }
            
            Button(action: { engine.resetGame() }) {
                Label(lang.restartBtn, systemImage: "arrow.clockwise.circle.fill")
                    .font(.system(size: 13, weight: .bold))
                    .padding(.horizontal, 8)
                    .padding(.vertical, 10)
                    .background(Color.white)
                    .foregroundColor(.red)
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.06), radius: 2)
            }
            
            Button(action: { showRules = true }) {
                Image(systemName: "questionmark.circle.fill")
                    .font(.system(size: 18, weight: .bold))
                    .padding(10)
                    .background(Color.white)
                    .foregroundColor(.purple)
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.06), radius: 2)
            }
        }
        .padding(.horizontal, 14)
    }
}

// MARK: - 4. 棋盘渲染

struct BoardView: View {
    @ObservedObject var engine: JungleGameEngine
    @State private var isPulsing: Bool = false
    
    var body: some View {
        GeometryReader { geo in
            let totalWidth = geo.size.width
            let cellSize = totalWidth / 7.0
            let totalHeight = cellSize * 9.0
            
            ZStack(alignment: .topLeading) {
                // 1. 底层棋盘网格
                VStack(spacing: 0) {
                    ForEach(0..<9, id: \.self) { r in
                        HStack(spacing: 0) {
                            ForEach(0..<7, id: \.self) { c in
                                let pos = Position(row: r, col: c)
                                let tileType = engine.getTileType(at: pos)
                                
                                let actionBadge: String? = {
                                    if let selected = engine.selectedPos {
                                        return engine.getActionBadge(for: pos, from: selected)
                                    } else if engine.optimalAdvice?.to == pos {
                                        return engine.optimalAdvice?.badgeText
                                    }
                                    return nil
                                }()
                                
                                let lastOpMove = engine.lastOpponentMove
                                let isOpponentSource = (lastOpMove?.from == pos) && (lastOpMove?.movedPiece.owner != engine.currentTurn) && (engine.board[pos] == nil)
                                
                                TileView(tileType: tileType,
                                         isValidTarget: engine.validMoves.contains(pos),
                                         isDangerous: engine.dangerousMoves.contains(pos),
                                         actionBadgeText: actionBadge,
                                         isGlobalOptimalTarget: engine.optimalAdvice?.to == pos,
                                         isEmergencyTarget: engine.optimalAdvice?.isEmergency == true && engine.optimalAdvice?.to == pos,
                                         isSelected: engine.selectedPos == pos,
                                         isOpponentSource: isOpponentSource)
                                    .frame(width: cellSize, height: cellSize)
                                    .onTapGesture {
                                        engine.handleTileTap(at: pos)
                                    }
                            }
                        }
                    }
                }
                .background(Color(red: 0.92, green: 0.88, blue: 0.78))
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color(red: 0.45, green: 0.32, blue: 0.18), lineWidth: 3)
                )
                
                // 2. 棋子层
                ForEach(Array(engine.board.keys), id: \.self) { pos in
                    if let piece = engine.board[pos] {
                        let isPlayerPiece = (piece.owner == .blue || !engine.isVsAI)
                        let isOptimalSourcePiece = isPlayerPiece && (engine.currentTurn == piece.owner) && (engine.optimalAdvice?.from == pos)
                        let isOpponentTarget = (engine.lastOpponentMove?.to == pos) && (piece.owner != engine.currentTurn)
                        
                        PieceBadgeView(piece: piece,
                                       isSelected: engine.selectedPos == pos,
                                       isOptimalSourcePiece: isOptimalSourcePiece,
                                       isEmergencyPiece: isOptimalSourcePiece && (engine.optimalAdvice?.isEmergency == true),
                                       isOpponentTarget: isOpponentTarget,
                                       isPulsing: isPulsing)
                            .frame(width: cellSize * 0.88, height: cellSize * 0.88)
                            .offset(x: CGFloat(pos.col) * cellSize + cellSize * 0.06,
                                    y: CGFloat(pos.row) * cellSize + cellSize * 0.06)
                            .animation(.spring(response: 0.35, dampingFraction: 0.75), value: pos)
                            .onTapGesture {
                                engine.handleTileTap(at: pos)
                            }
                    }
                }
            }
            .frame(width: totalWidth, height: totalHeight)
            .onAppear {
                withAnimation(Animation.easeInOut(duration: 0.75).repeatForever(autoreverses: true)) {
                    isPulsing = true
                }
            }
        }
        .aspectRatio(7.0 / 9.0, contentMode: .fit)
    }
}

// 单个格子
struct TileView: View {
    let tileType: TileType
    let isValidTarget: Bool
    let isDangerous: Bool
    let actionBadgeText: String?
    let isGlobalOptimalTarget: Bool
    let isEmergencyTarget: Bool
    let isSelected: Bool
    var isOpponentSource: Bool = false
    
    var body: some View {
        ZStack {
            Rectangle()
                .fill(tileBackgroundColor)
                .border(Color(red: 0.75, green: 0.65, blue: 0.5).opacity(0.4), width: 0.5)
            
            switch tileType {
            case .river:
                Text("🌊").font(.system(size: 15)).opacity(0.7)
            case .trap(let owner):
                VStack(spacing: -2) {
                    Text("⚠️").font(.system(size: 10))
                    Text(owner == .red ? JungleLanguageManager.shared.redTrapLabel : JungleLanguageManager.shared.blueTrapLabel)
                        .font(.system(size: 7, weight: .bold))
                        .foregroundColor(owner.color.opacity(0.8))
                        .minimumScaleFactor(0.7)
                        .lineLimit(1)
                }
            case .den(let owner):
                VStack(spacing: -2) {
                    Text("🏰").font(.system(size: 12))
                    Text(owner == .red ? JungleLanguageManager.shared.redDenLabel : JungleLanguageManager.shared.blueDenLabel)
                        .font(.system(size: 7, weight: .bold))
                        .foregroundColor(owner.color)
                        .minimumScaleFactor(0.7)
                        .lineLimit(1)
                }
            case .land:
                EmptyView()
            }
            
            // 🚨 1. 紧急保命/保卫大本营目标格子 (红标)
            if isEmergencyTarget, let badge = actionBadgeText {
                Text(badge)
                    .font(.system(size: 8, weight: .black))
                    .foregroundColor(.white)
                    .padding(.horizontal, 3)
                    .padding(.vertical, 2)
                    .background(Color.red)
                    .cornerRadius(4)
                    .shadow(radius: 3)
                    .overlay(
                        RoundedRectangle(cornerRadius: 6)
                            .stroke(Color.red, lineWidth: 3)
                    )
            }
            // 👑 2. 金色最优推荐或行动标牌
            else if (isValidTarget || isGlobalOptimalTarget), let badge = actionBadgeText {
                Text(badge)
                    .font(.system(size: 8, weight: .black))
                    .foregroundColor(.white)
                    .padding(.horizontal, 3)
                    .padding(.vertical, 2)
                    .background((badge.contains("净赚") || badge.contains("+")) ? Color.purple : (isGlobalOptimalTarget ? Color(red: 0.1, green: 0.6, blue: 0.2) : Color(red: 0.15, green: 0.55, blue: 0.2)))
                    .cornerRadius(4)
                    .shadow(radius: 2)
                    .overlay(
                        RoundedRectangle(cornerRadius: 6)
                            .stroke((badge.contains("净赚") || badge.contains("+")) ? Color.pink : Color.green, lineWidth: 2)
                    )
            }
            // 🔴 3. 红色危险格子
            else if isValidTarget && isDangerous {
                Text("⚠️")
                    .font(.system(size: 10, weight: .black))
                    .foregroundColor(.white)
                    .padding(.horizontal, 3)
                    .padding(.vertical, 2)
                    .background(Color.red.opacity(0.85))
                    .cornerRadius(4)
            }
            // 🟢 4. 正常安全格子 (清爽绿圈)
            else if isValidTarget {
                Circle()
                    .fill(Color.green.opacity(0.45))
                    .frame(width: 18, height: 18)
                    .overlay(Circle().stroke(Color.green, lineWidth: 2))
            }
            // 📍 5. 对方行棋起点标记
            else if isOpponentSource {
                VStack(spacing: 1) {
                    Text("📍")
                        .font(.system(size: 9))
                    Text(JungleLanguageManager.shared.lastOpponentSourceBadge)
                        .font(.system(size: 7, weight: .bold))
                        .foregroundColor(Color.red.opacity(0.85))
                        .minimumScaleFactor(0.7)
                        .lineLimit(1)
                }
                .padding(.horizontal, 2)
                .padding(.vertical, 1)
                .background(Color(red: 1.0, green: 0.92, blue: 0.92).opacity(0.9))
                .cornerRadius(4)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .stroke(Color.red.opacity(0.4), lineWidth: 1)
                )
            }
            
            if isSelected {
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color.yellow, lineWidth: 3)
            }
        }
    }
    
    var tileBackgroundColor: Color {
        if isOpponentSource {
            return Color(red: 1.0, green: 0.94, blue: 0.94)
        }
        switch tileType {
        case .river: return Color(red: 0.65, green: 0.85, blue: 0.95)
        case .trap: return Color(red: 0.98, green: 0.92, blue: 0.8)
        case .den: return Color(red: 0.95, green: 0.85, blue: 0.85)
        case .land: return Color(red: 0.93, green: 0.89, blue: 0.80)
        }
    }
}

// 棋子视图
struct PieceBadgeView: View {
    @ObservedObject private var lang = JungleLanguageManager.shared
    let piece: Piece
    let isSelected: Bool
    let isOptimalSourcePiece: Bool
    let isEmergencyPiece: Bool
    var isOpponentTarget: Bool = false
    let isPulsing: Bool
    
    private var shouldPulse: Bool {
        return isOptimalSourcePiece || isOpponentTarget
    }
    
    var body: some View {
        ZStack {
            // 1. 闪烁光晕外圈 (只针对：蓝方推荐走棋 或 对方刚走过的棋子)
            if isOptimalSourcePiece {
                Circle()
                    .stroke(Color.green.opacity(isPulsing ? 0.85 : 0.25), lineWidth: 3.5)
                    .scaleEffect(isPulsing ? 1.18 : 1.04)
            } else if isOpponentTarget {
                Circle()
                    .stroke(Color(red: 1.0, green: 0.1, blue: 0.25).opacity(isPulsing ? 0.85 : 0.25), lineWidth: 3.5)
                    .scaleEffect(isPulsing ? 1.18 : 1.04)
            }
            
            // 2. 棋子圆盘底色
            Circle()
                .fill(isEmergencyPiece ? Color(red: 1.0, green: 0.85, blue: 0.85) :
                      (isOpponentTarget ? Color(red: 1.0, green: 0.93, blue: 0.93) :
                      (isOptimalSourcePiece ? Color(red: 0.9, green: 0.98, blue: 0.9) : Color.white)))
                .shadow(color: isOpponentTarget ? Color.red.opacity(0.4) : (isOptimalSourcePiece ? Color.green.opacity(0.35) : Color.black.opacity(0.2)),
                        radius: isSelected ? 5 : (shouldPulse ? 4 : 2),
                        x: 0,
                        y: isSelected ? 3 : 1.5)
            
            // 3. 棋子边缘描边
            Circle()
                .stroke(isEmergencyPiece ? Color.red :
                        (isOpponentTarget ? Color(red: 1.0, green: 0.1, blue: 0.25) :
                        (isOptimalSourcePiece ? Color.green : piece.owner.color)),
                        lineWidth: isEmergencyPiece ? 4 : (isOpponentTarget ? 3.5 : (isOptimalSourcePiece ? 3.5 : 3)))
            
            // 4. 动物图标与名称
            VStack(spacing: -2) {
                Text(piece.type.emoji).font(.system(size: 18))
                Text(lang.animalName(piece.type))
                    .font(.system(size: 10, weight: .black))
                    .foregroundColor(isOpponentTarget ? Color(red: 0.9, green: 0.1, blue: 0.2) : (isOptimalSourcePiece ? Color(red: 0.05, green: 0.55, blue: 0.15) : piece.owner.color))
                    .minimumScaleFactor(0.7)
                    .lineLimit(1)
            }
            
            // 5. 角标提示 (紧急/敌步/推荐)
            if isEmergencyPiece {
                VStack {
                    HStack {
                        Spacer()
                        Text("🚨")
                            .font(.system(size: 8, weight: .black))
                            .foregroundColor(.white)
                            .padding(.horizontal, 2)
                            .padding(.vertical, 1)
                            .background(Color.red)
                            .cornerRadius(4)
                            .offset(x: 4, y: -4)
                    }
                    Spacer()
                }
            }
            else if isOpponentTarget {
                VStack {
                    HStack {
                        Spacer()
                        Text(lang.lastOpponentMoveBadge)
                            .font(.system(size: 8, weight: .black))
                            .foregroundColor(.white)
                            .padding(.horizontal, 3)
                            .padding(.vertical, 1.5)
                            .background(Color(red: 1.0, green: 0.1, blue: 0.25))
                            .cornerRadius(4)
                            .shadow(radius: 2)
                            .offset(x: 4, y: -4)
                    }
                    Spacer()
                }
            }
            else if isOptimalSourcePiece {
                VStack {
                    HStack {
                        Spacer()
                        Text("1️⃣")
                            .font(.system(size: 8, weight: .black))
                            .foregroundColor(.white)
                            .padding(.horizontal, 2)
                            .padding(.vertical, 1)
                            .background(Color.green)
                            .cornerRadius(4)
                            .offset(x: 4, y: -4)
                    }
                    Spacer()
                }
            }
        }
        .scaleEffect(isSelected ? 1.16 : (shouldPulse ? (isPulsing ? 1.09 : 1.0) : 1.0))
    }
}

// 胜利祝贺弹窗
struct VictoryOverlay: View {
    @ObservedObject private var lang = JungleLanguageManager.shared
    let winner: Player
    let onRestart: () -> Void
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.5).ignoresSafeArea()
            
            VStack(spacing: 16) {
                Text(lang.victoryTitle)
                    .font(.system(size: 23, weight: .black, design: .rounded))
                    .foregroundColor(.orange)
                
                Text("👑 \(lang.playerName(winner))")
                    .font(.system(size: 20, weight: .bold))
                    .foregroundColor(winner.color)
                
                Button(action: onRestart) {
                    Text(lang.playAgainBtn)
                        .font(.system(size: 17, weight: .bold))
                        .foregroundColor(.white)
                        .padding(.horizontal, 32)
                        .padding(.vertical, 12)
                        .background(winner.color)
                        .cornerRadius(22)
                        .shadow(radius: 6)
                }
            }
            .padding(26)
            .background(Color.white)
            .cornerRadius(24)
            .shadow(radius: 12)
            .padding(.horizontal, 32)
        }
    }
}

// 规则指引
struct RuleGuideView: View {
    @ObservedObject private var lang = JungleLanguageManager.shared
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Group {
                        sectionTitle("🎯 \(lang.ruleWinConditionTitle)")
                        Text(lang.ruleWinConditionDesc)
                    }
                    Divider()
                    Group {
                        sectionTitle("🦁 \(lang.ruleHierarchyTitle)")
                        Text("🐘 ＞ 🦁 ＞ 🐯 ＞ 🐆 ＞ 🐺 ＞ 🐶 ＞ 🐱 ＞ 🐭")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.blue)
                        ForEach(AnimalType.allCases) { type in
                            HStack(alignment: .top, spacing: 6) {
                                Text("\(type.emoji) \(lang.animalName(type)):")
                                    .font(.system(size: 12, weight: .black))
                                    .foregroundColor(.primary)
                                    .frame(width: 80, alignment: .leading)
                                Text(lang.animalEatRule(type))
                                    .font(.system(size: 11))
                                    .foregroundColor(.secondary)
                            }
                        }
                    }
                    Divider()
                    Group {
                        sectionTitle("⚡️ \(lang.ruleSpecialAbilitiesTitle)")
                        Text("• 🌊: \(lang.animalName(.rat)) - \(lang.animalEatRule(.rat))")
                        Text("• 🦁 / 🐯: \(lang.animalEatRule(.lion))")
                        Text("• \(lang.shieldSkillText(1))")
                        Text("• \(lang.frenzySkillText(1))")
                    }
                    Divider()
                    VStack(spacing: 4) {
                        Text(lang.appTitle)
                            .font(.system(size: 13, weight: .bold))
                            .foregroundColor(.primary)
                        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.4"
                        let buildNumber = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "1"
                        Text("v\(appVersion) (Build \(buildNumber))")
                            .font(.system(size: 11, weight: .medium, design: .monospaced))
                            .foregroundColor(.secondary)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.top, 8)
                }
                .font(.system(size: 13))
                .padding(18)
            }
            .navigationTitle(lang.ruleSheetTitle)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button(lang.closeBtn) { dismiss() }
                        .font(.system(size: 14, weight: .bold))
                }
            }
        }
    }
    
    private func sectionTitle(_ title: String) -> some View {
        Text(title)
            .font(.system(size: 15, weight: .bold))
            .foregroundColor(Color(red: 0.15, green: 0.38, blue: 0.15))
    }
}
