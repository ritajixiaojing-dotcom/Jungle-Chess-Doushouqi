//
//  JungleLanguageManager.swift
//  Jungle Chess Doushouqi
//
//  Created for Jungle Chess Doushouqi multi-language internationalization.
//

import SwiftUI
import Combine

enum JungleLanguage: String, CaseIterable, Identifiable {
    case zhHans = "zh-Hans" // 简体中文
    case zhHant = "zh-Hant" // 繁體中文
    case en = "en"           // English
    case ja = "ja"           // 日本語
    case ko = "ko"           // 한국어
    case es = "es"           // Español
    case fr = "fr"           // Français
    case de = "de"           // Deutsch
    
    var id: String { rawValue }
    
    var displayName: String {
        switch self {
        case .zhHans: return "🇨🇳 简体中文"
        case .zhHant: return "🇭🇰 繁體中文"
        case .en:     return "🇺🇸 English"
        case .ja:     return "🇯🇵 日本語"
        case .ko:     return "🇰🇷 한국어"
        case .es:     return "🇪🇸 Español"
        case .fr:     return "🇫🇷 Français"
        case .de:     return "🇩🇪 Deutsch"
        }
    }
    
    var flag: String {
        switch self {
        case .zhHans: return "🇨🇳"
        case .zhHant: return "🇭🇰"
        case .en:     return "🇺🇸"
        case .ja:     return "🇯🇵"
        case .ko:     return "🇰🇷"
        case .es:     return "🇪🇸"
        case .fr:     return "🇫🇷"
        case .de:     return "🇩🇪"
        }
    }
}

class JungleLanguageManager: ObservableObject {
    static let shared = JungleLanguageManager()
    
    private let manualOverrideKey = "app_user_has_manual_language_override_v1"
    private let languageKey = "app_selected_jungle_language_v1"
    
    @Published var isFollowingSystem: Bool = true
    @Published var currentLanguage: JungleLanguage {
        didSet {
            if !isFollowingSystem {
                UserDefaults.standard.set(currentLanguage.rawValue, forKey: languageKey)
            }
        }
    }
    
    private init() {
        // By default, detect and use the device's native language!
        if UserDefaults.standard.bool(forKey: manualOverrideKey),
           let saved = UserDefaults.standard.string(forKey: languageKey),
           let lang = JungleLanguage(rawValue: saved) {
            self.isFollowingSystem = false
            self.currentLanguage = lang
        } else {
            self.isFollowingSystem = true
            self.currentLanguage = JungleLanguageManager.detectSystemLanguage()
        }
        
        NotificationCenter.default.addObserver(
            forName: NSLocale.currentLocaleDidChangeNotification,
            object: nil,
            queue: .main
        ) { [weak self] _ in
            guard let self = self, self.isFollowingSystem else { return }
            self.currentLanguage = JungleLanguageManager.detectSystemLanguage()
        }
    }
    
    static func detectSystemLanguage() -> JungleLanguage {
        let preferred = Locale.preferredLanguages.first ?? "en"
        if preferred.hasPrefix("zh-Hant") || preferred.hasPrefix("zh-HK") || preferred.hasPrefix("zh-TW") {
            return .zhHant
        } else if preferred.hasPrefix("zh") {
            return .zhHans
        } else if preferred.hasPrefix("ja") {
            return .ja
        } else if preferred.hasPrefix("ko") {
            return .ko
        } else if preferred.hasPrefix("es") {
            return .es
        } else if preferred.hasPrefix("fr") {
            return .fr
        } else if preferred.hasPrefix("de") {
            return .de
        } else {
            return .en
        }
    }
    
    func setLanguage(_ lang: JungleLanguage) {
        withAnimation(.easeInOut(duration: 0.2)) {
            isFollowingSystem = false
            UserDefaults.standard.set(true, forKey: manualOverrideKey)
            UserDefaults.standard.set(lang.rawValue, forKey: languageKey)
            currentLanguage = lang
        }
    }
    
    func setFollowSystem() {
        withAnimation(.easeInOut(duration: 0.2)) {
            isFollowingSystem = true
            UserDefaults.standard.set(false, forKey: manualOverrideKey)
            UserDefaults.standard.removeObject(forKey: languageKey)
            currentLanguage = JungleLanguageManager.detectSystemLanguage()
        }
    }
    
    // MARK: - Localized Accessors
    
    var appTitle: String {
        switch currentLanguage {
        case .zhHans: return "快乐斗兽棋 · Jungle Chess"
        case .zhHant: return "快樂鬥獸棋 · Jungle Chess"
        case .en:     return "Jungle Chess"
        case .ja:     return "楽しい闘獣棋 (Jungle Chess)"
        case .ko:     return "행복한 투수기 (Jungle Chess)"
        case .es:     return "Ajedrez de la Selva (Jungle Chess)"
        case .fr:     return "Jeu des Animaux (Jungle Chess)"
        case .de:     return "Dschungelschach (Jungle Chess)"
        }
    }
    
    var systemDefaultText: String {
        switch currentLanguage {
        case .zhHans: return "🌐 跟随系统 (默认)"
        case .zhHant: return "🌐 跟隨系統 (預設)"
        case .en:     return "🌐 System Default"
        case .ja:     return "🌐 システム設定 (デフォルト)"
        case .ko:     return "🌐 시스템 기본값"
        case .es:     return "🌐 Idioma del Sistema"
        case .fr:     return "🌐 Par défaut du système"
        case .de:     return "🌐 Systemeinstellung"
        }
    }
    
    func playerName(_ player: Player) -> String {
        switch player {
        case .red:
            switch currentLanguage {
            case .zhHans: return "红方"
            case .zhHant: return "紅方"
            case .en:     return "Red"
            case .ja:     return "赤方"
            case .ko:     return "홍군"
            case .es:     return "Rojo"
            case .fr:     return "Rouge"
            case .de:     return "Rot"
            }
        case .blue:
            switch currentLanguage {
            case .zhHans: return "蓝方"
            case .zhHant: return "藍方"
            case .en:     return "Blue"
            case .ja:     return "青方"
            case .ko:     return "청군"
            case .es:     return "Azul"
            case .fr:     return "Bleu"
            case .de:     return "Blau"
            }
        }
    }
    
    func animalName(_ type: AnimalType) -> String {
        switch type {
        case .rat:
            switch currentLanguage {
            case .zhHans, .zhHant: return "鼠"
            case .en: return "Rat"
            case .ja: return "鼠"
            case .ko: return "쥐"
            case .es: return "Rata"
            case .fr: return "Rat"
            case .de: return "Ratte"
            }
        case .cat:
            switch currentLanguage {
            case .zhHans: return "猫"
            case .zhHant: return "貓"
            case .en: return "Cat"
            case .ja: return "猫"
            case .ko: return "고양이"
            case .es: return "Gato"
            case .fr: return "Chat"
            case .de: return "Katze"
            }
        case .dog:
            switch currentLanguage {
            case .zhHans, .zhHant: return "狗"
            case .en: return "Dog"
            case .ja: return "犬"
            case .ko: return "개"
            case .es: return "Perro"
            case .fr: return "Chien"
            case .de: return "Hund"
            }
        case .wolf:
            switch currentLanguage {
            case .zhHans, .zhHant: return "狼"
            case .en: return "Wolf"
            case .ja: return "狼"
            case .ko: return "늑대"
            case .es: return "Lobo"
            case .fr: return "Loup"
            case .de: return "Wolf"
            }
        case .leopard:
            switch currentLanguage {
            case .zhHans, .zhHant: return "豹"
            case .en: return "Leopard"
            case .ja: return "豹"
            case .ko: return "표범"
            case .es: return "Leopardo"
            case .fr: return "Léopard"
            case .de: return "Leopard"
            }
        case .tiger:
            switch currentLanguage {
            case .zhHans, .zhHant: return "虎"
            case .en: return "Tiger"
            case .ja: return "虎"
            case .ko: return "호랑이"
            case .es: return "Tigre"
            case .fr: return "Tigre"
            case .de: return "Tiger"
            }
        case .lion:
            switch currentLanguage {
            case .zhHans: return "狮"
            case .zhHant: return "獅"
            case .en: return "Lion"
            case .ja: return "獅"
            case .ko: return "사자"
            case .es: return "León"
            case .fr: return "Lion"
            case .de: return "Löwe"
            }
        case .elephant:
            switch currentLanguage {
            case .zhHans, .zhHant: return "象"
            case .en: return "Elephant"
            case .ja: return "象"
            case .ko: return "코끼리"
            case .es: return "Elefante"
            case .fr: return "Éléphant"
            case .de: return "Elefant"
            }
        }
    }
    
    func animalEatRule(_ type: AnimalType) -> String {
        switch type {
        case .elephant:
            switch currentLanguage {
            case .zhHans: return "能吃象及以下(🐘🦁🐯🐆🐺🐶🐱，除鼠外)；同级可互吃"
            case .zhHant: return "能吃象及以下(🐘🦁🐯🐆🐺🐶🐱，除鼠外)；同級可互吃"
            case .en: return "Captures Elephant & below (except Rat); captures equal rank"
            case .ja: return "象以下を捕獲可能(ネズミ以外)；同格は互いに捕獲可"
            case .ko: return "코끼리 이하 포획(쥐 제외)；동급 상호 포획 가능"
            case .es: return "Come Elefante e inferiores (excepto Rata); come mismo rango"
            case .fr: return "Capture Éléphant et inférieurs (sauf Rat); même rang"
            case .de: return "Schlägt Elefant und schwächer (außer Ratte); gleicher Rang"
            }
        case .lion:
            switch currentLanguage {
            case .zhHans: return "能吃狮及以下(🦁🐯🐆🐺🐶🐱🐭)；可飞跃小河；同级可互吃"
            case .zhHant: return "能吃獅及以下(🦁🐯🐆🐺🐶🐱🐭)；可飛躍小河；同級可互吃"
            case .en: return "Captures Lion & below; jumps across river; captures equal rank"
            case .ja: return "獅子以下を捕獲；川を飛び越え可能；同格捕獲可"
            case .ko: return "사자 이하 포획；강을 뛰어넘음；동급 포획 가능"
            case .es: return "Come León e inferiores; salta el río; come mismo rango"
            case .fr: return "Capture Lion et inférieurs; saute la rivière; même rang"
            case .de: return "Schlägt Löwe und schwächer; springt über Fluss; gleicher Rang"
            }
        case .tiger:
            switch currentLanguage {
            case .zhHans: return "能吃虎及以下(🐯🐆🐺🐶🐱🐭)；可飞跃小河；同级可互吃"
            case .zhHant: return "能吃虎及以下(🐯🐆🐺🐶🐱🐭)；可飛躍小河；同級可互吃"
            case .en: return "Captures Tiger & below; jumps across river; captures equal rank"
            case .ja: return "虎以下を捕獲；川を飛び越え可能；同格捕獲可"
            case .ko: return "호랑이 이하 포획；강을 뛰어넘음；동급 포획 가능"
            case .es: return "Come Tigre e inferiores; salta el río; come mismo rango"
            case .fr: return "Capture Tigre et inférieurs; saute la rivière; même rang"
            case .de: return "Schlägt Tiger und schwächer; springt über Fluss; gleicher Rang"
            }
        case .leopard:
            switch currentLanguage {
            case .zhHans: return "能吃豹及以下(🐆🐺🐶🐱🐭)；同级可互吃(先手必胜)"
            case .zhHant: return "能吃豹及以下(🐆🐺🐶🐱🐭)；同級可互吃(先手必勝)"
            case .en: return "Captures Leopard & below; captures equal rank"
            case .ja: return "豹以下を捕獲；同格は先手必勝"
            case .ko: return "표범 이하 포획；동급 선공 유리"
            case .es: return "Come Leopardo e inferiores; come mismo rango"
            case .fr: return "Capture Léopard et inférieurs; même rang"
            case .de: return "Schlägt Leopard und schwächer; gleicher Rang"
            }
        case .wolf:
            switch currentLanguage {
            case .zhHans: return "能吃狼及以下(🐺🐶🐱🐭)；同级可互吃(先手必胜)"
            case .zhHant: return "能吃狼及以下(🐺🐶🐱🐭)；同級可互吃(先手必勝)"
            case .en: return "Captures Wolf & below; captures equal rank"
            case .ja: return "狼以下を捕獲；同格は先手必勝"
            case .ko: return "늑대 이하 포획；동급 선공 유리"
            case .es: return "Come Lobo e inferiores; come mismo rango"
            case .fr: return "Capture Loup et inférieurs; même rang"
            case .de: return "Schlägt Wolf und schwächer; gleicher Rang"
            }
        case .dog:
            switch currentLanguage {
            case .zhHans: return "能吃狗及以下(🐶🐱🐭)；同级可互吃(先手必胜)"
            case .zhHant: return "能吃狗及以下(🐶🐱🐭)；同級可互吃(先手必勝)"
            case .en: return "Captures Dog & below; captures equal rank"
            case .ja: return "犬以下を捕獲；同格は先手必勝"
            case .ko: return "개 이하 포획；동급 선공 유리"
            case .es: return "Come Perro e inferiores; come mismo rango"
            case .fr: return "Capture Chien et inférieurs; même rang"
            case .de: return "Schlägt Hund und schwächer; gleicher Rang"
            }
        case .cat:
            switch currentLanguage {
            case .zhHans: return "能吃猫及以下(🐱🐭)；同级可互吃(先手必胜)"
            case .zhHant: return "能吃貓及以下(🐱🐭)；同級可互吃(先手必勝)"
            case .en: return "Captures Cat & Rat; captures equal rank"
            case .ja: return "猫と鼠を捕獲；同格は先手必勝"
            case .ko: return "고양이와 쥐 포획；동급 선공 유리"
            case .es: return "Come Gato y Rata; come mismo rango"
            case .fr: return "Capture Chat et Rat; même rang"
            case .de: return "Schlägt Katze und Ratte; gleicher Rang"
            }
        case .rat:
            switch currentLanguage {
            case .zhHans: return "⭐克制大象(🐘)！能吃同级鼠；唯一能下水游泳"
            case .zhHant: return "⭐克制大象(🐘)！能吃同級鼠；唯一能下水游泳"
            case .en: return "⭐ Beats Elephant (🐘)! Can swim in river"
            case .ja: return "⭐ 象(🐘)に勝てる！唯一川を泳げる特権"
            case .ko: return "⭐ 코끼리(🐘)를 이김! 유일하게 강을 헤엄침"
            case .es: return "⭐ ¡Vence al Elefante (🐘)! El único que nada"
            case .fr: return "⭐ Bat l'Éléphant (🐘) ! Le seul qui nage"
            case .de: return "⭐ Besiegt Elefant (🐘)! Einziges Schwimmtier"
            }
        }
    }
    
    func difficultyName(_ diff: AIDifficulty) -> String {
        switch diff {
        case .easy:
            switch currentLanguage {
            case .zhHans: return "🌱 轻松入门"
            case .zhHant: return "🌱 輕鬆入門"
            case .en:     return "🌱 Casual"
            case .ja:     return "🌱 初級"
            case .ko:     return "🌱 초급"
            case .es:     return "🌱 Fácil"
            case .fr:     return "🌱 Facile"
            case .de:     return "🌱 Leicht"
            }
        case .normal:
            switch currentLanguage {
            case .zhHans: return "🌿 棋艺进阶"
            case .zhHant: return "🌿 棋藝進階"
            case .en:     return "🌿 Balanced"
            case .ja:     return "🌿 中級"
            case .ko:     return "🌿 중급"
            case .es:     return "🌿 Medio"
            case .fr:     return "🌿 Moyen"
            case .de:     return "🌿 Normal"
            }
        case .hard:
            switch currentLanguage {
            case .zhHans: return "👑 智谋大师"
            case .zhHant: return "👑 智謀大師"
            case .en:     return "👑 Master"
            case .ja:     return "👑 上級"
            case .ko:     return "👑 상급"
            case .es:     return "👑 Difícil"
            case .fr:     return "👑 Difficile"
            case .de:     return "👑 Schwer"
            }
        }
    }
    
    func modeName(isVsAI: Bool) -> String {
        if isVsAI {
            switch currentLanguage {
            case .zhHans: return "👤 人机对决"
            case .zhHant: return "👤 人機對決"
            case .en:     return "👤 vs AI"
            case .ja:     return "👤 AI対戦"
            case .ko:     return "👤 AI 대전"
            case .es:     return "👤 vs IA"
            case .fr:     return "👤 vs IA"
            case .de:     return "👤 Gegen KI"
            }
        } else {
            switch currentLanguage {
            case .zhHans: return "👥 双人同屏"
            case .zhHant: return "👥 雙人同屏"
            case .en:     return "👥 2 Players"
            case .ja:     return "👥 二人対戦"
            case .ko:     return "👥 2인 대전"
            case .es:     return "👥 2 Jugadores"
            case .fr:     return "👥 2 Joueurs"
            case .de:     return "👥 2 Spieler"
            }
        }
    }
    
    var moveForMeBtn: String {
        switch currentLanguage {
        case .zhHans: return "🚀 帮我走这步\n(破局)"
        case .zhHant: return "🚀 幫我走這步\n(破局)"
        case .en:     return "🚀 Smart Move\n(Hint)"
        case .ja:     return "🚀 AIアシスト\n(推薦手)"
        case .ko:     return "🚀 추천 수\n(힌트)"
        case .es:     return "🚀 Sugerencia\n(Movimiento)"
        case .fr:     return "🚀 Coup Clé\n(Conseil)"
        case .de:     return "🚀 Zugtipp\n(Bester Zug)"
        }
    }
    
    var undoBtn: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "悔棋"
        case .en: return "Undo"
        case .ja: return "待った"
        case .ko: return "무르기"
        case .es: return "Deshacer"
        case .fr: return "Annuler"
        case .de: return "Zurück"
        }
    }
    
    var restartBtn: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "重来"
        case .en: return "Restart"
        case .ja: return "やり直し"
        case .ko: return "다시하기"
        case .es: return "Reiniciar"
        case .fr: return "Recommencer"
        case .de: return "Neustart"
        }
    }
    
    var rulePromptHint: String {
        switch currentLanguage {
        case .zhHans: return "📖 点击棋盘上任意动物，实时查看它能吃谁、怎么走。"
        case .zhHant: return "📖 點擊棋盤上任意動物，實時查看它能吃誰、怎麼走。"
        case .en:     return "📖 Tap any piece to view moves & targets."
        case .ja:     return "📖 盤上の駒をタップすると、進路と捕獲対象を確認できます。"
        case .ko:     return "📖 말 선택 시 이동 가능한 위치와 규칙을 확인합니다."
        case .es:     return "📖 Toca una pieza para ver movimientos y capturas."
        case .fr:     return "📖 Touchez une pièce pour voir mouvements et cibles."
        case .de:     return "📖 Figur antippen, um Züge und Schlagziele zu sehen."
        }
    }
    
    var redTrapLabel: String {
        switch currentLanguage {
        case .zhHans: return "红陷"
        case .zhHant: return "紅陷"
        case .en:     return "Trap"
        case .ja:     return "赤罠"
        case .ko:     return "함정"
        case .es:     return "Trampa"
        case .fr:     return "Piège"
        case .de:     return "Falle"
        }
    }
    
    var blueTrapLabel: String {
        switch currentLanguage {
        case .zhHans: return "蓝陷"
        case .zhHant: return "藍陷"
        case .en:     return "Trap"
        case .ja:     return "青罠"
        case .ko:     return "함정"
        case .es:     return "Trampa"
        case .fr:     return "Piège"
        case .de:     return "Falle"
        }
    }
    
    var redDenLabel: String {
        switch currentLanguage {
        case .zhHans: return "红穴"
        case .zhHant: return "紅穴"
        case .en:     return "Den"
        case .ja:     return "赤城"
        case .ko:     return "진영"
        case .es:     return "Guarida"
        case .fr:     return "Tanière"
        case .de:     return "Höhle"
        }
    }
    
    var blueDenLabel: String {
        switch currentLanguage {
        case .zhHans: return "蓝穴"
        case .zhHant: return "藍穴"
        case .en:     return "Den"
        case .ja:     return "青城"
        case .ko:     return "진영"
        case .es:     return "Guarida"
        case .fr:     return "Tanière"
        case .de:     return "Höhle"
        }
    }
    
    func shieldSkillText(_ count: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "🛡️ 神兽天降守穴 (\(count))"
        case .zhHant: return "🛡️ 神獸天降守穴 (\(count))"
        case .en:     return "🛡️ Guardian Teleport (\(count))"
        case .ja:     return "🛡️ 神獣防衛ワープ (\(count))"
        case .ko:     return "🛡️ 수호신 소환 (\(count))"
        case .es:     return "🛡️ Teletransporte Guardián (\(count))"
        case .fr:     return "🛡️ Téléportation Défensive (\(count))"
        case .de:     return "🛡️ Wächter-Teleport (\(count))"
        }
    }
    
    func frenzySkillText(_ count: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "🔥 狂暴越级 (\(count))"
        case .zhHant: return "🔥 狂暴越級 (\(count))"
        case .en:     return "🔥 Beast Frenzy (\(count))"
        case .ja:     return "🔥 猛獣バーサーク (\(count))"
        case .ko:     return "🔥 맹수 광폭화 (\(count))"
        case .es:     return "🔥 Caza Frenética (\(count))"
        case .fr:     return "🔥 Chasse Frénétique (\(count))"
        case .de:     return "🔥 Raserei-Jagd (\(count))"
        }
    }
    
    var victoryTitle: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🎉 战斗胜利！"
        case .en: return "🎉 Victory!"
        case .ja: return "🎉 勝利！"
        case .ko: return "🎉 승리!"
        case .es: return "🎉 ¡Victoria!"
        case .fr: return "🎉 Victoire !"
        case .de: return "🎉 Sieg!"
        }
    }
    
    var defeatTitle: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "💔 遗憾落败"
        case .en: return "💔 Defeat"
        case .ja: return "💔 敗北"
        case .ko: return "💔 패배"
        case .es: return "💔 Derrota"
        case .fr: return "💔 Défaite"
        case .de: return "💔 Niederlage"
        }
    }
    
    var playAgainBtn: String {
        switch currentLanguage {
        case .zhHans: return "再来一局"
        case .zhHant: return "再來一局"
        case .en: return "Play Again"
        case .ja: return "もう一度"
        case .ko: return "다시 대국"
        case .es: return "Jugar otra vez"
        case .fr: return "Rejouer"
        case .de: return "Nochmal spielen"
        }
    }
    
    var ruleSheetTitle: String {
        switch currentLanguage {
        case .zhHans: return "📖 斗兽棋经典对弈规则"
        case .zhHant: return "📖 鬥獸棋經典對弈規則"
        case .en:     return "📖 Jungle Chess Rules"
        case .ja:     return "📖 闘獣棋の遊び方とルール"
        case .ko:     return "📖 투수기 대국 규칙"
        case .es:     return "📖 Reglas de Ajedrez de la Selva"
        case .fr:     return "📖 Règles du Combat des Animaux"
        case .de:     return "📖 Dschungelschach Regeln"
        }
    }
    
    var ruleHierarchyTitle: String {
        switch currentLanguage {
        case .zhHans: return "1. 动物食物链等级 (从大到小)"
        case .zhHant: return "1. 動物食物鏈等級 (從大到小)"
        case .en:     return "1. Animal Rank Hierarchy (Highest to Lowest)"
        case .ja:     return "1. 動物の強さ序列 (強い順)"
        case .ko:     return "1. 동물 서열 계급 (높은 순)"
        case .es:     return "1. Jerarquía de Animales (Mayor a Menor)"
        case .fr:     return "1. Hiérarchie des Animaux (Plus Fort au Plus Faible)"
        case .de:     return "1. Tier-Rangfolge (Stärkste zuerst)"
        }
    }
    
    var ruleSpecialAbilitiesTitle: String {
        switch currentLanguage {
        case .zhHans: return "2. 特殊地形与特殊能力"
        case .zhHant: return "2. 特殊地形與特殊能力"
        case .en:     return "2. Special Terrain & Abilities"
        case .ja:     return "2. 特殊地形と特殊能力"
        case .ko:     return "2. 특수 지형 및 특수 능력"
        case .es:     return "2. Terrenos y Habilidades Especiales"
        case .fr:     return "2. Terrains et Capacités Spéciales"
        case .de:     return "2. Spezial-Terrain & Fähigkeiten"
        }
    }
    
    var ruleWinConditionTitle: String {
        switch currentLanguage {
        case .zhHans: return "3. 胜负判定条件"
        case .zhHant: return "3. 勝負判定條件"
        case .en:     return "3. Win / Loss Conditions"
        case .ja:     return "3. 勝敗の判定条件"
        case .ko:     return "3. 승패 결정 조건"
        case .es:     return "3. Condiciones de Victoria"
        case .fr:     return "3. Conditions de Victoire"
        case .de:     return "3. Siegbedingungen"
        }
    }
    
    var ruleWinConditionDesc: String {
        switch currentLanguage {
        case .zhHans: return "任一方动物进入对方兽穴（大本营）即直接获胜；或将对方全部动物吃光亦可获胜。"
        case .zhHant: return "任一方動物進入對方獸穴（大本營）即直接獲勝；或將對方全部動物吃光亦可獲勝。"
        case .en:     return "Entering the opponent's Den immediately wins the match; capturing all opposing pieces also wins."
        case .ja:     return "相手の獣穴(城)に入城するか、相手の全コマを捕獲すると勝利となります。"
        case .ko:     return "상대방의 진영(동굴)에 들어가거나 모든 적 말을 잡으면 승리합니다."
        case .es:     return "Entrar en la guarida enemiga o capturar todas las piezas rivales otorga la victoria inmediata."
        case .fr:     return "Entrer dans la tanière ennemie ou capturer toutes les pièces adverses assure la victoire immédiate."
        case .de:     return "Das Betreten der gegnerischen Höhle oder das Schlagen aller gegnerischen Figuren führt zum Sieg."
        }
    }
    
    var languagePickerTitle: String {
        switch currentLanguage {
        case .zhHans: return "🌐 切换语言"
        case .zhHant: return "🌐 切換語言"
        case .en:     return "🌐 Language"
        case .ja:     return "🌐 言語切替"
        case .ko:     return "🌐 언어 변경"
        case .es:     return "🌐 Idioma"
        case .fr:     return "🌐 Langue"
        case .de:     return "🌐 Sprache"
        }
    }
    
    var closeBtn: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "我知道了，开始对弈"
        case .en: return "Got it, Let's Play"
        case .ja: return "了解、対局を開始"
        case .ko: return "확인, 대국 시작"
        case .es: return "Entendido, ¡A jugar!"
        case .fr: return "Compris, Jouer"
        case .de: return "Verstanden, Los geht's"
        }
    }
    
    var highlightBtn: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "高亮显示"
        case .en: return "Highlight"
        case .ja: return "強調表示"
        case .ko: return "강조 표시"
        case .es: return "Resaltar"
        case .fr: return "Surbrillance"
        case .de: return "Hervorheben"
        }
    }
    
    var depth3DeductionTitle: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🔮 3步深度推演:"
        case .en: return "🔮 3-Step Forecast:"
        case .ja: return "🔮 3手読み予測:"
        case .ko: return "🔮 3수 수읽기 예측:"
        case .es: return "🔮 Predicción a 3 pasos:"
        case .fr: return "🔮 Prévision à 3 coups :"
        case .de: return "🔮 3-Züge Vorschau:"
        }
    }
    
    var deducingBestMove: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "正在推演全局最优路线..."
        case .en: return "Calculating optimal move..."
        case .ja: return "最適手を分析中..."
        case .ko: return "최적의 수를 계산하는 중..."
        case .es: return "Calculando la mejor jugada..."
        case .fr: return "Calcul du meilleur coup en cours..."
        case .de: return "Berechne optimalen Zug..."
        }
    }
    
    var frenzyBannerText: String {
        switch currentLanguage {
        case .zhHans: return "🔥【全员狂暴已开启】：2回合内你的所有动物可越级吃掉任何红方强敌！"
        case .zhHant: return "🔥【全員狂暴已開啟】：2回合內你的所有動物可越級吃掉任何紅方強敵！"
        case .en:     return "🔥【Beast Frenzy Activated】: For 2 turns, all your pieces can capture higher-ranked opponents!"
        case .ja:     return "🔥【猛獣バーサーク発動】：2ターンの間、味方全駒が格上の敵を捕獲可能！"
        case .ko:     return "🔥【광폭화 발동】：2턴 동안 모든 아군 말이 상위 등급의 적을 잡을 수 있습니다!"
        case .es:     return "🔥【Furia Bestial Activada】: ¡Durante 2 turnos tus animales pueden comer rangos superiores!"
        case .fr:     return "🔥【Frénésie Animale Activée】: Pendant 2 tours, vos animaux capturent les rangs supérieurs !"
        case .de:     return "🔥【Raserei Aktiviert】: Für 2 Runden können deine Tiere stärkere Gegner schlagen!"
        }
    }
    
    func shieldBannerText(pieceName: String) -> String {
        switch currentLanguage {
        case .zhHans: return "🛡️【神兽天降】：【\(pieceName)】已瞬间传送回防大本营咽喉要道！"
        case .zhHant: return "🛡️【神獸天降】：【\(pieceName)】已瞬間傳送回防大本營咽喉要道！"
        case .en:     return "🛡️【Guardian Teleport】: [\(pieceName)] teleported to defend the home den choke point!"
        case .ja:     return "🛡️【神獣防衛ワープ】：[\(pieceName)]が自陣獣穴の要所へ瞬間移動しました！"
        case .ko:     return "🛡️【수호신 소환】：[\(pieceName)] 말이 진영 요충지로 순간이동했습니다!"
        case .es:     return "🛡️【Teletransporte Guardián】: ¡[\(pieceName)] se ha teletransportado para defender la guarida!"
        case .fr:     return "🛡️【Téléportation Défensive】: [\(pieceName)] téléporté pour défendre la tanière !"
        case .de:     return "🛡️【Wächter-Teleport】: [\(pieceName)] zum Schutz der Höhle teleportiert!"
        }
    }
    
    var eatElephantBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "吃大象"
        case .en: return "Eat Elephant"
        case .ja: return "象を捕獲"
        case .ko: return "코끼리 잡기"
        case .es: return "Comer Elefante"
        case .fr: return "Capturer Éléphant"
        case .de: return "Elefant schlagen"
        }
    }
    
    var protectElephantBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "灭鼠护象"
        case .en: return "Hunt Rat"
        case .ja: return "鼠退治"
        case .ko: return "쥐 퇴치"
        case .es: return "Cazar Rata"
        case .fr: return "Chasser le Rat"
        case .de: return "Ratte jagen"
        }
    }
    
    func captureTargetBadge(_ type: AnimalType) -> String {
        let name = animalName(type)
        switch currentLanguage {
        case .zhHans, .zhHant: return "吃\(name)"
        case .en: return "Eat \(name)"
        case .ja: return "\(name)捕獲"
        case .ko: return "\(name) 잡기"
        case .es: return "Comer \(name)"
        case .fr: return "Capturer \(name)"
        case .de: return "\(name) schlagen"
        }
    }
    
    var jumpRiverBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "飞跃小河"
        case .en: return "Jump River"
        case .ja: return "渡河ジャンプ"
        case .ko: return "강 뛰어넘기"
        case .es: return "Saltar Río"
        case .fr: return "Sauter Rivière"
        case .de: return "Flusssprung"
        }
    }
    
    var winGameBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "制胜赢"
        case .en: return "Checkmate"
        case .ja: return "詰み・勝利"
        case .ko: return "결정타 승리"
        case .es: return "Victoria"
        case .fr: return "Victoire"
        case .de: return "Siegzug"
        }
    }
    
    func doctrineName(_ doctrine: StrategicDoctrine) -> String {
        switch doctrine {
        case .lionBlitz:
            switch currentLanguage {
            case .zhHans: return "🦁 狮王飞河突击"
            case .zhHant: return "🦁 獅王飛河突擊"
            case .en:     return "🦁 Lion River Blitz"
            case .ja:     return "🦁 獅子渡河電撃戦"
            case .ko:     return "🦁 사자 도하 돌격"
            case .es:     return "🦁 Asalto de León"
            case .fr:     return "🦁 Assaut du Lion"
            case .de:     return "🦁 Löwen-Flussblitz"
            }
        case .elephantTank:
            switch currentLanguage {
            case .zhHans: return "🐘 象王中军平推"
            case .zhHant: return "🐘 象王中軍平推"
            case .en:     return "🐘 Elephant Advance"
            case .ja:     return "🐘 巨象中央突破"
            case .ko:     return "🐘 코끼리 정면 진격"
            case .es:     return "🐘 Avance de Elefante"
            case .fr:     return "🐘 Poussée de l'Éléphant"
            case .de:     return "🐘 Elefanten-Vormarsch"
            }
        case .ratStealth:
            switch currentLanguage {
            case .zhHans: return "🐭 水鬼潜行偷家"
            case .zhHant: return "🐭 水鬼潛行偷家"
            case .en:     return "🐭 Rat River Infiltration"
            case .ja:     return "🐭 鼠の水中奇襲"
            case .ko:     return "🐭 쥐 수중 기습"
            case .es:     return "🐭 Infiltración de Rata"
            case .fr:     return "🐭 Infiltration du Rat"
            case .de:     return "🐭 Ratten-Infiltration"
            }
        case .packHunt:
            switch currentLanguage {
            case .zhHans: return "🐺 狼豹多路包抄"
            case .zhHant: return "🐺 狼豹多路包抄"
            case .en:     return "🐺 Wolf & Leopard Flank"
            case .ja:     return "🐺 狼豹包囲網"
            case .ko:     return "🐺 늑대 표범 협공"
            case .es:     return "🐺 Flanqueo de Lobos"
            case .fr:     return "🐺 Attaque en Meute"
            case .de:     return "🐺 Wolf-Leopard-Flanken"
            }
        }
    }
    
    var recommendedMoveBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🎯 推荐走这"
        case .en: return "🎯 Best Move"
        case .ja: return "🎯 おすすめ"
        case .ko: return "🎯 추천 수"
        case .es: return "🎯 Mejor Jugada"
        case .fr: return "🎯 Meilleur Coup"
        case .de: return "🎯 Bester Zug"
        }
    }
    
    func recommendedMoveHeadline(_ type: AnimalType) -> String {
        let name = animalName(type)
        switch currentLanguage {
        case .zhHans: return "👉 推荐进攻: 移动【\(name)】"
        case .zhHant: return "👉 推薦進攻: 移動【\(name)】"
        case .en:     return "👉 Best Move: Advance [\(name)]"
        case .ja:     return "👉 推奨手: [\(name)]を進める"
        case .ko:     return "👉 추천 수: [\(name)] 이동"
        case .es:     return "👉 Ataque recomendado: Mover [\(name)]"
        case .fr:     return "👉 Coup recommandé : Avancer [\(name)]"
        case .de:     return "👉 Empfohlener Zug: [\(name)] vorrücken"
        }
    }
    
    var recommendedMoveDetail: String {
        switch currentLanguage {
        case .zhHans: return "向前推进阵型，压制红方！"
        case .zhHant: return "向前推進陣型，壓制紅方！"
        case .en:     return "Advance pieces to maintain pressure!"
        case .ja:     return "前進して陣形を押し上げ、有利を拡大！"
        case .ko:     return "진형을 전진시켜 상대를 압박하세요!"
        case .es:     return "¡Avanza la formación para presionar al rival!"
        case .fr:     return "Avancez la formation pour maintenir la pression !"
        case .de:     return "Formierung vorrücken und Druck aufbauen!"
        }
    }
    
    func forecastStep1(_ type: AnimalType) -> String {
        let name = animalName(type)
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(name)】向前推进"
        case .zhHant: return "1️⃣ 我方【\(name)】向前推進"
        case .en:     return "1️⃣ Advance [\(name)]"
        case .ja:     return "1️⃣ 味方 [\(name)] 前進"
        case .ko:     return "1️⃣ 아군 [\(name)] 전진"
        case .es:     return "1️⃣ Avanza [\(name)]"
        case .fr:     return "1️⃣ Avancer [\(name)]"
        case .de:     return "1️⃣ [\(name)] vorrücken"
        }
    }
    
    func forecastStep2(_ type: AnimalType?) -> String {
        if let type = type {
            let name = animalName(type)
            switch currentLanguage {
            case .zhHans: return "2️⃣ 敌方【\(name)】应对"
            case .zhHant: return "2️⃣ 敵方【\(name)】應對"
            case .en:     return "2️⃣ Opponent [\(name)] reacts"
            case .ja:     return "2️⃣ 敵方 [\(name)] 応戦"
            case .ko:     return "2️⃣ 적 [\(name)] 대응"
            case .es:     return "2️⃣ Rival [\(name)] responde"
            case .fr:     return "2️⃣ L'adversaire [\(name)] réagit"
            case .de:     return "2️⃣ Gegner [\(name)] reagiert"
            }
        } else {
            switch currentLanguage {
            case .zhHans: return "2️⃣ 敌方应对"
            case .zhHant: return "2️⃣ 敵方應對"
            case .en:     return "2️⃣ Opponent reacts"
            case .ja:     return "2️⃣ 敵方応戦"
            case .ko:     return "2️⃣ 적 대응"
            case .es:     return "2️⃣ Rival responde"
            case .fr:     return "2️⃣ L'adversaire réagit"
            case .de:     return "2️⃣ Gegner reagiert"
            }
        }
    }
    
    var forecastStep3: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "3️⃣ 我方掌控主动"
        case .en: return "3️⃣ Seize board control"
        case .ja: return "3️⃣ 主導権を確保"
        case .ko: return "3️⃣ 주도권 장악"
        case .es: return "3️⃣ Tomar la iniciativa"
        case .fr: return "3️⃣ Prendre l'initiative"
        case .de: return "3️⃣ Initiative sichern"
        }
    }
    
    // MARK: - 🎯 战术建议与进攻提示全套国际化

    var adviceWinHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🏆 锁定胜局！直接入穴！"
        case .zhHant: return "🏆 鎖定勝局！直接入穴！"
        case .en:     return "🏆 Winning Move!"
        case .ja:     return "🏆 王手・決勝の一手！"
        case .ko:     return "🏆 승리의 결정타!"
        case .es:     return "🏆 ¡Jugada Ganadora!"
        case .fr:     return "🏆 Coup Gagnant !"
        case .de:     return "🏆 Siegeszug!"
        }
    }

    var adviceWinDetail: String {
        switch currentLanguage {
        case .zhHans: return "大本营空虚！直接一步攻占红方兽穴，立刻赢得比赛！"
        case .zhHant: return "大本營空虛！直接一步攻佔紅方獸穴，立刻贏得比賽！"
        case .en:     return "Enemy Den is unprotected! Capture it now to win immediately!"
        case .ja:     return "敵の獣穴が空いています！今すぐ占領して勝利を掴みましょう！"
        case .ko:     return "적 진영이 비었습니다! 즉시 진입하여 승리하세요!"
        case .es:     return "¡La guarida enemiga está desprotegida! ¡Ocúpala para ganar ya!"
        case .fr:     return "La tanière ennemie est sans défense ! Capturez-la pour gagner !"
        case .de:     return "Gegnerische Höhle ist ungeschützt! Sofort betreten und gewinnen!"
        }
    }

    func adviceWinForecast(piece: String, winner: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】直插大本营 ➔ 🏆 \(winner)获胜！"
        case .zhHant: return "1️⃣ 我方【\(piece)】直插大本營 ➔ 🏆 \(winner)獲勝！"
        case .en:     return "1️⃣ [\(piece)] enters Den ➔ 🏆 \(winner) Wins!"
        case .ja:     return "1️⃣ 味方 [\(piece)] 敵城突入 ➔ 🏆 \(winner) 勝利！"
        case .ko:     return "1️⃣ 아군 [\(piece)] 진영 침투 ➔ 🏆 \(winner) 승리!"
        case .es:     return "1️⃣ [\(piece)] entra a guarida ➔ 🏆 ¡\(winner) Gana!"
        case .fr:     return "1️⃣ [\(piece)] pénètre dans la tanière ➔ 🏆 \(winner) Gagne !"
        case .de:     return "1️⃣ [\(piece)] zieht in Höhle ➔ 🏆 \(winner) Siegt!"
        }
    }

    var adviceWinBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🏆 占穴获胜"
        case .en: return "🏆 Win Game"
        case .ja: return "🏆 入城勝利"
        case .ko: return "🏆 진영 점령"
        case .es: return "🏆 Victoria"
        case .fr: return "🏆 Victoire"
        case .de: return "🏆 Höhlen-Sieg"
        }
    }

    var adviceCounterKillHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🚨【绝地反击·驱逐入侵者】"
        case .zhHant: return "🚨【絕地反擊·驅逐入侵者】"
        case .en:     return "🚨【Intercept & Defend Den】"
        case .ja:     return "🚨【迎撃！本陣の侵入者を撃破】"
        case .ko:     return "🚨【역습! 침입자 처치】"
        case .es:     return "🚨【¡Contraataque! Eliminar Invasor】"
        case .fr:     return "🚨【Contre-attaque ! Éliminer l'intrus】"
        case .de:     return "🚨【Konter! Eindringling abfangen】"
        }
    }

    func adviceCounterKillDetail(piece: String, inv: String) -> String {
        switch currentLanguage {
        case .zhHans: return "立刻用【\(piece)】将敌方【\(inv)】吃掉于大本营门前！"
        case .zhHant: return "立刻用【\(piece)】將敵方【\(inv)】吃掉於大本營門前！"
        case .en:     return "Use [\(piece)] to eliminate enemy [\(inv)] at your Den entrance!"
        case .ja:     return "今すぐ[\(piece)]で本陣前の敵[\(inv)]を撃破しましょう！"
        case .ko:     return "[\(piece)] 말로 본진 앞의 적 [\(inv)] 말을 즉시 처치하세요!"
        case .es:     return "¡Usa tu [\(piece)] para abatir al [\(inv)] frente a la guarida!"
        case .fr:     return "Utilisez [\(piece)] pour intercepter le [\(inv)] ennemi !"
        case .de:     return "Schlage gegnerischen [\(inv)] vor der Höhle mit [\(piece)]!"
        }
    }

    var adviceCounterKillBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🛡️ 驱敌保家"
        case .en: return "🛡️ Defend Den"
        case .ja: return "🛡️ 本陣防衛"
        case .ko: return "🛡️ 진영 수호"
        case .es: return "🛡️ Defender"
        case .fr: return "🛡️ Défendre"
        case .de: return "🛡️ Verteidigen"
        }
    }

    var adviceBlockGateHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🚨【誓死堵门·保卫大本营】"
        case .zhHant: return "🚨【誓死堵門·保衛大本營】"
        case .en:     return "🚨【Block Choke Point】"
        case .ja:     return "🚨【要所封鎖！本陣を死守】"
        case .ko:     return "🚨【길목 차단! 진영 방어】"
        case .es:     return "🚨【¡Bloquear Paso Clave!】"
        case .fr:     return "🚨【Bloquer l'accès au repaire】"
        case .de:     return "🚨【Engpass blockieren】"
        }
    }

    func adviceBlockGateDetail(piece: String, inv: String) -> String {
        switch currentLanguage {
        case .zhHans: return "敌方【\(inv)】直逼大本营！立刻移动【\(piece)】堵住要道！"
        case .zhHant: return "敵方【\(inv)】直逼大本營！立刻移動【\(piece)】堵住要道！"
        case .en:     return "Enemy [\(inv)] approaches Den! Move [\(piece)] to block the entrance!"
        case .ja:     return "敵[\(inv)]が本陣へ迫っています！[\(piece)]で要所を塞ぎましょう！"
        case .ko:     return "적 [\(inv)] 말이 접근 중입니다! [\(piece)] 말로 길목을 차단하세요!"
        case .es:     return "¡[\(inv)] se acerca! Mueve [\(piece)] para bloquear la entrada."
        case .fr:     return "L'ennemi [\(inv)] approche ! Déplacez [\(piece)] pour bloquer l'accès."
        case .de:     return "Gegnerischer [\(inv)] nähert sich! Mit [\(piece)] den Eingang sperren!"
        }
    }

    var adviceBlockGateBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🛡️ 堵门保家"
        case .en: return "🛡️ Block Den"
        case .ja: return "🛡️ 本陣死守"
        case .ko: return "🛡️ 입구 방어"
        case .es: return "🛡️ Bloquear"
        case .fr: return "🛡️ Bloquer"
        case .de: return "🛡️ Blockieren"
        }
    }

    var adviceFallBackHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🚨【全速回防·迎击敌军】"
        case .zhHant: return "🚨【全速回防·迎擊敵軍】"
        case .en:     return "🚨【Emergency Fallback】"
        case .ja:     return "🚨【全力救援！本陣へ急行】"
        case .ko:     return "🚨【긴급 회방! 진영 복귀】"
        case .es:     return "🚨【¡Regreso de Emergencia!】"
        case .fr:     return "🚨【Repli d'Urgence】"
        case .de:     return "🚨【Notfall-Rückzug】"
        }
    }

    func adviceFallBackDetail(piece: String, inv: String) -> String {
        switch currentLanguage {
        case .zhHans: return "大本营告急！立刻调动【\(piece)】全速回防阻截【\(inv)】！"
        case .zhHant: return "大本營告急！立刻調動【\(piece)】全速回防阻截【\(inv)】！"
        case .en:     return "Den in danger! Rush [\(piece)] back to intercept [\(inv)]!"
        case .ja:     return "本陣が危険です！[\(piece)]を急行させて[\(inv)]を阻止しましょう！"
        case .ko:     return "진영이 위태롭습니다! [\(piece)] 말을 이동시켜 [\(inv)] 말을 저지하세요!"
        case .es:     return "¡Guarida en peligro! Retrocede [\(piece)] para interceptar a [\(inv)]."
        case .fr:     return "Tanière en danger ! Rapatriez [\(piece)] pour intercepter [\(inv)]."
        case .de:     return "Höhle in Gefahr! Mit [\(piece)] zurückeilen und [\(inv)] abfangen!"
        }
    }

    var adviceFallBackBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🛡️ 紧急回防"
        case .en: return "🛡️ Fall Back"
        case .ja: return "🛡️ 緊急帰還"
        case .ko: return "🛡️ 긴급 방어"
        case .es: return "🛡️ Retroceder"
        case .fr: return "🛡️ Repli"
        case .de: return "🛡️ Rückzug"
        }
    }

    func adviceHomeDefenseForecast(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】紧急回防 ➔ 2️⃣ 敌方进攻受阻 ➔ 3️⃣ 稳固大本营"
        case .zhHant: return "1️⃣ 我方【\(piece)】緊急回防 ➔ 2️⃣ 敵方進攻受阻 ➔ 3️⃣ 穩固大本營"
        case .en:     return "1️⃣ [\(piece)] rushes back ➔ 2️⃣ Enemy attack stalled ➔ 3️⃣ Secure Den"
        case .ja:     return "1️⃣ 味方 [\(piece)] 救援 ➔ 2️⃣ 敵の進撃阻止 ➔ 3️⃣ 本陣防衛"
        case .ko:     return "1️⃣ 아군 [\(piece)] 복귀 ➔ 2️⃣ 적 공격 차단 ➔ 3️⃣ 진영 사수"
        case .es:     return "1️⃣ [\(piece)] retrocede ➔ 2️⃣ Ataque rival frenado ➔ 3️⃣ Guarida segura"
        case .fr:     return "1️⃣ [\(piece)] se replie ➔ 2️⃣ Attaque ennemie bloquée ➔ 3️⃣ Tanière sécurisée"
        case .de:     return "1️⃣ [\(piece)] eilt zurück ➔ 2️⃣ Angriff gestoppt ➔ 3️⃣ Höhle gesichert"
        }
    }

    var adviceRatSwimEvadeHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🌊【潜水避险·【鼠】遁入水中】"
        case .zhHant: return "🌊【潛水避險·【鼠】遁入水中】"
        case .en:     return "🌊【Dive into River·Safe Escape】"
        case .ja:     return "🌊【水中退避！川へ潜入】"
        case .ko:     return "🌊【잠수 도피! 강으로 입수】"
        case .es:     return "🌊【Bucear en Río·Escape Seguro】"
        case .fr:     return "🌊【Plongée dans la Rivière】"
        case .de:     return "🌊【Ins Wasser tauchen·Sicherer Rückzug】"
        }
    }

    var adviceRatSwimEvadeDetail: String {
        switch currentLanguage {
        case .zhHans: return "敌方强敌已贴身逼近！立刻跳入河中避险，陆地猛兽无法下水，绝对安全！"
        case .zhHant: return "敵方強敵已貼身逼近！立刻跳入河中避險，陸地猛獸無法下水，絕對安全！"
        case .en:     return "Threat close by! Jump into the river where land animals cannot follow!"
        case .ja:     return "敵が接近中！陸の猛獣が入れない川へ飛び込み、安全を確保しましょう！"
        case .ko:     return "적이 근접했습니다! 육지 맹수가 들어올 수 없는 강으로 피신하세요!"
        case .es:     return "¡Enemigo cerca! Salta al río donde las fieras terrestres no pueden entrar."
        case .fr:     return "Menace proche ! Sautez dans la rivière, les fauves ne peuvent pas nager !"
        case .de:     return "Gegner nah! Springe in den Fluss, Landtiere können nicht folgen!"
        }
    }

    var adviceRatSwimEvadeBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🌊 遁水避险"
        case .en: return "🌊 Dive River"
        case .ja: return "🌊 水中退避"
        case .ko: return "🌊 강으로 피신"
        case .es: return "🌊 Al Río"
        case .fr: return "🌊 Nager"
        case .de: return "🌊 Ins Wasser"
        }
    }

    var adviceRatSwimEvadeForecast: String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【鼠】跳入河中 ➔ 2️⃣ 敌兽无法追击 ➔ 3️⃣ 伺机渡河偷家"
        case .zhHant: return "1️⃣ 我方【鼠】跳入河中 ➔ 2️⃣ 敵獸無法追擊 ➔ 3️⃣ 伺機渡河偷家"
        case .en:     return "1️⃣ Rat dives into river ➔ 2️⃣ Beasts cannot follow ➔ 3️⃣ Flank enemy Den"
        case .ja:     return "1️⃣ 鼠が川へ潜入 ➔ 2️⃣ 敵追撃不能 ➔ 3️⃣ 対岸を伺う"
        case .ko:     return "1️⃣ 쥐 강 입수 ➔ 2️⃣ 적 추격 불가 ➔ 3️⃣ 빈집 기습 노림"
        case .es:     return "1️⃣ Rata salta al río ➔ 2️⃣ Fieras no pueden seguir ➔ 3️⃣ Infiltrar guarida"
        case .fr:     return "1️⃣ Le Rat plonge ➔ 2️⃣ Poursuite impossible ➔ 3️⃣ Contourner le repaire"
        case .de:     return "1️⃣ Ratte taucht ab ➔ 2️⃣ Gegner kann nicht folgen ➔ 3️⃣ Gegenangriff"
        }
    }

    var adviceRatFleeHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🚨【猛兽逼近·【鼠】撤离避险】"
        case .zhHant: return "🚨【猛獸逼近·【鼠】撤離避險】"
        case .en:     return "🚨【Beast Nearby·Rat Retreat】"
        case .ja:     return "🚨【猛獣接近！鼠の緊急回避】"
        case .ko:     return "🚨【맹수 접근! 쥐 긴급 후퇴】"
        case .es:     return "🚨【¡Fiera Cerca! Retirada de Rata】"
        case .fr:     return "🚨【Fauve proche ! Fuite du Rat】"
        case .de:     return "🚨【Raubtier nah! Ratte flieht】"
        }
    }

    var adviceRatFleeDetail: String {
        switch currentLanguage {
        case .zhHans: return "敌方强敌就在旁边！立刻向后/向小河方向撤退一步脱离危险！"
        case .zhHant: return "敵方強敵就在旁邊！立刻向後/向小河方向撤退一步脫離危險！"
        case .en:     return "Enemy predator is next to you! Step back towards the river to escape danger!"
        case .ja:     return "敵が隣接しています！川や後方へ一歩下がって危険を回避しましょう！"
        case .ko:     return "적 맹수가 바로 옆에 있습니다! 뒤나 강 방향으로 한 칸 물러나세요!"
        case .es:     return "¡Fiera enemiga al lado! Da un paso atrás o hacia el río para escapar."
        case .fr:     return "Un prédateur est adjacent ! Reculez vers la rivière pour vous sauver !"
        case .de:     return "Raubtier steht neben dir! Nach hinten oder zum Fluss ausweichen!"
        }
    }

    var adviceRatFleeBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🛡️ 老鼠快跑"
        case .en: return "🛡️ Rat Flee"
        case .ja: return "🛡️ 鼠逃走"
        case .ko: return "🛡️ 쥐 도망"
        case .es: return "🛡️ Escapar"
        case .fr: return "🛡️ Fuir"
        case .de: return "🛡️ Flucht"
        }
    }

    var adviceRatFleeForecast: String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【鼠】脱离危险区 ➔ 2️⃣ 避开强敌捕捉 ➔ 3️⃣ 保全关键神兽"
        case .zhHant: return "1️⃣ 我方【鼠】脫離危險區 ➔ 2️⃣ 避開強敵捕捉 ➔ 3️⃣ 保全關鍵神獸"
        case .en:     return "1️⃣ Rat leaves danger zone ➔ 2️⃣ Avoid predator ➔ 3️⃣ Keep piece safe"
        case .ja:     return "1️⃣ 鼠が危険域を脱出 ➔ 2️⃣ 強敵の攻撃回避 ➔ 3️⃣ 貴重な戦力を温存"
        case .ko:     return "1️⃣ 쥐 위험 구역 탈출 ➔ 2️⃣ 적 공격 회피 ➔ 3️⃣ 전력 보존"
        case .es:     return "1️⃣ Rata sale del peligro ➔ 2️⃣ Esquiva al cazador ➔ 3️⃣ Conserva la pieza"
        case .fr:     return "1️⃣ Le Rat s'échappe ➔ 2️⃣ Évite le prédateur ➔ 3️⃣ Préserve la pièce"
        case .de:     return "1️⃣ Ratte entkommt ➔ 2️⃣ Schlag abgewendet ➔ 3️⃣ Figur gerettet"
        }
    }

    func advicePieceRetreatHeadline(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "🚨【紧急避险·【\(piece)】快撤】"
        case .zhHant: return "🚨【緊急避險·【\(piece)】快撤】"
        case .en:     return "🚨【Tactical Retreat: [\(piece)]】"
        case .ja:     return "🚨【緊急退避！[\(piece)]を引く】"
        case .ko:     return "🚨【긴급 후퇴: [\(piece)] 피신】"
        case .es:     return "🚨【Retirada Táctica: [\(piece)]】"
        case .fr:     return "🚨【Retraite Tactique : [\(piece)]】"
        case .de:     return "🚨【Taktischer Rückzug: [\(piece)]】"
        }
    }

    func advicePieceRetreatDetail(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "敌方天敌正虎视眈眈！立刻将【\(piece)】转移到安全格子避开锋芒！"
        case .zhHant: return "敵方天敵正虎視眈眈！立刻將【\(piece)】轉移到安全格子避開鋒芒！"
        case .en:     return "Enemy predators are lurking! Move [\(piece)] to a safe tile immediately!"
        case .ja:     return "敵が狙っています！今すぐ[\(piece)]を安全なマスへ退避させましょう！"
        case .ko:     return "상위 천적이 노리고 있습니다! [\(piece)] 말을 안전한 곳으로 이동하세요!"
        case .es:     return "¡Te acecha un enemigo superior! Mueve [\(piece)] a una casilla segura."
        case .fr:     return "Un prédateur vous guette ! Mettez [\(piece)] en sécurité immédiatement."
        case .de:     return "Stärkerer Gegner lauert! [\(piece)] sofort auf sicheres Feld ziehen!"
        }
    }

    var advicePieceRetreatBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🛡️ 紧急避险"
        case .en: return "🛡️ Evade Danger"
        case .ja: return "🛡️ 緊急退避"
        case .ko: return "🛡️ 위험 회피"
        case .es: return "🛡️ Esquivar"
        case .fr: return "🛡️ Éviter"
        case .de: return "🛡️ Ausweichen"
        }
    }

    func advicePieceRetreatForecast(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】避险撤退 ➔ 2️⃣ 敌方扑空 ➔ 3️⃣ 保留战力"
        case .zhHant: return "1️⃣ 我方【\(piece)】避險撤退 ➔ 2️⃣ 敵方撲空 ➔ 3️⃣ 保留戰力"
        case .en:     return "1️⃣ [\(piece)] retreats ➔ 2️⃣ Enemy strike misses ➔ 3️⃣ Preserve army"
        case .ja:     return "1️⃣ [\(piece)] 緊急退避 ➔ 2️⃣ 敵の攻撃空振り ➔ 3️⃣ 戦力維持"
        case .ko:     return "1️⃣ [\(piece)] 후퇴 ➔ 2️⃣ 적 공격 무산 ➔ 3️⃣ 전력 보존"
        case .es:     return "1️⃣ [\(piece)] se retira ➔ 2️⃣ Rival falla ataque ➔ 3️⃣ Conserva fuerza"
        case .fr:     return "1️⃣ [\(piece)] recule ➔ 2️⃣ Attaque adverse déjouée ➔ 3️⃣ Garder la force"
        case .de:     return "1️⃣ [\(piece)] weicht aus ➔ 2️⃣ Gegner schlägt ins Leere ➔ 3️⃣ Kraft bewahrt"
        }
    }

    var adviceHuntRiverRatHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🐭【水中猎鼠·打通航道】"
        case .zhHant: return "🐭【水中獵鼠·打通航道】"
        case .en:     return "🐭【Hunt River Rat·Clear Path】"
        case .ja:     return "🐭【水中戦！敵鼠を討ち航路開放】"
        case .ko:     return "🐭【수중전! 적 쥐 처치 및 도하로 개방】"
        case .es:     return "🐭【Cazar Rata en Agua·Abrir Paso】"
        case .fr:     return "🐭【Chasser le Rat d'Eau·Voie Libre】"
        case .de:     return "🐭【Flussratte schlagen·Weg frei】"
        }
    }

    var adviceHuntRiverRatDetail: String {
        switch currentLanguage {
        case .zhHans: return "敌方老鼠潜伏在水中封锁猛虎跳河！立刻在水中将其吃掉，全面打通飞河航道！"
        case .zhHant: return "敵方老鼠潛伏在水中封鎖猛虎跳河！立刻在水中將其吃掉，全面打通飛河航道！"
        case .en:     return "Enemy Rat blocks the river! Capture it in water to clear the river-jumping path!"
        case .ja:     return "敵鼠が水中で跳河を妨害しています！水中で撃破して渡河航路を開放しましょう！"
        case .ko:     return "적 쥐가 수중에서 호랑이의 도하를 막고 있습니다! 물속에서 잡아 도하로를 여세요!"
        case .es:     return "¡La rata rival bloquea el río! Captúrala en el agua para despejar el salto fluvial."
        case .fr:     return "Le rat ennemi bloque le saut ! Capturez-le dans l'eau pour libérer le passage."
        case .de:     return "Gegnerische Ratte blockiert den Sprung! Im Wasser schlagen und Weg freimachen!"
        }
    }

    var adviceHuntRiverRatBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🌊 破除水障"
        case .en: return "🌊 Clear River"
        case .ja: return "🌊 航路開放"
        case .ko: return "🌊 수로 개방"
        case .es: return "🌊 Despejar Río"
        case .fr: return "🌊 Voie Libre"
        case .de: return "🌊 Fluss frei"
        }
    }

    var adviceHuntRiverRatForecast: String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【鼠】水中吃掉敌鼠 ➔ 2️⃣ 解除跳河封锁 ➔ 3️⃣ 猛虎狮王飞河突破！"
        case .zhHant: return "1️⃣ 我方【鼠】水中吃掉敵鼠 ➔ 2️⃣ 解除跳河封鎖 ➔ 3️⃣ 猛虎獅王飛河突破！"
        case .en:     return "1️⃣ Rat captures enemy Rat ➔ 2️⃣ Unblock river jump ➔ 3️⃣ Tiger & Lion leap across!"
        case .ja:     return "1️⃣ 鼠が敵鼠を捕獲 ➔ 2️⃣ 跳河封鎖解除 ➔ 3️⃣ 獅子虎の渡河突撃！"
        case .ko:     return "1️⃣ 쥐가 적 쥐 포획 ➔ 2️⃣ 도하 봉쇄 해제 ➔ 3️⃣ 호랑이 사자 도하 기습!"
        case .es:     return "1️⃣ Rata captura rata rival ➔ 2️⃣ Desbloquea salto ➔ 3️⃣ ¡Tigre y León saltan el río!"
        case .fr:     return "1️⃣ Le Rat capture le rat rival ➔ 2️⃣ Voie libérée ➔ 3️⃣ Tigre et Lion sautent !"
        case .de:     return "1️⃣ Ratte schlägt Ratte ➔ 2️⃣ Flusssprung frei ➔ 3️⃣ Tiger & Löwe springen über!"
        }
    }

    var adviceUnderwaterPursuitHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🐭【水下潜行·合围水鬼】"
        case .zhHant: return "🐭【水下潛行·合圍水鬼】"
        case .en:     return "🐭【Underwater Stealth·Pursuit】"
        case .ja:     return "🐭【水中追撃！敵鼠包囲】"
        case .ko:     return "🐭【수중 추격! 적 쥐 포위】"
        case .es:     return "🐭【Persecución Submarina】"
        case .fr:     return "🐭【Traque Aquatique】"
        case .de:     return "🐭【Unterwasser-Verfolgung】"
        }
    }

    var adviceUnderwaterPursuitDetail: String {
        switch currentLanguage {
        case .zhHans: return "调动老鼠向水中敌鼠靠近，准备清除水障！"
        case .zhHant: return "調動老鼠向水中敵鼠靠近，準備清除水障！"
        case .en:     return "Move your Rat closer to the enemy Rat in the river to clear the waterway!"
        case .ja:     return "鼠を水中へ進め、敵鼠を追い詰めて川を開放する準備をしましょう！"
        case .ko:     return "쥐를 움직여 수중의 적 쥐를 압박하고 수로를 확보하세요!"
        case .es:     return "¡Acerca tu rata al enemigo en el agua para despejar el río!"
        case .fr:     return "Rapprochez votre Rat de l'adversaire pour libérer le fleuve !"
        case .de:     return "Ratte im Fluss näher an Gegner heranführen, um den Fluss zu befreien!"
        }
    }

    var adviceUnderwaterPursuitBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🌊 水下追击"
        case .en: return "🌊 River Pursuit"
        case .ja: return "🌊 水中追撃"
        case .ko: return "🌊 수중 추격"
        case .es: return "🌊 Perseguir"
        case .fr: return "🌊 Poursuite"
        case .de: return "🌊 Verfolgung"
        }
    }

    var adviceUnderwaterPursuitForecast: String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【鼠】下水进逼 ➔ 2️⃣ 逼退敌方水鬼 ➔ 3️⃣ 打通跳河路线"
        case .zhHant: return "1️⃣ 我方【鼠】下水進逼 ➔ 2️⃣ 逼退敵方水鬼 ➔ 3️⃣ 打通跳河路線"
        case .en:     return "1️⃣ Rat advances in water ➔ 2️⃣ Corner enemy Rat ➔ 3️⃣ Clear river jump lane"
        case .ja:     return "1️⃣ 鼠が水中へ前進 ➔ 2️⃣ 敵鼠を牽制 ➔ 3️⃣ 渡河ルート確保"
        case .ko:     return "1️⃣ 쥐 수중 전진 ➔ 2️⃣ 적 쥐 압박 ➔ 3️⃣ 도하로 확보"
        case .es:     return "1️⃣ Rata avanza en río ➔ 2️⃣ Acorrala a rival ➔ 3️⃣ Abre paso al salto"
        case .fr:     return "1️⃣ Le Rat avance dans l'eau ➔ 2️⃣ Bloque le rival ➔ 3️⃣ Prépare le saut"
        case .de:     return "1️⃣ Ratte rückt im Wasser vor ➔ 2️⃣ Gegner bedrängt ➔ 3️⃣ Sprungweg frei"
        }
    }

    var adviceProtectElephantHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🐱【天敌诛鼠·护卫神象】"
        case .zhHant: return "🐱【天敵誅鼠·護衛神象】"
        case .en:     return "🐱【Hunt Rat·Protect Elephant】"
        case .ja:     return "🐱【鼠退治！巨象を護衛】"
        case .ko:     return "🐱【쥐 퇴치! 코끼리 보호】"
        case .es:     return "🐱【Cazar Rata·Proteger Elefante】"
        case .fr:     return "🐱【Chasser le Rat·Protéger l'Éléphant】"
        case .de:     return "🐱【Ratte schlagen·Elefant schützen】"
        }
    }

    func adviceProtectElephantDetail(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "敌方老鼠逼近大象！立刻调动【\(piece)】将其吃掉，解除大象危机！"
        case .zhHant: return "敵方老鼠逼近大象！立刻調動【\(piece)】將其吃掉，解除大象危機！"
        case .en:     return "Enemy Rat threatens your Elephant! Use [\(piece)] to eliminate the Rat!"
        case .ja:     return "敵鼠が象に迫っています！[\(piece)]で捕獲して象の危機を救いましょう！"
        case .ko:     return "적 쥐가 코끼리를 노립니다! [\(piece)] 말로 즉시 처치하여 위기를 넘기세요!"
        case .es:     return "¡Rata enemiga cerca del Elefante! ¡Usa [\(piece)] para atraparla!"
        case .fr:     return "Le rat adverse menace l'Éléphant ! Utilisez [\(piece)] pour le neutraliser !"
        case .de:     return "Gegnerische Ratte bedroht den Elefanten! Mit [\(piece)] Ratte schlagen!"
        }
    }

    var adviceProtectElephantBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🐱 捕鼠护象"
        case .en: return "🐱 Guard Elephant"
        case .ja: return "🐱 鼠退治護衛"
        case .ko: return "🐱 코끼리 보호"
        case .es: return "🐱 Proteger Elefante"
        case .fr: return "🐱 Protéger l'Éléphant"
        case .de: return "🐱 Elefant beschützen"
        }
    }

    func adviceProtectElephantForecast(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】吃掉敌鼠 ➔ 2️⃣ 大象危机解除 ➔ 3️⃣ 大象势不可挡！"
        case .zhHant: return "1️⃣ 我方【\(piece)】吃掉敵鼠 ➔ 2️⃣ 大象危機解除 ➔ 3️⃣ 大象勢不可擋！"
        case .en:     return "1️⃣ [\(piece)] captures Rat ➔ 2️⃣ Elephant danger resolved ➔ 3️⃣ Elephant charges freely!"
        case .ja:     return "1️⃣ [\(piece)] 敵鼠捕獲 ➔ 2️⃣ 象の危機解除 ➔ 3️⃣ 巨象が猛進撃！"
        case .ko:     return "1️⃣ [\(piece)] 쥐 처치 ➔ 2️⃣ 코끼리 위기 해소 ➔ 3️⃣ 코끼리 거침없는 진격!"
        case .es:     return "1️⃣ [\(piece)] captura Rata ➔ 2️⃣ Elefante a salvo ➔ 3️⃣ ¡Elefante imparable!"
        case .fr:     return "1️⃣ [\(piece)] élimine le Rat ➔ 2️⃣ Danger écarté ➔ 3️⃣ L'Éléphant est inarrêtable !"
        case .de:     return "1️⃣ [\(piece)] schlägt Ratte ➔ 2️⃣ Gefahr gebannt ➔ 3️⃣ Elefant stürmt ungehindert vor!"
        }
    }

    var adviceElephantEscapeHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🐘【大象避险·紧急脱离】"
        case .zhHant: return "🐘【大象避險·緊急脫離】"
        case .en:     return "🐘【Elephant Evade·Flee Rat】"
        case .ja:     return "🐘【巨象脱出！鼠から退避】"
        case .ko:     return "🐘【코끼리 피신! 쥐로부터 탈출】"
        case .es:     return "🐘【Escape de Elefante·Evitar Rata】"
        case .fr:     return "🐘【Évasion de l'Éléphant】"
        case .de:     return "🐘【Elefant weicht Ratte aus】"
        }
    }

    var adviceElephantEscapeDetail: String {
        switch currentLanguage {
        case .zhHans: return "敌方老鼠近在咫尺！大象立刻退开脱离危险！"
        case .zhHant: return "敵方老鼠近在咫尺！大象立刻退開脫離危險！"
        case .en:     return "Enemy Rat is adjacent! Retreat Elephant immediately to avoid capture!"
        case .ja:     return "敵鼠が隣にいます！象を一歩引いて危険から逃れましょう！"
        case .ko:     return "적 쥐가 인접했습니다! 코끼리를 즉시 물러나게 하여 위기를 피하세요!"
        case .es:     return "¡La rata enemiga está al lado! ¡Mueve el Elefante para ponerlo a salvo!"
        case .fr:     return "Le rat adverse est adjacent ! Reculez l'Éléphant immédiatement !"
        case .de:     return "Ratte steht direkt daneben! Elefant sofort außer Reichweite ziehen!"
        }
    }

    var adviceElephantEscapeBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🐘 大象避险"
        case .en: return "🐘 Elephant Flee"
        case .ja: return "🐘 象退避"
        case .ko: return "🐘 코끼리 피신"
        case .es: return "🐘 Elefante a Salvo"
        case .fr: return "🐘 Éléphant Sauf"
        case .de: return "🐘 Elefant in Sicherheit"
        }
    }

    var adviceElephantEscapeForecast: String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 大象拉开身位 ➔ 2️⃣ 猫狗包抄 ➔ 3️⃣ 彻底歼灭敌鼠"
        case .zhHant: return "1️⃣ 大象拉開身位 ➔ 2️⃣ 貓狗包抄 ➔ 3️⃣ 徹底殲滅敵鼠"
        case .en:     return "1️⃣ Elephant steps back ➔ 2️⃣ Cat/Dog flank ➔ 3️⃣ Eliminate threat"
        case .ja:     return "1️⃣ 象が距離を取る ➔ 2️⃣ 猫犬で挟撃 ➔ 3️⃣ 敵鼠を完全殲滅"
        case .ko:     return "1️⃣ 코끼리 거리 벌림 ➔ 2️⃣ 고양이·개 협공 ➔ 3️⃣ 적 쥐 격멸"
        case .es:     return "1️⃣ Elefante se aleja ➔ 2️⃣ Gato/Perro flanquean ➔ 3️⃣ Eliminar rata"
        case .fr:     return "1️⃣ L'Éléphant prend distance ➔ 2️⃣ Chat/Chien encerclent ➔ 3️⃣ Neutraliser le rat"
        case .de:     return "1️⃣ Elefant gewinnt Distanz ➔ 2️⃣ Katze/Hund flankieren ➔ 3️⃣ Ratte ausschalten"
        }
    }

    var adviceFlankRatHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🐺【合围敌鼠·护卫大象】"
        case .zhHant: return "🐺【合圍敵鼠·護衛大象】"
        case .en:     return "🐺【Flank Rat·Protect Elephant】"
        case .ja:     return "🐺【敵鼠包囲！象を守護】"
        case .ko:     return "🐺【적 쥐 포위! 코끼리 호위】"
        case .es:     return "🐺【Acorralar Rata·Guardar Elefante】"
        case .fr:     return "🐺【Encercler le Rat·Garder l'Éléphant】"
        case .de:     return "🐺【Ratte umstellen·Elefant decken】"
        }
    }

    func adviceFlankRatDetail(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "调动【\(piece)】向敌方老鼠包抄，保护大象！"
        case .zhHant: return "調動【\(piece)】向敵方老鼠包抄，保護大象！"
        case .en:     return "Move [\(piece)] closer to pinch the enemy Rat and keep Elephant safe!"
        case .ja:     return "[\(piece)]で敵鼠に接近して包囲網を敷き、象を守りましょう！"
        case .ko:     return "[\(piece)] 말로 적 쥐를 압박하여 코끼리를 안전하게 지키세요!"
        case .es:     return "¡Mueve [\(piece)] para cortar el paso a la rata y blindar al Elefante!"
        case .fr:     return "Avancez [\(piece)] pour encercler le rat et sécuriser l'Éléphant !"
        case .de:     return "Mit [\(piece)] die Ratte stellen und den Elefanten absichern!"
        }
    }

    var adviceFlankRatBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🐺 包抄敌鼠"
        case .en: return "🐺 Flank Rat"
        case .ja: return "🐺 敵鼠包囲"
        case .ko: return "🐺 쥐 포위"
        case .es: return "🐺 Acorralar"
        case .fr: return "🐺 Encercler"
        case .de: return "🐺 Umstellen"
        }
    }

    func adviceFlankRatForecast(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】包抄敌鼠 ➔ 2️⃣ 逼退敌鼠 ➔ 3️⃣ 护卫神象"
        case .zhHant: return "1️⃣ 我方【\(piece)】包抄敵鼠 ➔ 2️⃣ 逼退敵鼠 ➔ 3️⃣ 護衛神象"
        case .en:     return "1️⃣ [\(piece)] corners Rat ➔ 2️⃣ Force Rat back ➔ 3️⃣ Guard Elephant"
        case .ja:     return "1️⃣ [\(piece)] 包囲網 ➔ 2️⃣ 敵鼠を後退させる ➔ 3️⃣ 象を護衛"
        case .ko:     return "1️⃣ [\(piece)] 포위 ➔ 2️⃣ 적 쥐 격퇴 ➔ 3️⃣ 코끼리 수호"
        case .es:     return "1️⃣ [\(piece)] acorrala a rata ➔ 2️⃣ Rata retrocede ➔ 3️⃣ Elefante a salvo"
        case .fr:     return "1️⃣ [\(piece)] cerne le rat ➔ 2️⃣ Fait fuir le rival ➔ 3️⃣ Éléphant protégé"
        case .de:     return "1️⃣ [\(piece)] bedrängt Ratte ➔ 2️⃣ Ratte weicht zurück ➔ 3️⃣ Elefant sicher"
        }
    }

    func adviceProfitableTradeHeadline(piece: String, cap: String, isRatElephant: Bool) -> String {
        if isRatElephant {
            switch currentLanguage {
            case .zhHans: return "⚡️【战略大赚·以鼠换象】"
            case .zhHant: return "⚡️【戰略大賺·以鼠換象】"
            case .en:     return "⚡️【High-Value Trade: Rat for Elephant!】"
            case .ja:     return "⚡️【超好手！鼠で敵巨象を相討ち】"
            case .ko:     return "⚡️【대이득! 쥐로 코끼리 교환】"
            case .es:     return "⚡️【¡Gran Canje: Rata por Elefante!】"
            case .fr:     return "⚡️【Échange Royal : Rat contre Éléphant !】"
            case .de:     return "⚡️【Genialer Tausch: Ratte gegen Elefant!】"
            }
        } else {
            switch currentLanguage {
            case .zhHans: return "⚡️【战略兑子·以\(piece)换\(cap)】"
            case .zhHant: return "⚡️【戰略兌子·以\(piece)換\(cap)】"
            case .en:     return "⚡️【Favorable Trade: [\(piece)] for [\(cap)]】"
            case .ja:     return "⚡️【有利な交換！[\(piece)]で[\(cap)]を取る】"
            case .ko:     return "⚡️【유리한 교환: [\(piece)]로 [\(cap)] 포획】"
            case .es:     return "⚡️【Intercambio Favorable: [\(piece)] por [\(cap)]】"
            case .fr:     return "⚡️【Échange Favorable : [\(piece)] contre [\(cap)]】"
            case .de:     return "⚡️【Günstiger Abtausch: [\(piece)] gegen [\(cap)]】"
            }
        }
    }

    func adviceProfitableTradeDetail(cap: String, netProfit: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "吃掉【\(cap)】后虽会被反击，但我方用小换大，净赚 +\(netProfit) 战力，血赚！"
        case .zhHant: return "吃掉【\(cap)】後雖會被反擊，但我方用小換大，淨賺 +\(netProfit) 戰力，血賺！"
        case .en:     return "Trading [\(cap)] will invite counter-attack, but you gain +\(netProfit) net army advantage!"
        case .ja:     return "[\(cap)]捕獲後に反撃を受けますが、戦力差 +\(netProfit) の大幅黒字交換です！"
        case .ko:     return "[\(cap)]을 잡은 후 반격을 받지만, 전투력 +\(netProfit) 이득인 훌륭한 교환입니다!"
        case .es:     return "¡Aunque contraataquen tras comer [\(cap)], ganas +\(netProfit) de ventaja táctica!"
        case .fr:     return "Capturer [\(cap)] provoquera une riposte, mais vous gagnez +\(netProfit) en valeur !"
        case .de:     return "Das Schlagen von [\(cap)] bringt Gegenangriff, aber +\(netProfit) taktischen Vorteil!"
        }
    }

    func adviceProfitableTradeBadge(netProfit: Int) -> String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🟣 净赚+\(netProfit)"
        case .en: return "🟣 Gain +\(netProfit)"
        case .ja: return "🟣 得+\(netProfit)"
        case .ko: return "🟣 이득+\(netProfit)"
        case .es: return "🟣 Gana +\(netProfit)"
        case .fr: return "🟣 Gain +\(netProfit)"
        case .de: return "🟣 Gewinn+\(netProfit)"
        }
    }

    var enemyStrongPiece: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "强敌"
        case .en: return "Enemy"
        case .ja: return "強敵"
        case .ko: return "적 강자"
        case .es: return "Fiera rival"
        case .fr: return "Fauve ennemi"
        case .de: return "Gegner"
        }
    }

    func adviceProfitableTradeForecast(piece: String, cap: String, capVal: Int, oppKiller: String, pieceVal: Int, netProfit: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】吃【\(cap)】(+\(capVal)) ➔ 2️⃣ 敌方【\(oppKiller)】吃【\(piece)】(-\(pieceVal)) ➔ 3️⃣ 我方净赚 +\(netProfit) 战力！"
        case .zhHant: return "1️⃣ 我方【\(piece)】吃【\(cap)】(+\(capVal)) ➔ 2️⃣ 敵方【\(oppKiller)】吃【\(piece)】(-\(pieceVal)) ➔ 3️⃣ 我方淨賺 +\(netProfit) 戰力！"
        case .en:     return "1️⃣ [\(piece)] eats [\(cap)] (+\(capVal)) ➔ 2️⃣ Opponent [\(oppKiller)] eats [\(piece)] (-\(pieceVal)) ➔ 3️⃣ Net gain +\(netProfit)!"
        case .ja:     return "1️⃣ [\(piece)]が[\(cap)]を捕獲 (+\(capVal)) ➔ 2️⃣ 敵[\(oppKiller)]が反撃 (-\(pieceVal)) ➔ 3️⃣ 差引 +\(netProfit) の大勝利！"
        case .ko:     return "1️⃣ [\(piece)]로 [\(cap)] 잡음 (+\(capVal)) ➔ 2️⃣ 적 [\(oppKiller)] 반격 (-\(pieceVal)) ➔ 3️⃣ 순수 +\(netProfit) 이득!"
        case .es:     return "1️⃣ [\(piece)] come [\(cap)] (+\(capVal)) ➔ 2️⃣ Rival [\(oppKiller)] contraataca (-\(pieceVal)) ➔ 3️⃣ ¡Ganancia neta +\(netProfit)!"
        case .fr:     return "1️⃣ [\(piece)] prend [\(cap)] (+\(capVal)) ➔ 2️⃣ L'adversaire riposte (-\(pieceVal)) ➔ 3️⃣ Gain net +\(netProfit) !"
        case .de:     return "1️⃣ [\(piece)] schlägt [\(cap)] (+\(capVal)) ➔ 2️⃣ Gegnertausch [\(oppKiller)] (-\(pieceVal)) ➔ 3️⃣ Netto-Vorteil +\(netProfit)!"
        }
    }

    var adviceBadTradeHeadline: String {
        switch currentLanguage {
        case .zhHans: return "⚠️【亏本兑子·不建议走】"
        case .zhHant: return "⚠️【虧本兌子·不建議走】"
        case .en:     return "⚠️【Unfavorable Trade·Caution】"
        case .ja:     return "⚠️【損な交換！非推奨】"
        case .ko:     return "⚠️【손해 교환! 비추천】"
        case .es:     return "⚠️【Intercambio Desfavorable】"
        case .fr:     return "⚠️【Échange Désavantageux】"
        case .de:     return "⚠️【Verlustreicher Abtausch】"
        }
    }

    func adviceBadTradeDetail(cap: String, loss: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "吃掉【\(cap)】后会被反吃，亏损 \(loss) 战力！"
        case .zhHant: return "吃掉【\(cap)】後會被反吃，虧損 \(loss) 戰力！"
        case .en:     return "Capturing [\(cap)] leads to losing valuable piece, net loss -\(loss)!"
        case .ja:     return "[\(cap)]を捕獲すると反撃され、-\(loss) の戦力損失になります！"
        case .ko:     return "[\(cap)]을 잡은 후 반격당해 -\(loss) 손해가 발생합니다!"
        case .es:     return "¡Comer [\(cap)] causará contraataque y pérdida de -\(loss)!"
        case .fr:     return "Prendre [\(cap)] mènera à une riposte avec perte de -\(loss) !"
        case .de:     return "Das Schlagen von [\(cap)] führt zu Gegenangriff und Verlust von -\(loss)!"
        }
    }

    var adviceBadTradeBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "⚠️ 亏损兑子"
        case .en: return "⚠️ Loss Trade"
        case .ja: return "⚠️ 損失交換"
        case .ko: return "⚠️ 손해 교환"
        case .es: return "⚠️ Mal Canje"
        case .fr: return "⚠️ Mauvais Troc"
        case .de: return "⚠️ Verlust-Zug"
        }
    }

    func adviceBadTradeForecast(cap: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 吃【\(cap)】 ➔ 2️⃣ 我方被反吃 ➔ 3️⃣ 亏损战力"
        case .zhHant: return "1️⃣ 吃【\(cap)】 ➔ 2️⃣ 我方被反吃 ➔ 3️⃣ 虧損戰力"
        case .en:     return "1️⃣ Eat [\(cap)] ➔ 2️⃣ Countered ➔ 3️⃣ Net army loss"
        case .ja:     return "1️⃣ [\(cap)] 捕獲 ➔ 2️⃣ 反撃を受ける ➔ 3️⃣ 戦力低下"
        case .ko:     return "1️⃣ [\(cap)] 포획 ➔ 2️⃣ 반격당함 ➔ 3️⃣ 전력 손실"
        case .es:     return "1️⃣ Come [\(cap)] ➔ 2️⃣ Contraataque ➔ 3️⃣ Pérdida táctica"
        case .fr:     return "1️⃣ Prendre [\(cap)] ➔ 2️⃣ Contre-attaqué ➔ 3️⃣ Perte nette"
        case .de:     return "1️⃣ [\(cap)] schlagen ➔ 2️⃣ Ausgeschaltet ➔ 3️⃣ Punkteverlust"
        }
    }

    func adviceFreeCaptureHeadline(cap: String, isJumpRiver: Bool) -> String {
        if isJumpRiver {
            switch currentLanguage {
            case .zhHans: return "🌊【飞河巧妙夺取·吃掉\(cap)】"
            case .zhHant: return "🌊【飛河巧妙奪取·吃掉\(cap)】"
            case .en:     return "🌊【River Jump Strike: Capture \(cap)】"
            case .ja:     return "🌊【渡河急襲！無傷で[\(cap)]撃破】"
            case .ko:     return "🌊【도하 급습! [\(cap)] 무손실 포획】"
            case .es:     return "🌊【Salto Fluvial: Captura a \(cap)】"
            case .fr:     return "🌊【Frappe par Saut : Capture \(cap)】"
            case .de:     return "🌊【Flusssprung-Schlag: \(cap) schlagen】"
            }
        } else {
            switch currentLanguage {
            case .zhHans: return "👑【巧妙夺取·吃掉\(cap)】"
            case .zhHant: return "👑【巧妙奪取·吃掉\(cap)】"
            case .en:     return "👑【Free Capture: Take [\(cap)]】"
            case .ja:     return "👑【無傷撃破！[\(cap)]を捕獲】"
            case .ko:     return "👑【무손실 포획: [\(cap)] 잡기】"
            case .es:     return "👑【Captura Gratuita: Come [\(cap)]】"
            case .fr:     return "👑【Capture Sans Risque : Prendre [\(cap)]】"
            case .de:     return "👑【Verlustfreier Schlag: \(cap) fangen】"
            }
        }
    }

    func adviceFreeCaptureDetail(piece: String, cap: String, value: Int) -> String {
        switch currentLanguage {
        case .zhHans: return "用【\(piece)】吃掉敌方【\(cap)】后我方绝对安全，白赚 +\(value) 战力！"
        case .zhHant: return "用【\(piece)】吃掉敵方【\(cap)】後我方絕對安全，白賺 +\(value) 戰力！"
        case .en:     return "Use [\(piece)] to take [\(cap)] safely! You gain free +\(value) army value!"
        case .ja:     return "[\(piece)]で[\(cap)]を捕獲後も安全！無傷で +\(value) の戦力獲得！"
        case .ko:     return "[\(piece)]로 적 [\(cap)]을 잡은 후 완전히 안전합니다! +\(value) 이득!"
        case .es:     return "¡Usa [\(piece)] para comer [\(cap)] sin peligro! Ganas +\(value) gratis."
        case .fr:     return "Capturez [\(cap)] avec [\(piece)] en toute sécurité ! +\(value) net !"
        case .de:     return "Mit [\(piece)] [\(cap)] gefahrlos schlagen! Freier Gewinn von +\(value)!"
        }
    }

    func adviceFreeCaptureBadge(cap: String) -> String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "👑 无损吃\(cap)"
        case .en: return "👑 Free \(cap)"
        case .ja: return "👑 \(cap)無傷捕獲"
        case .ko: return "👑 \(cap) 무료포획"
        case .es: return "👑 Come \(cap)"
        case .fr: return "👑 Prendre \(cap)"
        case .de: return "👑 \(cap) schlagen"
        }
    }

    func adviceFreeCaptureForecast(piece: String, cap: String) -> String {
        switch currentLanguage {
        case .zhHans: return "1️⃣ 我方【\(piece)】无损吃【\(cap)】 ➔ 2️⃣ 敌方防线崩溃 ➔ 3️⃣ 全面压制！"
        case .zhHant: return "1️⃣ 我方【\(piece)】無損吃【\(cap)】 ➔ 2️⃣ 敵方防線崩潰 ➔ 3️⃣ 全面壓制！"
        case .en:     return "1️⃣ [\(piece)] captures [\(cap)] safely ➔ 2️⃣ Enemy line breaks ➔ 3️⃣ Total control!"
        case .ja:     return "1️⃣ [\(piece)]が無傷で[\(cap)]捕獲 ➔ 2️⃣ 敵の防衛線崩壊 ➔ 3️⃣ 圧倒的優勢！"
        case .ko:     return "1️⃣ [\(piece)] 무손실 포획 ➔ 2️⃣ 적 방어선 붕괴 ➔ 3️⃣ 압도적 우위!"
        case .es:     return "1️⃣ [\(piece)] captura [\(cap)] seguro ➔ 2️⃣ Defensa rival rota ➔ 3️⃣ ¡Control total!"
        case .fr:     return "1️⃣ [\(piece)] prend [\(cap)] sans danger ➔ 2️⃣ Ligne ennemie brisée ➔ 3️⃣ Domination !"
        case .de:     return "1️⃣ [\(piece)] schlägt [\(cap)] sicher ➔ 2️⃣ Gegnertruppe wankt ➔ 3️⃣ Totale Dominanz!"
        }
    }

    var adviceAssaultDenHeadline: String {
        switch currentLanguage {
        case .zhHans: return "🏰【直取兽穴·胜势冲锋】"
        case .zhHant: return "🏰【直取獸穴·勝勢衝鋒】"
        case .en:     return "🏰【Den Assault·Final Charge】"
        case .ja:     return "🏰【敵本陣強襲！突撃の一手】"
        case .ko:     return "🏰【진영 돌파! 최종 돌격】"
        case .es:     return "🏰【Asalto a la Guarida·Carga Final】"
        case .fr:     return "🏰【Assaut du Repaire·Charge Finale】"
        case .de:     return "🏰【Höhlen-Sturm·Finaler Ansturm】"
        }
    }

    var adviceAssaultDenDetail: String {
        switch currentLanguage {
        case .zhHans: return "敌方大本营就在眼前！全速扑向大本营锁定胜局！"
        case .zhHant: return "敵方大本營就在眼前！全速撲向大本營鎖定勝局！"
        case .en:     return "Enemy Den is right ahead! Charge forward to secure immediate victory!"
        case .ja:     return "敵の本陣は目の前です！全速力で突入して勝利を決めましょう！"
        case .ko:     return "적 진영이 바로 앞입니다! 전속력으로 진격하여 승리를 확정하세요!"
        case .es:     return "¡La guarida rival está a tiro! ¡Carga al frente y sentencia la partida!"
        case .fr:     return "La tanière adverse est à portée ! Foncez pour sceller la victoire !"
        case .de:     return "Gegnerische Höhle liegt direkt vor dir! Vorstürmen und Sieg sichern!"
        }
    }

    var adviceAssaultDenBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🏆 冲向兽穴"
        case .en: return "🏆 Charge Den"
        case .ja: return "🏆 本陣突撃"
        case .ko: return "🏆 진영 돌격"
        case .es: return "🏆 Cargar Guarida"
        case .fr: return "🏆 Assaut"
        case .de: return "🏆 Höhlensturm"
        }
    }

    var adviceApproachDenHeadline: String {
        switch currentLanguage {
        case .zhHans: return "⚡️【直捣黄龙·逼近兽穴】"
        case .zhHant: return "⚡️【直搗黃龍·逼近獸穴】"
        case .en:     return "⚡️【Deep Infiltration·Advance】"
        case .ja:     return "⚡️【敵陣深く進撃！本陣へ接近】"
        case .ko:     return "⚡️【적진 깊숙이 진격! 진영 압박】"
        case .es:     return "⚡️【Infiltración Profunda·Avanzar】"
        case .fr:     return "⚡️【Infiltration Profonde·Avancer】"
        case .de:     return "⚡️【Tiefe Infiltration·Vorrücken】"
        }
    }

    func adviceApproachDenDetail(piece: String) -> String {
        switch currentLanguage {
        case .zhHans: return "【\(piece)】已突入敌后！全速挺进！"
        case .zhHant: return "【\(piece)】已突入敵後！全速挺進！"
        case .en:     return "[\(piece)] penetrated enemy lines! Keep pressing forward!"
        case .ja:     return "[\(piece)]が敵陣深くに切り込みました！このまま突進！"
        case .ko:     return "[\(piece)] 말이 적진 뒤편으로 침투했습니다! 전속 전진!"
        case .es:     return "¡[\(piece)] ha penetrado las líneas enemigas! ¡Sigue avanzando!"
        case .fr:     return "[\(piece)] a percé les lignes ennemies ! Continuez d'avancer !"
        case .de:     return "[\(piece)] ist durchgebrochen! Weiter vorrücken!"
        }
    }

    var adviceApproachDenBadge: String {
        switch currentLanguage {
        case .zhHans, .zhHant: return "🎯 逼近兽穴"
        case .en: return "🎯 Approach Den"
        case .ja: return "🎯 本陣接近"
        case .ko: return "🎯 진영 접근"
        case .es: return "🎯 Hacia Guarida"
        case .fr: return "🎯 Vers Repaire"
        case .de: return "🎯 Höhle nah"
        }
    }
}
