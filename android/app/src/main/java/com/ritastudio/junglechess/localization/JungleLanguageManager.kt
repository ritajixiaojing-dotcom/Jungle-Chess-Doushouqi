package com.ritastudio.junglechess.localization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ritastudio.junglechess.model.*

class JungleLanguageManager(initialLanguage: JungleLanguage = JungleLanguage.detectSystem()) {
    var isFollowingSystem by mutableStateOf(true)
    var currentLanguage by mutableStateOf(initialLanguage)

    fun setLanguage(lang: JungleLanguage) {
        isFollowingSystem = false
        currentLanguage = lang
    }

    fun setFollowSystem() {
        isFollowingSystem = true
        currentLanguage = JungleLanguage.detectSystem()
    }

    companion object {
        val shared = JungleLanguageManager()
    }
// MARK: - Localized Accessors
    
    val appTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "快乐斗兽棋 · Jungle Chess"
        JungleLanguage.ZH_HANT -> "快樂鬥獸棋 · Jungle Chess"
        JungleLanguage.EN -> "Jungle Chess"
        JungleLanguage.JA -> "楽しい闘獣棋 (Jungle Chess)"
        JungleLanguage.KO -> "행복한 투수기 (Jungle Chess)"
        JungleLanguage.ES -> "Ajedrez de la Selva (Jungle Chess)"
        JungleLanguage.FR -> "Jeu des Animaux (Jungle Chess)"
        JungleLanguage.DE -> "Dschungelschach (Jungle Chess)"
        }
    }
    
    val systemDefaultText: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🌐 跟随系统 (默认)"
        JungleLanguage.ZH_HANT -> "🌐 跟隨系統 (預設)"
        JungleLanguage.EN -> "🌐 System Default"
        JungleLanguage.JA -> "🌐 システム設定 (デフォルト)"
        JungleLanguage.KO -> "🌐 시스템 기본값"
        JungleLanguage.ES -> "🌐 Idioma del Sistema"
        JungleLanguage.FR -> "🌐 Par défaut du système"
        JungleLanguage.DE -> "🌐 Systemeinstellung"
        }
    }
    
    fun playerName(player: PlayerColor): String {
        return when (player ) {
        PlayerColor.RED ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "红方"
            JungleLanguage.ZH_HANT -> "紅方"
            JungleLanguage.EN -> "Red"
            JungleLanguage.JA -> "赤方"
            JungleLanguage.KO -> "홍군"
            JungleLanguage.ES -> "Rojo"
            JungleLanguage.FR -> "Rouge"
            JungleLanguage.DE -> "Rot"
            }
        PlayerColor.BLUE ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "蓝方"
            JungleLanguage.ZH_HANT -> "藍方"
            JungleLanguage.EN -> "Blue"
            JungleLanguage.JA -> "青方"
            JungleLanguage.KO -> "청군"
            JungleLanguage.ES -> "Azul"
            JungleLanguage.FR -> "Bleu"
            JungleLanguage.DE -> "Blau"
            }
        }
    }
    
    fun animalName(type: AnimalType): String {
        return when (type ) {
        AnimalType.RAT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "鼠"
            JungleLanguage.EN -> "Rat"
            JungleLanguage.JA -> "鼠"
            JungleLanguage.KO -> "쥐"
            JungleLanguage.ES -> "Rata"
            JungleLanguage.FR -> "Rat"
            JungleLanguage.DE -> "Ratte"
            }
        AnimalType.CAT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "猫"
            JungleLanguage.ZH_HANT -> "貓"
            JungleLanguage.EN -> "Cat"
            JungleLanguage.JA -> "猫"
            JungleLanguage.KO -> "고양이"
            JungleLanguage.ES -> "Gato"
            JungleLanguage.FR -> "Chat"
            JungleLanguage.DE -> "Katze"
            }
        AnimalType.DOG ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "狗"
            JungleLanguage.EN -> "Dog"
            JungleLanguage.JA -> "犬"
            JungleLanguage.KO -> "개"
            JungleLanguage.ES -> "Perro"
            JungleLanguage.FR -> "Chien"
            JungleLanguage.DE -> "Hund"
            }
        AnimalType.WOLF ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "狼"
            JungleLanguage.EN -> "Wolf"
            JungleLanguage.JA -> "狼"
            JungleLanguage.KO -> "늑대"
            JungleLanguage.ES -> "Lobo"
            JungleLanguage.FR -> "Loup"
            JungleLanguage.DE -> "Wolf"
            }
        AnimalType.LEOPARD ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "豹"
            JungleLanguage.EN -> "Leopard"
            JungleLanguage.JA -> "豹"
            JungleLanguage.KO -> "표범"
            JungleLanguage.ES -> "Leopardo"
            JungleLanguage.FR -> "Léopard"
            JungleLanguage.DE -> "Leopard"
            }
        AnimalType.TIGER ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "虎"
            JungleLanguage.EN -> "Tiger"
            JungleLanguage.JA -> "虎"
            JungleLanguage.KO -> "호랑이"
            JungleLanguage.ES -> "Tigre"
            JungleLanguage.FR -> "Tigre"
            JungleLanguage.DE -> "Tiger"
            }
        AnimalType.LION ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "狮"
            JungleLanguage.ZH_HANT -> "獅"
            JungleLanguage.EN -> "Lion"
            JungleLanguage.JA -> "獅"
            JungleLanguage.KO -> "사자"
            JungleLanguage.ES -> "León"
            JungleLanguage.FR -> "Lion"
            JungleLanguage.DE -> "Löwe"
            }
        AnimalType.ELEPHANT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "象"
            JungleLanguage.EN -> "Elephant"
            JungleLanguage.JA -> "象"
            JungleLanguage.KO -> "코끼리"
            JungleLanguage.ES -> "Elefante"
            JungleLanguage.FR -> "Éléphant"
            JungleLanguage.DE -> "Elefant"
            }
        }
    }
    
    fun animalEatRule(type: AnimalType): String {
        return when (type ) {
        AnimalType.ELEPHANT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃象及以下(🐘🦁🐯🐆🐺🐶🐱，除鼠外)；同级可互吃"
            JungleLanguage.ZH_HANT -> "能吃象及以下(🐘🦁🐯🐆🐺🐶🐱，除鼠外)；同級可互吃"
            JungleLanguage.EN -> "Captures Elephant & below (except Rat); captures equal rank"
            JungleLanguage.JA -> "象以下を捕獲可能(ネズミ以外)；同格は互いに捕獲可"
            JungleLanguage.KO -> "코끼리 이하 포획(쥐 제외)；동급 상호 포획 가능"
            JungleLanguage.ES -> "Come Elefante e inferiores (excepto Rata); come mismo rango"
            JungleLanguage.FR -> "Capture Éléphant et inférieurs (sauf Rat); même rang"
            JungleLanguage.DE -> "Schlägt Elefant und schwächer (außer Ratte); gleicher Rang"
            }
        AnimalType.LION ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃狮及以下(🦁🐯🐆🐺🐶🐱🐭)；可飞跃小河；同级可互吃"
            JungleLanguage.ZH_HANT -> "能吃獅及以下(🦁🐯🐆🐺🐶🐱🐭)；可飛躍小河；同級可互吃"
            JungleLanguage.EN -> "Captures Lion & below; jumps across river; captures equal rank"
            JungleLanguage.JA -> "獅子以下を捕獲；川を飛び越え可能；同格捕獲可"
            JungleLanguage.KO -> "사자 이하 포획；강을 뛰어넘음；동급 포획 가능"
            JungleLanguage.ES -> "Come León e inferiores; salta el río; come mismo rango"
            JungleLanguage.FR -> "Capture Lion et inférieurs; saute la rivière; même rang"
            JungleLanguage.DE -> "Schlägt Löwe und schwächer; springt über Fluss; gleicher Rang"
            }
        AnimalType.TIGER ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃虎及以下(🐯🐆🐺🐶🐱🐭)；可飞跃小河；同级可互吃"
            JungleLanguage.ZH_HANT -> "能吃虎及以下(🐯🐆🐺🐶🐱🐭)；可飛躍小河；同級可互吃"
            JungleLanguage.EN -> "Captures Tiger & below; jumps across river; captures equal rank"
            JungleLanguage.JA -> "虎以下を捕獲；川を飛び越え可能；同格捕獲可"
            JungleLanguage.KO -> "호랑이 이하 포획；강을 뛰어넘음；동급 포획 가능"
            JungleLanguage.ES -> "Come Tigre e inferiores; salta el río; come mismo rango"
            JungleLanguage.FR -> "Capture Tigre et inférieurs; saute la rivière; même rang"
            JungleLanguage.DE -> "Schlägt Tiger und schwächer; springt über Fluss; gleicher Rang"
            }
        AnimalType.LEOPARD ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃豹及以下(🐆🐺🐶🐱🐭)；同级可互吃(先手必胜)"
            JungleLanguage.ZH_HANT -> "能吃豹及以下(🐆🐺🐶🐱🐭)；同級可互吃(先手必勝)"
            JungleLanguage.EN -> "Captures Leopard & below; captures equal rank"
            JungleLanguage.JA -> "豹以下を捕獲；同格は先手必勝"
            JungleLanguage.KO -> "표범 이하 포획；동급 선공 유리"
            JungleLanguage.ES -> "Come Leopardo e inferiores; come mismo rango"
            JungleLanguage.FR -> "Capture Léopard et inférieurs; même rang"
            JungleLanguage.DE -> "Schlägt Leopard und schwächer; gleicher Rang"
            }
        AnimalType.WOLF ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃狼及以下(🐺🐶🐱🐭)；同级可互吃(先手必胜)"
            JungleLanguage.ZH_HANT -> "能吃狼及以下(🐺🐶🐱🐭)；同級可互吃(先手必勝)"
            JungleLanguage.EN -> "Captures Wolf & below; captures equal rank"
            JungleLanguage.JA -> "狼以下を捕獲；同格は先手必勝"
            JungleLanguage.KO -> "늑대 이하 포획；동급 선공 유리"
            JungleLanguage.ES -> "Come Lobo e inferiores; come mismo rango"
            JungleLanguage.FR -> "Capture Loup et inférieurs; même rang"
            JungleLanguage.DE -> "Schlägt Wolf und schwächer; gleicher Rang"
            }
        AnimalType.DOG ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃狗及以下(🐶🐱🐭)；同级可互吃(先手必胜)"
            JungleLanguage.ZH_HANT -> "能吃狗及以下(🐶🐱🐭)；同級可互吃(先手必勝)"
            JungleLanguage.EN -> "Captures Dog & below; captures equal rank"
            JungleLanguage.JA -> "犬以下を捕獲；同格は先手必勝"
            JungleLanguage.KO -> "개 이하 포획；동급 선공 유리"
            JungleLanguage.ES -> "Come Perro e inferiores; come mismo rango"
            JungleLanguage.FR -> "Capture Chien et inférieurs; même rang"
            JungleLanguage.DE -> "Schlägt Hund und schwächer; gleicher Rang"
            }
        AnimalType.CAT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "能吃猫及以下(🐱🐭)；同级可互吃(先手必胜)"
            JungleLanguage.ZH_HANT -> "能吃貓及以下(🐱🐭)；同級可互吃(先手必勝)"
            JungleLanguage.EN -> "Captures Cat & Rat; captures equal rank"
            JungleLanguage.JA -> "猫と鼠を捕獲；同格は先手必勝"
            JungleLanguage.KO -> "고양이와 쥐 포획；동급 선공 유리"
            JungleLanguage.ES -> "Come Gato y Rata; come mismo rango"
            JungleLanguage.FR -> "Capture Chat et Rat; même rang"
            JungleLanguage.DE -> "Schlägt Katze und Ratte; gleicher Rang"
            }
        AnimalType.RAT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "⭐克制大象(🐘)！能吃同级鼠；唯一能下水游泳"
            JungleLanguage.ZH_HANT -> "⭐克制大象(🐘)！能吃同級鼠；唯一能下水游泳"
            JungleLanguage.EN -> "⭐ Beats Elephant (🐘)! Can swim in river"
            JungleLanguage.JA -> "⭐ 象(🐘)に勝てる！唯一川を泳げる特権"
            JungleLanguage.KO -> "⭐ 코끼리(🐘)를 이김! 유일하게 강을 헤엄침"
            JungleLanguage.ES -> "⭐ ¡Vence al Elefante (🐘)! El único que nada"
            JungleLanguage.FR -> "⭐ Bat l'Éléphant (🐘) ! Le seul qui nage"
            JungleLanguage.DE -> "⭐ Besiegt Elefant (🐘)! Einziges Schwimmtier"
            }
        }
    }
    
    fun difficultyName(diff: AIDifficulty): String {
        return when (diff ) {
        AIDifficulty.EASY ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🌱 轻松入门"
            JungleLanguage.ZH_HANT -> "🌱 輕鬆入門"
            JungleLanguage.EN -> "🌱 Casual"
            JungleLanguage.JA -> "🌱 初級"
            JungleLanguage.KO -> "🌱 초급"
            JungleLanguage.ES -> "🌱 Fácil"
            JungleLanguage.FR -> "🌱 Facile"
            JungleLanguage.DE -> "🌱 Leicht"
            }
        AIDifficulty.MEDIUM ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🌿 棋艺进阶"
            JungleLanguage.ZH_HANT -> "🌿 棋藝進階"
            JungleLanguage.EN -> "🌿 Balanced"
            JungleLanguage.JA -> "🌿 中級"
            JungleLanguage.KO -> "🌿 중급"
            JungleLanguage.ES -> "🌿 Medio"
            JungleLanguage.FR -> "🌿 Moyen"
            JungleLanguage.DE -> "🌿 Normal"
            }
        AIDifficulty.HARD ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "👑 智谋大师"
            JungleLanguage.ZH_HANT -> "👑 智謀大師"
            JungleLanguage.EN -> "👑 Master"
            JungleLanguage.JA -> "👑 上級"
            JungleLanguage.KO -> "👑 상급"
            JungleLanguage.ES -> "👑 Difícil"
            JungleLanguage.FR -> "👑 Difficile"
            JungleLanguage.DE -> "👑 Schwer"
            }
        }
    }
    
    fun modeName(isVsAI: Boolean): String {
        return if (isVsAI) {
            when (currentLanguage) {
                JungleLanguage.ZH_HANS -> "👤 人机对决"
                JungleLanguage.ZH_HANT -> "👤 人機對決"
                JungleLanguage.EN -> "👤 vs AI"
                JungleLanguage.JA -> "👤 AI対戦"
                JungleLanguage.KO -> "👤 AI 대전"
                JungleLanguage.ES -> "👤 vs IA"
                JungleLanguage.FR -> "👤 vs IA"
                JungleLanguage.DE -> "👤 Gegen KI"
            }
        } else {
            when (currentLanguage) {
                JungleLanguage.ZH_HANS -> "👥 双人同屏"
                JungleLanguage.ZH_HANT -> "👥 雙人同屏"
                JungleLanguage.EN -> "👥 2 Players"
                JungleLanguage.JA -> "👥 二人対戦"
                JungleLanguage.KO -> "👥 2인 대전"
                JungleLanguage.ES -> "👥 2 Jugadores"
                JungleLanguage.FR -> "👥 2 Joueurs"
                JungleLanguage.DE -> "👥 2 Spieler"
            }
        }
    }
    
    val moveForMeBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🚀 帮我走这步\n(破局)"
        JungleLanguage.ZH_HANT -> "🚀 幫我走這步\n(破局)"
        JungleLanguage.EN -> "🚀 Smart Move\n(Hint)"
        JungleLanguage.JA -> "🚀 AIアシスト\n(推薦手)"
        JungleLanguage.KO -> "🚀 추천 수\n(힌트)"
        JungleLanguage.ES -> "🚀 Sugerencia\n(Movimiento)"
        JungleLanguage.FR -> "🚀 Coup Clé\n(Conseil)"
        JungleLanguage.DE -> "🚀 Zugtipp\n(Bester Zug)"
        }
    }
    
    val undoBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "悔棋"
        JungleLanguage.EN -> "Undo"
        JungleLanguage.JA -> "待った"
        JungleLanguage.KO -> "무르기"
        JungleLanguage.ES -> "Deshacer"
        JungleLanguage.FR -> "Annuler"
        JungleLanguage.DE -> "Zurück"
        }
    }
    
    val restartBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "重来"
        JungleLanguage.EN -> "Restart"
        JungleLanguage.JA -> "やり直し"
        JungleLanguage.KO -> "다시하기"
        JungleLanguage.ES -> "Reiniciar"
        JungleLanguage.FR -> "Recommencer"
        JungleLanguage.DE -> "Neustart"
        }
    }
    
    val rulePromptHint: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "📖 点击棋盘上任意动物，实时查看它能吃谁、怎么走。"
        JungleLanguage.ZH_HANT -> "📖 點擊棋盤上任意動物，實時查看它能吃誰、怎麼走。"
        JungleLanguage.EN -> "📖 Tap any piece to view moves & targets."
        JungleLanguage.JA -> "📖 盤上の駒をタップすると、進路と捕獲対象を確認できます。"
        JungleLanguage.KO -> "📖 말 선택 시 이동 가능한 위치와 규칙을 확인합니다."
        JungleLanguage.ES -> "📖 Toca una pieza para ver movimientos y capturas."
        JungleLanguage.FR -> "📖 Touchez une pièce pour voir mouvements et cibles."
        JungleLanguage.DE -> "📖 Figur antippen, um Züge und Schlagziele zu sehen."
        }
    }
    
    val redTrapLabel: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "红陷"
        JungleLanguage.ZH_HANT -> "紅陷"
        JungleLanguage.EN -> "Trap"
        JungleLanguage.JA -> "赤罠"
        JungleLanguage.KO -> "함정"
        JungleLanguage.ES -> "Trampa"
        JungleLanguage.FR -> "Piège"
        JungleLanguage.DE -> "Falle"
        }
    }
    
    val blueTrapLabel: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "蓝陷"
        JungleLanguage.ZH_HANT -> "藍陷"
        JungleLanguage.EN -> "Trap"
        JungleLanguage.JA -> "青罠"
        JungleLanguage.KO -> "함정"
        JungleLanguage.ES -> "Trampa"
        JungleLanguage.FR -> "Piège"
        JungleLanguage.DE -> "Falle"
        }
    }
    
    val redDenLabel: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "红穴"
        JungleLanguage.ZH_HANT -> "紅穴"
        JungleLanguage.EN -> "Den"
        JungleLanguage.JA -> "赤城"
        JungleLanguage.KO -> "진영"
        JungleLanguage.ES -> "Guarida"
        JungleLanguage.FR -> "Tanière"
        JungleLanguage.DE -> "Höhle"
        }
    }
    
    val blueDenLabel: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "蓝穴"
        JungleLanguage.ZH_HANT -> "藍穴"
        JungleLanguage.EN -> "Den"
        JungleLanguage.JA -> "青城"
        JungleLanguage.KO -> "진영"
        JungleLanguage.ES -> "Guarida"
        JungleLanguage.FR -> "Tanière"
        JungleLanguage.DE -> "Höhle"
        }
    }
    
    fun shieldSkillText(count: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🛡️ 神兽天降守穴 (${count})"
        JungleLanguage.ZH_HANT -> "🛡️ 神獸天降守穴 (${count})"
        JungleLanguage.EN -> "🛡️ Guardian Teleport (${count})"
        JungleLanguage.JA -> "🛡️ 神獣防衛ワープ (${count})"
        JungleLanguage.KO -> "🛡️ 수호신 소환 (${count})"
        JungleLanguage.ES -> "🛡️ Teletransporte Guardián (${count})"
        JungleLanguage.FR -> "🛡️ Téléportation Défensive (${count})"
        JungleLanguage.DE -> "🛡️ Wächter-Teleport (${count})"
        }
    }
    
    fun frenzySkillText(count: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "✨ 妙手越级 (${count})"
        JungleLanguage.ZH_HANT -> "✨ 妙手越級 (${count})"
        JungleLanguage.EN -> "✨ Master Leap (${count})"
        JungleLanguage.JA -> "✨ 妙手跳躍 (${count})"
        JungleLanguage.KO -> "✨ 묘수 도약 (${count})"
        JungleLanguage.ES -> "✨ Salto Magistral (${count})"
        JungleLanguage.FR -> "✨ Saut Magistral (${count})"
        JungleLanguage.DE -> "✨ Meister-Sprung (${count})"
        }
    }
    
    val victoryTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🎉 对决胜利！"
        JungleLanguage.ZH_HANT -> "🎉 對決勝利！"
        JungleLanguage.EN -> "🎉 Victory!"
        JungleLanguage.JA -> "🎉 勝利！"
        JungleLanguage.KO -> "🎉 승리!"
        JungleLanguage.ES -> "🎉 ¡Victoria!"
        JungleLanguage.FR -> "🎉 Victoire !"
        JungleLanguage.DE -> "🎉 Sieg!"
        }
    }
    
    val defeatTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "💔 遗憾落败"
        JungleLanguage.EN -> "💔 Defeat"
        JungleLanguage.JA -> "💔 敗北"
        JungleLanguage.KO -> "💔 패배"
        JungleLanguage.ES -> "💔 Derrota"
        JungleLanguage.FR -> "💔 Défaite"
        JungleLanguage.DE -> "💔 Niederlage"
        }
    }
    
    val playAgainBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "再来一局"
        JungleLanguage.ZH_HANT -> "再來一局"
        JungleLanguage.EN -> "Play Again"
        JungleLanguage.JA -> "もう一度"
        JungleLanguage.KO -> "다시 대국"
        JungleLanguage.ES -> "Jugar otra vez"
        JungleLanguage.FR -> "Rejouer"
        JungleLanguage.DE -> "Nochmal spielen"
        }
    }
    
    val ruleSheetTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "📖 斗兽棋经典对弈规则"
        JungleLanguage.ZH_HANT -> "📖 鬥獸棋經典對弈規則"
        JungleLanguage.EN -> "📖 Jungle Chess Rules"
        JungleLanguage.JA -> "📖 闘獣棋の遊び方とルール"
        JungleLanguage.KO -> "📖 투수기 대국 규칙"
        JungleLanguage.ES -> "📖 Reglas de Ajedrez de la Selva"
        JungleLanguage.FR -> "📖 Règles du Combat des Animaux"
        JungleLanguage.DE -> "📖 Dschungelschach Regeln"
        }
    }
    
    val ruleHierarchyTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1. 动物食物链等级 (从大到小)"
        JungleLanguage.ZH_HANT -> "1. 動物食物鏈等級 (從大到小)"
        JungleLanguage.EN -> "1. Animal Rank Hierarchy (Highest to Lowest)"
        JungleLanguage.JA -> "1. 動物の強さ序列 (強い順)"
        JungleLanguage.KO -> "1. 동물 서열 계급 (높은 순)"
        JungleLanguage.ES -> "1. Jerarquía de Animales (Mayor a Menor)"
        JungleLanguage.FR -> "1. Hiérarchie des Animaux (Plus Fort au Plus Faible)"
        JungleLanguage.DE -> "1. Tier-Rangfolge (Stärkste zuerst)"
        }
    }
    
    val ruleSpecialAbilitiesTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "2. 特殊地形与特殊能力"
        JungleLanguage.ZH_HANT -> "2. 特殊地形與特殊能力"
        JungleLanguage.EN -> "2. Special Terrain & Abilities"
        JungleLanguage.JA -> "2. 特殊地形と特殊能力"
        JungleLanguage.KO -> "2. 특수 지형 및 특수 능력"
        JungleLanguage.ES -> "2. Terrenos y Habilidades Especiales"
        JungleLanguage.FR -> "2. Terrains et Capacités Spéciales"
        JungleLanguage.DE -> "2. Spezial-Terrain & Fähigkeiten"
        }
    }
    
    val ruleWinConditionTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "3. 胜负判定条件"
        JungleLanguage.ZH_HANT -> "3. 勝負判定條件"
        JungleLanguage.EN -> "3. Win / Loss Conditions"
        JungleLanguage.JA -> "3. 勝敗の判定条件"
        JungleLanguage.KO -> "3. 승패 결정 조건"
        JungleLanguage.ES -> "3. Condiciones de Victoria"
        JungleLanguage.FR -> "3. Conditions de Victoire"
        JungleLanguage.DE -> "3. Siegbedingungen"
        }
    }
    
    val ruleWinConditionDesc: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "任一方动物进入对方兽穴（大本营）即直接获胜；或将对方全部动物吃光亦可获胜。"
        JungleLanguage.ZH_HANT -> "任一方動物進入對方獸穴（大本營）即直接獲勝；或將對方全部動物吃光亦可獲勝。"
        JungleLanguage.EN -> "Entering the opponent's Den immediately wins the match; capturing all opposing pieces also wins."
        JungleLanguage.JA -> "相手の獣穴(城)に入城するか、相手の全コマを捕獲すると勝利となります。"
        JungleLanguage.KO -> "상대방의 진영(동굴)에 들어가거나 모든 적 말을 잡으면 승리합니다."
        JungleLanguage.ES -> "Entrar en la guarida enemiga o capturar todas las piezas rivales otorga la victoria inmediata."
        JungleLanguage.FR -> "Entrer dans la tanière ennemie ou capturer toutes les pièces adverses assure la victoire immédiate."
        JungleLanguage.DE -> "Das Betreten der gegnerischen Höhle oder das Schlagen aller gegnerischen Figuren führt zum Sieg."
        }
    }
    
    val languagePickerTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🌐 切换语言"
        JungleLanguage.ZH_HANT -> "🌐 切換語言"
        JungleLanguage.EN -> "🌐 Language"
        JungleLanguage.JA -> "🌐 言語切替"
        JungleLanguage.KO -> "🌐 언어 변경"
        JungleLanguage.ES -> "🌐 Idioma"
        JungleLanguage.FR -> "🌐 Langue"
        JungleLanguage.DE -> "🌐 Sprache"
        }
    }
    
    val closeBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "我知道了，开始对弈"
        JungleLanguage.EN -> "Got it, Let's Play"
        JungleLanguage.JA -> "了解、対局を開始"
        JungleLanguage.KO -> "확인, 대국 시작"
        JungleLanguage.ES -> "Entendido, ¡A jugar!"
        JungleLanguage.FR -> "Compris, Jouer"
        JungleLanguage.DE -> "Verstanden, Los geht's"
        }
    }
    
    val highlightBtn: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "高亮显示"
        JungleLanguage.EN -> "Highlight"
        JungleLanguage.JA -> "強調表示"
        JungleLanguage.KO -> "강조 표시"
        JungleLanguage.ES -> "Resaltar"
        JungleLanguage.FR -> "Surbrillance"
        JungleLanguage.DE -> "Hervorheben"
        }
    }
    
    val depth3DeductionTitle: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🔮 3步深度推演:"
        JungleLanguage.EN -> "🔮 3-Step Forecast:"
        JungleLanguage.JA -> "🔮 3手読み予測:"
        JungleLanguage.KO -> "🔮 3수 수읽기 예측:"
        JungleLanguage.ES -> "🔮 Predicción a 3 pasos:"
        JungleLanguage.FR -> "🔮 Prévision à 3 coups :"
        JungleLanguage.DE -> "🔮 3-Züge Vorschau:"
        }
    }
    
    val deducingBestMove: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "正在推演全局最优路线..."
        JungleLanguage.EN -> "Calculating optimal move..."
        JungleLanguage.JA -> "最適手を分析中..."
        JungleLanguage.KO -> "최적의 수를 계산하는 중..."
        JungleLanguage.ES -> "Calculando la mejor jugada..."
        JungleLanguage.FR -> "Calcul du meilleur coup en cours..."
        JungleLanguage.DE -> "Berechne optimalen Zug..."
        }
    }
    
    val frenzyBannerText: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "✨【妙手越级已开启】：2回合内你的所有动物可越级吃掉对方棋子！"
        JungleLanguage.ZH_HANT -> "✨【妙手越級已開啟】：2回合內你的所有動物可越級吃掉對方棋子！"
        JungleLanguage.EN -> "✨【Master Leap Activated】: For 2 turns, all your pieces can capture higher-ranked opponents!"
        JungleLanguage.JA -> "✨【妙手跳躍発動】：2ターンの間、味方全駒が格上の駒を捕獲可能！"
        JungleLanguage.KO -> "✨【묘수 발동】：2턴 동안 모든 아군 말이 상위 등급의 말을 잡을 수 있습니다!"
        JungleLanguage.ES -> "✨【Salto Magistral Activado】: ¡Durante 2 turnos tus animales pueden comer rangos superiores!"
        JungleLanguage.FR -> "✨【Saut Magistral Activé】: Pendant 2 tours, vos animaux capturent les rangs supérieurs !"
        JungleLanguage.DE -> "✨【Meister-Sprung Aktiviert】: Für 2 Runden können deine Tiere stärkere Figuren schlagen!"
        }
    }
    
    fun shieldBannerText(pieceName: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🛡️【神兽天降】：【${pieceName}】已瞬间传送回防大本营咽喉要道！"
        JungleLanguage.ZH_HANT -> "🛡️【神獸天降】：【${pieceName}】已瞬間傳送回防大本營咽喉要道！"
        JungleLanguage.EN -> "🛡️【Guardian Teleport】: [${pieceName}] teleported to defend the home den choke point!"
        JungleLanguage.JA -> "🛡️【神獣防衛ワープ】：[${pieceName}]が自陣獣穴の要所へ瞬間移動しました！"
        JungleLanguage.KO -> "🛡️【수호신 소환】：[${pieceName}] 말이 진영 요충지로 순간이동했습니다!"
        JungleLanguage.ES -> "🛡️【Teletransporte Guardián】: ¡[${pieceName}] se ha teletransportado para defender la guarida!"
        JungleLanguage.FR -> "🛡️【Téléportation Défensive】: [${pieceName}] téléporté pour défendre la tanière !"
        JungleLanguage.DE -> "🛡️【Wächter-Teleport】: [${pieceName}] zum Schutz der Höhle teleportiert!"
        }
    }
    
    val eatElephantBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "吃大象"
        JungleLanguage.EN -> "Eat Elephant"
        JungleLanguage.JA -> "象を捕獲"
        JungleLanguage.KO -> "코끼리 잡기"
        JungleLanguage.ES -> "Comer Elefante"
        JungleLanguage.FR -> "Capturer Éléphant"
        JungleLanguage.DE -> "Elefant schlagen"
        }
    }
    
    val protectElephantBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "灭鼠护象"
        JungleLanguage.EN -> "Hunt Rat"
        JungleLanguage.JA -> "鼠退治"
        JungleLanguage.KO -> "쥐 퇴치"
        JungleLanguage.ES -> "Cazar Rata"
        JungleLanguage.FR -> "Chasser le Rat"
        JungleLanguage.DE -> "Ratte jagen"
        }
    }
    
    fun captureTargetBadge(type: AnimalType): String {
        val name = animalName(type)
        return when (currentLanguage) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "吃${name}"
        JungleLanguage.EN -> "Eat ${name}"
        JungleLanguage.JA -> "${name}捕獲"
        JungleLanguage.KO -> "${name} 잡기"
        JungleLanguage.ES -> "Comer ${name}"
        JungleLanguage.FR -> "Capturer ${name}"
        JungleLanguage.DE -> "${name} schlagen"
        }
    }
    
    val jumpRiverBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "飞跃小河"
        JungleLanguage.EN -> "Jump River"
        JungleLanguage.JA -> "渡河ジャンプ"
        JungleLanguage.KO -> "강 뛰어넘기"
        JungleLanguage.ES -> "Saltar Río"
        JungleLanguage.FR -> "Sauter Rivière"
        JungleLanguage.DE -> "Flusssprung"
        }
    }
    
    val winGameBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "制胜赢"
        JungleLanguage.EN -> "Checkmate"
        JungleLanguage.JA -> "詰み・勝利"
        JungleLanguage.KO -> "결정타 승리"
        JungleLanguage.ES -> "Victoria"
        JungleLanguage.FR -> "Victoire"
        JungleLanguage.DE -> "Siegzug"
        }
    }
    
    fun doctrineName(doctrine: StrategicDoctrine): String {
        return when (doctrine ) {
                StrategicDoctrine.TIGER_LEAP ->
            when (currentLanguage) {
                JungleLanguage.ZH_HANS -> "🐯 猛虎纵跃横飞"
                JungleLanguage.ZH_HANT -> "🐯 猛虎縱躍橫飛"
                JungleLanguage.EN -> "🐯 Tiger River Leap"
                JungleLanguage.JA -> "🐯 猛虎渡河跳躍"
                JungleLanguage.KO -> "🐯 호랑이 도하 도약"
                JungleLanguage.ES -> "🐯 Salto de Tigre"
                JungleLanguage.FR -> "🐯 Saut du Tigre"
                JungleLanguage.DE -> "🐯 Tiger-Flusssprung"
            }
        StrategicDoctrine.LION_BLITZ ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🦁 狮王飞河跃进"
            JungleLanguage.ZH_HANT -> "🦁 獅王飛河躍進"
            JungleLanguage.EN -> "🦁 Lion River Leap"
            JungleLanguage.JA -> "🦁 獅子渡河跳躍"
            JungleLanguage.KO -> "🦁 사자 도하 도약"
            JungleLanguage.ES -> "🦁 Salto de León"
            JungleLanguage.FR -> "🦁 Saut du Lion"
            JungleLanguage.DE -> "🦁 Löwen-Flusssprung"
            }
        StrategicDoctrine.ELEPHANT_PUSH ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🐘 象王稳步推进"
            JungleLanguage.ZH_HANT -> "🐘 象王穩步推進"
            JungleLanguage.EN -> "🐘 Elephant Advance"
            JungleLanguage.JA -> "🐘 巨象中央推進"
            JungleLanguage.KO -> "🐘 코끼리 정면 진격"
            JungleLanguage.ES -> "🐘 Avance de Elefante"
            JungleLanguage.FR -> "🐘 Poussée de l'Éléphant"
            JungleLanguage.DE -> "🐘 Elefanten-Vormarsch"
            }
        StrategicDoctrine.RAT_SUBMERGE ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🐭 灵鼠潜河入穴"
            JungleLanguage.ZH_HANT -> "🐭 靈鼠潛河入穴"
            JungleLanguage.EN -> "🐭 Rat River Infiltration"
            JungleLanguage.JA -> "🐭 鼠の水中潜行"
            JungleLanguage.KO -> "🐭 쥐 수중 침투"
            JungleLanguage.ES -> "🐭 Infiltración de Rata"
            JungleLanguage.FR -> "🐭 Infiltration du Rat"
            JungleLanguage.DE -> "🐭 Ratten-Infiltration"
            }
        StrategicDoctrine.PACK_HUNT ->
            when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🐺 狼豹多路包抄"
            JungleLanguage.ZH_HANT -> "🐺 狼豹多路包抄"
            JungleLanguage.EN -> "🐺 Wolf & Leopard Flank"
            JungleLanguage.JA -> "🐺 狼豹包囲網"
            JungleLanguage.KO -> "🐺 늑대 표범 협공"
            JungleLanguage.ES -> "🐺 Flanqueo de Lobos"
            JungleLanguage.FR -> "🐺 Attaque en Meute"
            JungleLanguage.DE -> "🐺 Wolf-Leopard-Flanken"
            }
        }
    }
    
    val recommendedMoveBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🎯 推荐走这"
        JungleLanguage.EN -> "🎯 Best Move"
        JungleLanguage.JA -> "🎯 おすすめ"
        JungleLanguage.KO -> "🎯 추천 수"
        JungleLanguage.ES -> "🎯 Mejor Jugada"
        JungleLanguage.FR -> "🎯 Meilleur Coup"
        JungleLanguage.DE -> "🎯 Bester Zug"
        }
    }

    val recommendedSourceBadge: String
        get() {
            return when (currentLanguage) {
                JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "💡走它"
                JungleLanguage.EN -> "💡Move"
                JungleLanguage.JA -> "💡推奨"
                JungleLanguage.KO -> "💡추천"
                JungleLanguage.ES -> "💡Mover"
                JungleLanguage.FR -> "💡Jouer"
                JungleLanguage.DE -> "💡Ziehen"
            }
        }

    val recommendedTargetBadge: String
        get() {
            return when (currentLanguage) {
                JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🎯走这"
                JungleLanguage.EN -> "🎯Here"
                JungleLanguage.JA -> "🎯ここへ"
                JungleLanguage.KO -> "🎯여기로"
                JungleLanguage.ES -> "🎯Aquí"
                JungleLanguage.FR -> "🎯Ici"
                JungleLanguage.DE -> "🎯Hier"
            }
        }

    val lastOpponentMoveBadge: String
        get() {
            return when (currentLanguage) {
                JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "⚡敌步"
                JungleLanguage.EN -> "⚡Opponent"
                JungleLanguage.JA -> "⚡相手の手"
                JungleLanguage.KO -> "⚡상대의 수"
                JungleLanguage.ES -> "⚡Último"
                JungleLanguage.FR -> "⚡Adversaire"
                JungleLanguage.DE -> "⚡Gegner"
            }
        }

    val lastOpponentSourceBadge: String
        get() {
            return when (currentLanguage) {
                JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "起点"
                JungleLanguage.EN -> "From"
                JungleLanguage.JA -> "始点"
                JungleLanguage.KO -> "시작"
                JungleLanguage.ES -> "Desde"
                JungleLanguage.FR -> "Départ"
                JungleLanguage.DE -> "Start"
            }
        }
    
    fun recommendedMoveHeadline(type: AnimalType): String {
        val name = animalName(type)
        return when (currentLanguage) {
        JungleLanguage.ZH_HANS -> "👉 推荐走法: 移动【${name}】"
        JungleLanguage.ZH_HANT -> "👉 推薦走法: 移動【${name}】"
        JungleLanguage.EN -> "👉 Best Move: Move [${name}]"
        JungleLanguage.JA -> "👉 推奨手: [${name}]を進める"
        JungleLanguage.KO -> "👉 추천 수: [${name}] 이동"
        JungleLanguage.ES -> "👉 Jugada recomendada: Mover [${name}]"
        JungleLanguage.FR -> "👉 Coup recommandé : Déplacer [${name}]"
        JungleLanguage.DE -> "👉 Empfohlener Zug: [${name}] ziehen"
        }
    }
    
    val recommendedMoveDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "向前推进阵型，压制红方！"
        JungleLanguage.ZH_HANT -> "向前推進陣型，壓制紅方！"
        JungleLanguage.EN -> "Advance pieces to maintain pressure!"
        JungleLanguage.JA -> "前進して陣形を押し上げ、有利を拡大！"
        JungleLanguage.KO -> "진형을 전진시켜 상대를 압박하세요!"
        JungleLanguage.ES -> "¡Avanza la formación para presionar al rival!"
        JungleLanguage.FR -> "Avancez la formation pour maintenir la pression !"
        JungleLanguage.DE -> "Formierung vorrücken und Druck aufbauen!"
        }
    }
    
    fun forecastStep1(type: AnimalType): String {
        val name = animalName(type)
        return when (currentLanguage) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${name}】向前推进"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${name}】向前推進"
        JungleLanguage.EN -> "1️⃣ Advance [${name}]"
        JungleLanguage.JA -> "1️⃣ 味方 [${name}] 前進"
        JungleLanguage.KO -> "1️⃣ 아군 [${name}] 전진"
        JungleLanguage.ES -> "1️⃣ Avanza [${name}]"
        JungleLanguage.FR -> "1️⃣ Avancer [${name}]"
        JungleLanguage.DE -> "1️⃣ [${name}] vorrücken"
        }
    }
    
        fun forecastStep2(type: AnimalType?): String {
        return if (type != null) {
            val name = animalName(type)
            when (currentLanguage) {
                JungleLanguage.ZH_HANS -> "2️⃣ 对方【${name}】应对"
                JungleLanguage.ZH_HANT -> "2️⃣ 對方【${name}】應對"
                JungleLanguage.EN -> "2️⃣ Opponent [${name}] reacts"
                JungleLanguage.JA -> "2️⃣ 敵方 [${name}] 応戦"
                JungleLanguage.KO -> "2️⃣ 적 [${name}] 대응"
                JungleLanguage.ES -> "2️⃣ Rival [${name}] responde"
                JungleLanguage.FR -> "2️⃣ L'adversaire [${name}] réagit"
                JungleLanguage.DE -> "2️⃣ Gegner [${name}] reagiert"
            }
        } else {
            when (currentLanguage) {
                JungleLanguage.ZH_HANS -> "2️⃣ 对方应对"
                JungleLanguage.ZH_HANT -> "2️⃣ 對方應對"
                JungleLanguage.EN -> "2️⃣ Opponent reacts"
                JungleLanguage.JA -> "2️⃣ 敵方応戦"
                JungleLanguage.KO -> "2️⃣ 적 대응"
                JungleLanguage.ES -> "2️⃣ Rival responde"
                JungleLanguage.FR -> "2️⃣ L'adversaire réagit"
                JungleLanguage.DE -> "2️⃣ Gegner reagiert"
            }
        }
    }

    val forecastStep3: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "3️⃣ 我方掌控主动"
        JungleLanguage.EN -> "3️⃣ Seize board control"
        JungleLanguage.JA -> "3️⃣ 主導権を確保"
        JungleLanguage.KO -> "3️⃣ 주도권 장악"
        JungleLanguage.ES -> "3️⃣ Tomar la iniciativa"
        JungleLanguage.FR -> "3️⃣ Prendre l'initiative"
        JungleLanguage.DE -> "3️⃣ Initiative sichern"
        }
    }
    
    // MARK: - 🎯 妙手建议与对局提示全套国际化

    val adviceWinHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🏆 锁定胜局！直接入穴！"
        JungleLanguage.ZH_HANT -> "🏆 鎖定勝局！直接入穴！"
        JungleLanguage.EN -> "🏆 Winning Move!"
        JungleLanguage.JA -> "🏆 王手・決勝の一手！"
        JungleLanguage.KO -> "🏆 승리의 결정타!"
        JungleLanguage.ES -> "🏆 ¡Jugada Ganadora!"
        JungleLanguage.FR -> "🏆 Coup Gagnant !"
        JungleLanguage.DE -> "🏆 Siegeszug!"
        }
    }

    val adviceWinDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方大本营空虚！直接一步走入兽穴，立刻赢得棋局！"
        JungleLanguage.ZH_HANT -> "對方大本營空虛！直接一步走入獸穴，立刻贏得棋局！"
        JungleLanguage.EN -> "Enemy Den is unprotected! Capture it now to win immediately!"
        JungleLanguage.JA -> "敵の獣穴が空いています！今すぐ占領して勝利を掴みましょう！"
        JungleLanguage.KO -> "적 진영이 비었습니다! 즉시 진입하여 승리하세요!"
        JungleLanguage.ES -> "¡La guarida enemiga está desprotegida! ¡Ocúpala para ganar ya!"
        JungleLanguage.FR -> "La tanière ennemie est sans défense ! Capturez-la pour gagner !"
        JungleLanguage.DE -> "Gegnerische Höhle ist ungeschützt! Sofort betreten und gewinnen!"
        }
    }

    fun adviceWinForecast(piece: String, winner: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】走入大本营 ➔ 🏆 ${winner}获胜！"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】走入大本營 ➔ 🏆 ${winner}獲勝！"
        JungleLanguage.EN -> "1️⃣ [${piece}] enters Den ➔ 🏆 ${winner} Wins!"
        JungleLanguage.JA -> "1️⃣ 味方 [${piece}] 敵城突入 ➔ 🏆 ${winner} 勝利！"
        JungleLanguage.KO -> "1️⃣ 아군 [${piece}] 진영 침투 ➔ 🏆 ${winner} 승리!"
        JungleLanguage.ES -> "1️⃣ [${piece}] entra a guarida ➔ 🏆 ¡${winner} Gana!"
        JungleLanguage.FR -> "1️⃣ [${piece}] pénètre dans la tanière ➔ 🏆 ${winner} Gagne !"
        JungleLanguage.DE -> "1️⃣ [${piece}] zieht in Höhle ➔ 🏆 ${winner} Siegt!"
        }
    }

    val adviceWinBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🏆 占穴获胜"
        JungleLanguage.EN -> "🏆 Win Game"
        JungleLanguage.JA -> "🏆 入城勝利"
        JungleLanguage.KO -> "🏆 진영 점령"
        JungleLanguage.ES -> "🏆 Victoria"
        JungleLanguage.FR -> "🏆 Victoire"
        JungleLanguage.DE -> "🏆 Höhlen-Sieg"
        }
    }

    val adviceCounterKillHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🛡️【门前解围·化险为夷】"
        JungleLanguage.ZH_HANT -> "🛡️【門前解圍·化險為夷】"
        JungleLanguage.EN -> "🛡️【Defend Den·Resolve Threat】"
        JungleLanguage.JA -> "🛡️【本陣防衛・危機脱出】"
        JungleLanguage.KO -> "🛡️【진영 방어・위기 극복】"
        JungleLanguage.ES -> "🛡️【¡Defender Entrada!】"
        JungleLanguage.FR -> "🛡️【Défendre l'Entrée】"
        JungleLanguage.DE -> "🛡️【Höhleneingang sichern】"
        }
    }

    fun adviceCounterKillDetail(piece: String, inv: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "立刻用【${piece}】吃掉大本营门前的对方【${inv}】！"
        JungleLanguage.ZH_HANT -> "立刻用【${piece}】吃掉大本營門前的對方【${inv}】！"
        JungleLanguage.EN -> "Use [${piece}] to capture [${inv}] at your Den entrance!"
        JungleLanguage.JA -> "今すぐ[${piece}]で本陣前の[${inv}]を捕獲しましょう！"
        JungleLanguage.KO -> "[${piece}] 말로 본진 앞의 [${inv}] 말을 즉시 포획하세요!"
        JungleLanguage.ES -> "¡Usa tu [${piece}] para capturar al [${inv}] frente a la guarida!"
        JungleLanguage.FR -> "Utilisez [${piece}] pour intercepter le [${inv}] !"
        JungleLanguage.DE -> "Schlage [${inv}] vor der Höhle mit [${piece}]!"
        }
    }

    val adviceCounterKillBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🛡️ 门前解围"
        JungleLanguage.EN -> "🛡️ Defend Den"
        JungleLanguage.JA -> "🛡️ 本陣防衛"
        JungleLanguage.KO -> "🛡️ 진영 수호"
        JungleLanguage.ES -> "🛡️ Defender"
        JungleLanguage.FR -> "🛡️ Défendre"
        JungleLanguage.DE -> "🛡️ Verteidigen"
        }
    }

    val adviceBlockGateHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🛡️【稳守要道·保卫大本营】"
        JungleLanguage.ZH_HANT -> "🛡️【穩守要道·保衛大本營】"
        JungleLanguage.EN -> "🛡️【Block Choke Point】"
        JungleLanguage.JA -> "🛡️【要所封鎖・本陣防衛】"
        JungleLanguage.KO -> "🛡️【길목 차단・진영 방어】"
        JungleLanguage.ES -> "🛡️【¡Bloquear Paso Clave!】"
        JungleLanguage.FR -> "🛡️【Bloquer l'accès au repaire】"
        JungleLanguage.DE -> "🛡️【Engpass blockieren】"
        }
    }

    fun adviceBlockGateDetail(piece: String, inv: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方【${inv}】直逼大本营！立刻移动【${piece}】堵住要道！"
        JungleLanguage.ZH_HANT -> "對方【${inv}】直逼大本營！立刻移動【${piece}】堵住要道！"
        JungleLanguage.EN -> "Opponent [${inv}] approaches Den! Move [${piece}] to block the entrance!"
        JungleLanguage.JA -> "相手の[${inv}]が本陣へ迫っています！[${piece}]で要所を塞ぎましょう！"
        JungleLanguage.KO -> "상대 [${inv}] 말이 접근 중입니다! [${piece}] 말로 길목을 차단하세요!"
        JungleLanguage.ES -> "¡[${inv}] se acerca! Mueve [${piece}] para bloquear la entrada."
        JungleLanguage.FR -> "L'adversaire [${inv}] approche ! Déplacez [${piece}] pour bloquer l'accès."
        JungleLanguage.DE -> "Gegnerischer [${inv}] nähert sich! Mit [${piece}] den Eingang sperren!"
        }
    }

    val adviceBlockGateBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🛡️ 稳守要道"
        JungleLanguage.EN -> "🛡️ Block Den"
        JungleLanguage.JA -> "🛡️ 要所防衛"
        JungleLanguage.KO -> "🛡️ 입구 방어"
        JungleLanguage.ES -> "🛡️ Bloquear"
        JungleLanguage.FR -> "🛡️ Bloquer"
        JungleLanguage.DE -> "🛡️ Blockieren"
        }
    }

    val adviceFallBackHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🛡️【全速回防·守护家园】"
        JungleLanguage.ZH_HANT -> "🛡️【全速回防·守護家園】"
        JungleLanguage.EN -> "🛡️【Emergency Fallback】"
        JungleLanguage.JA -> "🛡️【全力救援・本陣急行】"
        JungleLanguage.KO -> "🛡️【긴급 회방・진영 복귀】"
        JungleLanguage.ES -> "🛡️【¡Regreso de Emergencia!】"
        JungleLanguage.FR -> "🛡️【Repli d'Urgence】"
        JungleLanguage.DE -> "🛡️【Notfall-Rückzug】"
        }
    }

    fun adviceFallBackDetail(piece: String, inv: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "大本营告急！立刻调动【${piece}】全速回防阻截【${inv}】！"
        JungleLanguage.ZH_HANT -> "大本營告急！立刻調動【${piece}】全速回防阻截【${inv}】！"
        JungleLanguage.EN -> "Den in danger! Rush [${piece}] back to intercept [${inv}]!"
        JungleLanguage.JA -> "本陣が危険です！[${piece}]を急行させて[${inv}]を阻止しましょう！"
        JungleLanguage.KO -> "진영이 위태롭습니다! [${piece}] 말을 이동시켜 [${inv}] 말을 저지하세요!"
        JungleLanguage.ES -> "¡Guarida en peligro! Retrocede [${piece}] para interceptar a [${inv}]."
        JungleLanguage.FR -> "Tanière en danger ! Rapatriez [${piece}] pour intercepter [${inv}]."
        JungleLanguage.DE -> "Höhle in Gefahr! Mit [${piece}] zurückeilen und [${inv}] abfangen!"
        }
    }

    val adviceFallBackBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🛡️ 紧急回防"
        JungleLanguage.EN -> "🛡️ Fall Back"
        JungleLanguage.JA -> "🛡️ 緊急帰還"
        JungleLanguage.KO -> "🛡️ 긴급 방어"
        JungleLanguage.ES -> "🛡️ Retroceder"
        JungleLanguage.FR -> "🛡️ Repli"
        JungleLanguage.DE -> "🛡️ Rückzug"
        }
    }

    fun adviceHomeDefenseForecast(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】紧急回防 ➔ 2️⃣ 对方推进受阻 ➔ 3️⃣ 稳固大本营"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】緊急回防 ➔ 2️⃣ 對方推進受阻 ➔ 3️⃣ 穩固大本營"
        JungleLanguage.EN -> "1️⃣ [${piece}] rushes back ➔ 2️⃣ Enemy attack stalled ➔ 3️⃣ Secure Den"
        JungleLanguage.JA -> "1️⃣ 味方 [${piece}] 救援 ➔ 2️⃣ 敵の進撃阻止 ➔ 3️⃣ 本陣防衛"
        JungleLanguage.KO -> "1️⃣ 아군 [${piece}] 복귀 ➔ 2️⃣ 적 공격 차단 ➔ 3️⃣ 진영 사수"
        JungleLanguage.ES -> "1️⃣ [${piece}] retrocede ➔ 2️⃣ Ataque rival frenado ➔ 3️⃣ Guarida segura"
        JungleLanguage.FR -> "1️⃣ [${piece}] se replie ➔ 2️⃣ Attaque ennemie bloquée ➔ 3️⃣ Tanière sécurisée"
        JungleLanguage.DE -> "1️⃣ [${piece}] eilt zurück ➔ 2️⃣ Angriff gestoppt ➔ 3️⃣ Höhle gesichert"
        }
    }

    val adviceRatSwimEvadeHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🌊【潜水避险·【鼠】遁入水中】"
        JungleLanguage.ZH_HANT -> "🌊【潛水避險·【鼠】遁入水中】"
        JungleLanguage.EN -> "🌊【Dive into River·Safe Escape】"
        JungleLanguage.JA -> "🌊【水中退避！川へ潜入】"
        JungleLanguage.KO -> "🌊【잠수 도피! 강으로 입수】"
        JungleLanguage.ES -> "🌊【Bucear en Río·Escape Seguro】"
        JungleLanguage.FR -> "🌊【Plongée dans la Rivière】"
        JungleLanguage.DE -> "🌊【Ins Wasser tauchen·Sicherer Rückzug】"
        }
    }

    val adviceRatSwimEvadeDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方棋子已贴身逼近！立刻跳入河中避险，陆地动物无法下水，绝对安全！"
        JungleLanguage.ZH_HANT -> "對方棋子已貼身逼近！立刻跳入河中避險，陸地動物無法下水，絕對安全！"
        JungleLanguage.EN -> "Threat close by! Jump into the river where land animals cannot follow!"
        JungleLanguage.JA -> "敵が接近中！陸の猛獣が入れない川へ飛び込み、安全を確保しましょう！"
        JungleLanguage.KO -> "적이 근접했습니다! 육지 맹수가 들어올 수 없는 강으로 피신하세요!"
        JungleLanguage.ES -> "¡Enemigo cerca! Salta al río donde las fieras terrestres no pueden entrar."
        JungleLanguage.FR -> "Menace proche ! Sautez dans la rivière, les fauves ne peuvent pas nager !"
        JungleLanguage.DE -> "Gegner nah! Springe in den Fluss, Landtiere können nicht folgen!"
        }
    }

    val adviceRatSwimEvadeBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🌊 遁水避险"
        JungleLanguage.EN -> "🌊 Dive River"
        JungleLanguage.JA -> "🌊 水中退避"
        JungleLanguage.KO -> "🌊 강으로 피신"
        JungleLanguage.ES -> "🌊 Al Río"
        JungleLanguage.FR -> "🌊 Nager"
        JungleLanguage.DE -> "🌊 Ins Wasser"
        }
    }

    val adviceRatSwimEvadeForecast: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【鼠】跳入河中 ➔ 2️⃣ 对方无法追赶 ➔ 3️⃣ 伺机渡河推进"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【鼠】跳入河中 ➔ 2️⃣ 對方無法追趕 ➔ 3️⃣ 伺機渡河推進"
        JungleLanguage.EN -> "1️⃣ Rat dives into river ➔ 2️⃣ Beasts cannot follow ➔ 3️⃣ Flank enemy Den"
        JungleLanguage.JA -> "1️⃣ 鼠が川へ潜入 ➔ 2️⃣ 敵追撃不能 ➔ 3️⃣ 対岸を伺う"
        JungleLanguage.KO -> "1️⃣ 쥐 강 입수 ➔ 2️⃣ 적 추격 불가 ➔ 3️⃣ 빈집 기습 노림"
        JungleLanguage.ES -> "1️⃣ Rata salta al río ➔ 2️⃣ Fieras no pueden seguir ➔ 3️⃣ Infiltrar guarida"
        JungleLanguage.FR -> "1️⃣ Le Rat plonge ➔ 2️⃣ Poursuite impossible ➔ 3️⃣ Contourner le repaire"
        JungleLanguage.DE -> "1️⃣ Ratte taucht ab ➔ 2️⃣ Gegner kann nicht folgen ➔ 3️⃣ Gegenangriff"
        }
    }

    val adviceRatFleeHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🚨【猛兽逼近·【鼠】撤离避险】"
        JungleLanguage.ZH_HANT -> "🚨【猛獸逼近·【鼠】撤離避險】"
        JungleLanguage.EN -> "🚨【Beast Nearby·Rat Retreat】"
        JungleLanguage.JA -> "🚨【猛獣接近！鼠の緊急回避】"
        JungleLanguage.KO -> "🚨【맹수 접근! 쥐 긴급 후퇴】"
        JungleLanguage.ES -> "🚨【¡Fiera Cerca! Retirada de Rata】"
        JungleLanguage.FR -> "🚨【Fauve proche ! Fuite du Rat】"
        JungleLanguage.DE -> "🚨【Raubtier nah! Ratte flieht】"
        }
    }

    val adviceRatFleeDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方棋子就在旁边！立刻向后或向小河方向移动一步脱离危险！"
        JungleLanguage.ZH_HANT -> "對方棋子就在旁邊！立刻向後或向小河方向移動一步脫離危險！"
        JungleLanguage.EN -> "Enemy predator is next to you! Step back towards the river to escape danger!"
        JungleLanguage.JA -> "敵が隣接しています！川や後方へ一歩下がって危険を回避しましょう！"
        JungleLanguage.KO -> "적 맹수가 바로 옆에 있습니다! 뒤나 강 방향으로 한 칸 물러나세요!"
        JungleLanguage.ES -> "¡Fiera enemiga al lado! Da un paso atrás o hacia el río para escapar."
        JungleLanguage.FR -> "Un prédateur est adjacent ! Reculez vers la rivière pour vous sauver !"
        JungleLanguage.DE -> "Raubtier steht neben dir! Nach hinten oder zum Fluss ausweichen!"
        }
    }

    val adviceRatFleeBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🛡️ 老鼠快跑"
        JungleLanguage.EN -> "🛡️ Rat Flee"
        JungleLanguage.JA -> "🛡️ 鼠逃走"
        JungleLanguage.KO -> "🛡️ 쥐 도망"
        JungleLanguage.ES -> "🛡️ Escapar"
        JungleLanguage.FR -> "🛡️ Fuir"
        JungleLanguage.DE -> "🛡️ Flucht"
        }
    }

    val adviceRatFleeForecast: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【鼠】脱离危险区 ➔ 2️⃣ 避开对方捕捉 ➔ 3️⃣ 保全关键棋子"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【鼠】脫離危險區 ➔ 2️⃣ 避開對方捕捉 ➔ 3️⃣ 保全關鍵棋子"
        JungleLanguage.EN -> "1️⃣ Rat leaves danger zone ➔ 2️⃣ Avoid predator ➔ 3️⃣ Keep piece safe"
        JungleLanguage.JA -> "1️⃣ 鼠が危険域を脱出 ➔ 2️⃣ 強敵の攻撃回避 ➔ 3️⃣ 貴重な戦力を温存"
        JungleLanguage.KO -> "1️⃣ 쥐 위험 구역 탈출 ➔ 2️⃣ 적 공격 회피 ➔ 3️⃣ 전력 보존"
        JungleLanguage.ES -> "1️⃣ Rata sale del peligro ➔ 2️⃣ Esquiva al cazador ➔ 3️⃣ Conserva la pieza"
        JungleLanguage.FR -> "1️⃣ Le Rat s'échappe ➔ 2️⃣ Évite le prédateur ➔ 3️⃣ Préserve la pièce"
        JungleLanguage.DE -> "1️⃣ Ratte entkommt ➔ 2️⃣ Schlag abgewendet ➔ 3️⃣ Figur gerettet"
        }
    }

    fun advicePieceRetreatHeadline(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🚨【紧急避险·【${piece}】快撤】"
        JungleLanguage.ZH_HANT -> "🚨【緊急避險·【${piece}】快撤】"
        JungleLanguage.EN -> "🚨【Tactical Retreat: [${piece}]】"
        JungleLanguage.JA -> "🚨【緊急退避！[${piece}]を引く】"
        JungleLanguage.KO -> "🚨【긴급 후퇴: [${piece}] 피신】"
        JungleLanguage.ES -> "🚨【Retirada Táctica: [${piece}]】"
        JungleLanguage.FR -> "🚨【Retraite Tactique : [${piece}]】"
        JungleLanguage.DE -> "🚨【Taktischer Rückzug: [${piece}]】"
        }
    }

    fun advicePieceRetreatDetail(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方大子正虎视眈眈！立刻将【${piece}】转移到安全格子避开锋芒！"
        JungleLanguage.ZH_HANT -> "對方大子正虎視眈眈！立刻將【${piece}】轉移到安全格子避開鋒芒！"
        JungleLanguage.EN -> "Enemy predators are lurking! Move [${piece}] to a safe tile immediately!"
        JungleLanguage.JA -> "敵が狙っています！今すぐ[${piece}]を安全なマスへ退避させましょう！"
        JungleLanguage.KO -> "상위 천적이 노리고 있습니다! [${piece}] 말을 안전한 곳으로 이동하세요!"
        JungleLanguage.ES -> "¡Te acecha un enemigo superior! Mueve [${piece}] a una casilla segura."
        JungleLanguage.FR -> "Un prédateur vous guette ! Mettez [${piece}] en sécurité immédiatement."
        JungleLanguage.DE -> "Stärkerer Gegner lauert! [${piece}] sofort auf sicheres Feld ziehen!"
        }
    }

    val advicePieceRetreatBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🛡️ 紧急避险"
        JungleLanguage.EN -> "🛡️ Evade Danger"
        JungleLanguage.JA -> "🛡️ 緊急退避"
        JungleLanguage.KO -> "🛡️ 위험 회피"
        JungleLanguage.ES -> "🛡️ Esquivar"
        JungleLanguage.FR -> "🛡️ Éviter"
        JungleLanguage.DE -> "🛡️ Ausweichen"
        }
    }

    fun advicePieceRetreatForecast(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】避险撤退 ➔ 2️⃣ 对方扑空 ➔ 3️⃣ 保留子力"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】避險撤退 ➔ 2️⃣ 對方撲空 ➔ 3️⃣ 保留子力"
        JungleLanguage.EN -> "1️⃣ [${piece}] retreats ➔ 2️⃣ Enemy strike misses ➔ 3️⃣ Preserve army"
        JungleLanguage.JA -> "1️⃣ [${piece}] 緊急退避 ➔ 2️⃣ 敵の攻撃空振り ➔ 3️⃣ 戦力維持"
        JungleLanguage.KO -> "1️⃣ [${piece}] 후퇴 ➔ 2️⃣ 적 공격 무산 ➔ 3️⃣ 전력 보존"
        JungleLanguage.ES -> "1️⃣ [${piece}] se retira ➔ 2️⃣ Rival falla ataque ➔ 3️⃣ Conserva fuerza"
        JungleLanguage.FR -> "1️⃣ [${piece}] recule ➔ 2️⃣ Attaque adverse déjouée ➔ 3️⃣ Garder la force"
        JungleLanguage.DE -> "1️⃣ [${piece}] weicht aus ➔ 2️⃣ Gegner schlägt ins Leere ➔ 3️⃣ Kraft bewahrt"
        }
    }

    val adviceHuntRiverRatHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🐭【水中清障·打通航道】"
        JungleLanguage.ZH_HANT -> "🐭【水中清障·打通航道】"
        JungleLanguage.EN -> "🐭【Clear River Rat·Open Path】"
        JungleLanguage.JA -> "🐭【水中戦！相手の鼠を捕獲し航路開放】"
        JungleLanguage.KO -> "🐭【수중전! 상대 쥐 포획 및 도하로 개방】"
        JungleLanguage.ES -> "🐭【Despejar Rata en Agua·Abrir Paso】"
        JungleLanguage.FR -> "🐭【Voie Fluviale·Prendre le Rat】"
        JungleLanguage.DE -> "🐭【Flussratte fangen·Weg frei】"
        }
    }

    val adviceHuntRiverRatDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方老鼠潜伏在水中阻挡猛虎跳河！立刻在水中将其吃掉，打通跳河路线！"
        JungleLanguage.ZH_HANT -> "對方老鼠潛伏在水中阻擋猛虎跳河！立刻在水中將其吃掉，打通跳河路線！"
        JungleLanguage.EN -> "Enemy Rat blocks the river! Capture it in water to clear the river-jumping path!"
        JungleLanguage.JA -> "相手の鼠が水中で跳河を妨害しています！水中で捕獲して渡河航路を開放しましょう！"
        JungleLanguage.KO -> "상대 쥐가 수중에서 호랑이의 도하를 막고 있습니다! 물속에서 잡아 도하로를 여세요!"
        JungleLanguage.ES -> "¡La rata rival bloquea el río! Captúrala en el agua para despejar el salto fluvial."
        JungleLanguage.FR -> "Le rat adverse bloque le saut ! Capturez-le dans l'eau pour libérer le passage."
        JungleLanguage.DE -> "Gegnerische Ratte blockiert den Sprung! Im Wasser schlagen und Weg freimachen!"
        }
    }

    val adviceHuntRiverRatBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🌊 破除水障"
        JungleLanguage.EN -> "🌊 Clear River"
        JungleLanguage.JA -> "🌊 航路開放"
        JungleLanguage.KO -> "🌊 수로 개방"
        JungleLanguage.ES -> "🌊 Despejar Río"
        JungleLanguage.FR -> "🌊 Voie Libre"
        JungleLanguage.DE -> "🌊 Fluss frei"
        }
    }

    val adviceHuntRiverRatForecast: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【鼠】水中吃掉对方老鼠 ➔ 2️⃣ 解除跳河阻挡 ➔ 3️⃣ 猛虎狮王跃河推进！"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【鼠】水中吃掉對方老鼠 ➔ 2️⃣ 解除跳河阻擋 ➔ 3️⃣ 猛虎獅王躍河推進！"
        JungleLanguage.EN -> "1️⃣ Rat captures enemy Rat ➔ 2️⃣ Unblock river jump ➔ 3️⃣ Tiger & Lion leap across!"
        JungleLanguage.JA -> "1️⃣ 鼠が相手の鼠を捕獲 ➔ 2️⃣ 跳河封鎖解除 ➔ 3️⃣ 獅子虎の渡河前進！"
        JungleLanguage.KO -> "1️⃣ 쥐가 상대 쥐 포획 ➔ 2️⃣ 도하 봉쇄 해제 ➔ 3️⃣ 호랑이 사자 도하 전진!"
        JungleLanguage.ES -> "1️⃣ Rata captura rata rival ➔ 2️⃣ Desbloquea salto ➔ 3️⃣ ¡Tigre y León saltan el río!"
        JungleLanguage.FR -> "1️⃣ Le Rat capture le rat rival ➔ 2️⃣ Voie libérée ➔ 3️⃣ Tigre et Lion sautent !"
        JungleLanguage.DE -> "1️⃣ Ratte schlägt Ratte ➔ 2️⃣ Flusssprung frei ➔ 3️⃣ Tiger & Löwe springen über!"
        }
    }

    val adviceUnderwaterPursuitHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🐭【水下推进·清除水障】"
        JungleLanguage.ZH_HANT -> "🐭【水下推進·清除水障】"
        JungleLanguage.EN -> "🐭【Underwater Stealth·Pursuit】"
        JungleLanguage.JA -> "🐭【水中前進！相手の鼠を牽制】"
        JungleLanguage.KO -> "🐭【수중 전진! 상대 쥐 압박】"
        JungleLanguage.ES -> "🐭【Persecución Submarina】"
        JungleLanguage.FR -> "🐭【Traque Aquatique】"
        JungleLanguage.DE -> "🐭【Unterwasser-Verfolgung】"
        }
    }

    val adviceUnderwaterPursuitDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "调动老鼠向水中对方老鼠靠近，准备清除水障！"
        JungleLanguage.ZH_HANT -> "調動老鼠向水中對方老鼠靠近，準備清除水障！"
        JungleLanguage.EN -> "Move your Rat closer to the enemy Rat in the river to clear the waterway!"
        JungleLanguage.JA -> "鼠を水中へ進め、相手の鼠を牽制して川を開放する準備をしましょう！"
        JungleLanguage.KO -> "쥐를 움직여 수중의 상대 쥐를 압박하고 수로를 확보하세요!"
        JungleLanguage.ES -> "¡Acerca tu rata al enemigo en el agua para despejar el río!"
        JungleLanguage.FR -> "Rapprochez votre Rat de l'adversaire pour libérer le fleuve !"
        JungleLanguage.DE -> "Ratte im Fluss näher an Gegner heranführen, um den Fluss zu befreien!"
        }
    }

    val adviceUnderwaterPursuitBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🌊 水下清障"
        JungleLanguage.EN -> "🌊 River Pursuit"
        JungleLanguage.JA -> "🌊 水中前進"
        JungleLanguage.KO -> "🌊 수중 전진"
        JungleLanguage.ES -> "🌊 Despejar"
        JungleLanguage.FR -> "🌊 Poursuite"
        JungleLanguage.DE -> "🌊 Verfolgung"
        }
    }

    val adviceUnderwaterPursuitForecast: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【鼠】下水进逼 ➔ 2️⃣ 化解水中阻碍 ➔ 3️⃣ 打通跳河路线"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【鼠】下水進逼 ➔ 2️⃣ 化解水中阻礙 ➔ 3️⃣ 打通跳河路線"
        JungleLanguage.EN -> "1️⃣ Rat advances in water ➔ 2️⃣ Corner enemy Rat ➔ 3️⃣ Clear river jump lane"
        JungleLanguage.JA -> "1️⃣ 鼠が水中へ前進 ➔ 2️⃣ 相手の鼠を牽制 ➔ 3️⃣ 渡河ルート確保"
        JungleLanguage.KO -> "1️⃣ 쥐 수중 전진 ➔ 2️⃣ 상대 쥐 압박 ➔ 3️⃣ 도하로 확보"
        JungleLanguage.ES -> "1️⃣ Rata avanza en río ➔ 2️⃣ Acorrala a rival ➔ 3️⃣ Abre paso al salto"
        JungleLanguage.FR -> "1️⃣ Le Rat avance dans l'eau ➔ 2️⃣ Bloque le rival ➔ 3️⃣ Prépare le saut"
        JungleLanguage.DE -> "1️⃣ Ratte rückt im Wasser vor ➔ 2️⃣ Gegner bedrängt ➔ 3️⃣ Sprungweg frei"
        }
    }

    val adviceProtectElephantHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🐱【灵巧捕鼠·护卫神象】"
        JungleLanguage.ZH_HANT -> "🐱【靈巧捕鼠·護衛神象】"
        JungleLanguage.EN -> "🐱【Guard Elephant·Take Rat】"
        JungleLanguage.JA -> "🐱【象の護衛！鼠を捕獲】"
        JungleLanguage.KO -> "🐱【코끼리 호위! 쥐 포획】"
        JungleLanguage.ES -> "🐱【Capturar Rata·Proteger Elefante】"
        JungleLanguage.FR -> "🐱【Prendre le Rat·Protéger l'Éléphant】"
        JungleLanguage.DE -> "🐱【Ratte fangen·Elefant schützen】"
        }
    }

    fun adviceProtectElephantDetail(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方老鼠逼近大象！立刻调动【${piece}】将其吃掉，解除大象危机！"
        JungleLanguage.ZH_HANT -> "對方老鼠逼近大象！立刻調動【${piece}】將其吃掉，解除大象危機！"
        JungleLanguage.EN -> "Enemy Rat threatens your Elephant! Use [${piece}] to eliminate the Rat!"
        JungleLanguage.JA -> "相手の鼠が象に迫っています！[${piece}]で捕獲して象の危機を救いましょう！"
        JungleLanguage.KO -> "상대 쥐가 코끼리를 노립니다! [${piece}] 말로 즉시 포획하여 위기를 넘기세요!"
        JungleLanguage.ES -> "¡Rata enemiga cerca del Elefante! ¡Usa [${piece}] para atraparla!"
        JungleLanguage.FR -> "Le rat adverse menace l'Éléphant ! Utilisez [${piece}] pour le neutraliser !"
        JungleLanguage.DE -> "Gegnerische Ratte bedroht den Elefanten! Mit [${piece}] Ratte schlagen!"
        }
    }

    val adviceProtectElephantBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🐱 捕鼠护象"
        JungleLanguage.EN -> "🐱 Guard Elephant"
        JungleLanguage.JA -> "🐱 鼠退治護衛"
        JungleLanguage.KO -> "🐱 코끼리 보호"
        JungleLanguage.ES -> "🐱 Proteger Elefante"
        JungleLanguage.FR -> "🐱 Protéger l'Éléphant"
        JungleLanguage.DE -> "🐱 Elefant beschützen"
        }
    }

    fun adviceProtectElephantForecast(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】吃掉对方老鼠 ➔ 2️⃣ 大象危机解除 ➔ 3️⃣ 大象稳步推进！"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】吃掉對方老鼠 ➔ 2️⃣ 大象危機解除 ➔ 3️⃣ 大象穩步推進！"
        JungleLanguage.EN -> "1️⃣ [${piece}] captures Rat ➔ 2️⃣ Elephant danger resolved ➔ 3️⃣ Elephant charges freely!"
        JungleLanguage.JA -> "1️⃣ [${piece}] 敵鼠捕獲 ➔ 2️⃣ 象の危機解除 ➔ 3️⃣ 巨象が猛進撃！"
        JungleLanguage.KO -> "1️⃣ [${piece}] 쥐 처치 ➔ 2️⃣ 코끼리 위기 해소 ➔ 3️⃣ 코끼리 거침없는 진격!"
        JungleLanguage.ES -> "1️⃣ [${piece}] captura Rata ➔ 2️⃣ Elefante a salvo ➔ 3️⃣ ¡Elefante imparable!"
        JungleLanguage.FR -> "1️⃣ [${piece}] élimine le Rat ➔ 2️⃣ Danger écarté ➔ 3️⃣ L'Éléphant est inarrêtable !"
        JungleLanguage.DE -> "1️⃣ [${piece}] schlägt Ratte ➔ 2️⃣ Gefahr gebannt ➔ 3️⃣ Elefant stürmt ungehindert vor!"
        }
    }

    val adviceElephantEscapeHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🐘【大象避险·紧急脱离】"
        JungleLanguage.ZH_HANT -> "🐘【大象避險·緊急脫離】"
        JungleLanguage.EN -> "🐘【Elephant Evade·Flee Rat】"
        JungleLanguage.JA -> "🐘【巨象脱出！鼠から退避】"
        JungleLanguage.KO -> "🐘【코끼리 피신! 쥐로부터 탈출】"
        JungleLanguage.ES -> "🐘【Escape de Elefante·Evitar Rata】"
        JungleLanguage.FR -> "🐘【Évasion de l'Éléphant】"
        JungleLanguage.DE -> "🐘【Elefant weicht Ratte aus】"
        }
    }

    val adviceElephantEscapeDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方老鼠近在咫尺！大象立刻退开脱离危险！"
        JungleLanguage.ZH_HANT -> "對方老鼠近在咫尺！大象立刻退開脫離危險！"
        JungleLanguage.EN -> "Enemy Rat is adjacent! Retreat Elephant immediately to avoid capture!"
        JungleLanguage.JA -> "相手の鼠が隣にいます！象を一歩引いて危険から逃れましょう！"
        JungleLanguage.KO -> "상대 쥐가 인접했습니다! 코끼리를 즉시 물러나게 하여 위기를 피하세요!"
        JungleLanguage.ES -> "¡La rata enemiga está al lado! ¡Mueve el Elefante para ponerlo a salvo!"
        JungleLanguage.FR -> "Le rat adverse est adjacent ! Reculez l'Éléphant immédiatement !"
        JungleLanguage.DE -> "Ratte steht direkt daneben! Elefant sofort außer Reichweite ziehen!"
        }
    }

    val adviceElephantEscapeBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🐘 大象避险"
        JungleLanguage.EN -> "🐘 Elephant Flee"
        JungleLanguage.JA -> "🐘 象退避"
        JungleLanguage.KO -> "🐘 코끼리 피신"
        JungleLanguage.ES -> "🐘 Elefante a Salvo"
        JungleLanguage.FR -> "🐘 Éléphant Sauf"
        JungleLanguage.DE -> "🐘 Elefant in Sicherheit"
        }
    }

    val adviceElephantEscapeForecast: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 大象拉开距离 ➔ 2️⃣ 猫狗配合 ➔ 3️⃣ 稳妥吃掉老鼠"
        JungleLanguage.ZH_HANT -> "1️⃣ 大象拉開距離 ➔ 2️⃣ 貓狗配合 ➔ 3️⃣ 穩妥吃掉老鼠"
        JungleLanguage.EN -> "1️⃣ Elephant steps back ➔ 2️⃣ Cat/Dog flank ➔ 3️⃣ Eliminate threat"
        JungleLanguage.JA -> "1️⃣ 象が距離を取る ➔ 2️⃣ 猫犬で連携 ➔ 3️⃣ 確実に鼠を捕獲"
        JungleLanguage.KO -> "1️⃣ 코끼리 거리 벌림 ➔ 2️⃣ 고양이·개 연계 ➔ 3️⃣ 쥐 안정적 포획"
        JungleLanguage.ES -> "1️⃣ Elefante se aleja ➔ 2️⃣ Gato/Perro flanquean ➔ 3️⃣ Eliminar rata"
        JungleLanguage.FR -> "1️⃣ L'Éléphant prend distance ➔ 2️⃣ Chat/Chien encerclent ➔ 3️⃣ Neutraliser le rat"
        JungleLanguage.DE -> "1️⃣ Elefant gewinnt Distanz ➔ 2️⃣ Katze/Hund flankieren ➔ 3️⃣ Ratte ausschalten"
        }
    }

    val adviceFlankRatHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🐺【合围老鼠·护卫大象】"
        JungleLanguage.ZH_HANT -> "🐺【合圍老鼠·護衛大象】"
        JungleLanguage.EN -> "🐺【Flank Rat·Protect Elephant】"
        JungleLanguage.JA -> "🐺【鼠包囲！象を守護】"
        JungleLanguage.KO -> "🐺【쥐 포위! 코끼리 호위】"
        JungleLanguage.ES -> "🐺【Acorralar Rata·Guardar Elefante】"
        JungleLanguage.FR -> "🐺【Encercler le Rat·Garder l'Éléphant】"
        JungleLanguage.DE -> "🐺【Ratte umstellen·Elefant decken】"
        }
    }

    fun adviceFlankRatDetail(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "调动【${piece}】向对方老鼠配合合围，保护大象！"
        JungleLanguage.ZH_HANT -> "調動【${piece}】向對方老鼠配合合圍，保護大象！"
        JungleLanguage.EN -> "Move [${piece}] closer to pinch the enemy Rat and keep Elephant safe!"
        JungleLanguage.JA -> "[${piece}]で相手の鼠に接近して包囲網を敷き、象を守りましょう！"
        JungleLanguage.KO -> "[${piece}] 말로 상대 쥐를 압박하여 코끼리를 안전하게 지키세요!"
        JungleLanguage.ES -> "¡Mueve [${piece}] para cortar el paso a la rata y blindar al Elefante!"
        JungleLanguage.FR -> "Avancez [${piece}] pour encercler le rat et sécuriser l'Éléphant !"
        JungleLanguage.DE -> "Mit [${piece}] die Ratte stellen und den Elefanten absichern!"
        }
    }

    val adviceFlankRatBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🐺 合围老鼠"
        JungleLanguage.EN -> "🐺 Flank Rat"
        JungleLanguage.JA -> "🐺 鼠包囲"
        JungleLanguage.KO -> "🐺 쥐 포위"
        JungleLanguage.ES -> "🐺 Acorralar"
        JungleLanguage.FR -> "🐺 Encercler"
        JungleLanguage.DE -> "🐺 Umstellen"
        }
    }

    fun adviceFlankRatForecast(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】合围老鼠 ➔ 2️⃣ 逼退老鼠 ➔ 3️⃣ 护卫大象"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】合圍老鼠 ➔ 2️⃣ 逼退老鼠 ➔ 3️⃣ 護衛大象"
        JungleLanguage.EN -> "1️⃣ [${piece}] corners Rat ➔ 2️⃣ Force Rat back ➔ 3️⃣ Guard Elephant"
        JungleLanguage.JA -> "1️⃣ [${piece}] 包囲 ➔ 2️⃣ 相手の鼠を後退させる ➔ 3️⃣ 象を護衛"
        JungleLanguage.KO -> "1️⃣ [${piece}] 포위 ➔ 2️⃣ 상대 쥐 격퇴 ➔ 3️⃣ 코끼리 수호"
        JungleLanguage.ES -> "1️⃣ [${piece}] acorrala a rata ➔ 2️⃣ Rata retrocede ➔ 3️⃣ Elefante a salvo"
        JungleLanguage.FR -> "1️⃣ [${piece}] cerne le rat ➔ 2️⃣ Fait fuir le rival ➔ 3️⃣ Éléphant protégé"
        JungleLanguage.DE -> "1️⃣ [${piece}] bedrängt Ratte ➔ 2️⃣ Ratte weicht zurück ➔ 3️⃣ Elefant sicher"
        }
    }

    fun adviceProfitableTradeHeadline(piece: String, cap: String, isRatElephant: Boolean): String {
        return if (isRatElephant) {
            return when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "⚡️【棋局大赚·以鼠换象】"
            JungleLanguage.ZH_HANT -> "⚡️【棋局大賺·以鼠換象】"
            JungleLanguage.EN -> "⚡️【High-Value Trade: Rat for Elephant!】"
            JungleLanguage.JA -> "⚡️【超好手！鼠で敵巨象を相討ち】"
            JungleLanguage.KO -> "⚡️【대이득! 쥐로 코끼리 교환】"
            JungleLanguage.ES -> "⚡️【¡Gran Canje: Rata por Elefante!】"
            JungleLanguage.FR -> "⚡️【Échange Royal : Rat contre Éléphant !】"
            JungleLanguage.DE -> "⚡️【Genialer Tausch: Ratte gegen Elefant!】"
            }
        } else {
            return when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "⚡️【巧妙兑子·以${piece}换${cap}】"
            JungleLanguage.ZH_HANT -> "⚡️【巧妙兌子·以${piece}換${cap}】"
            JungleLanguage.EN -> "⚡️【Favorable Trade: [${piece}] for [${cap}]】"
            JungleLanguage.JA -> "⚡️【有利な交換！[${piece}]で[${cap}]を取る】"
            JungleLanguage.KO -> "⚡️【유리한 교환: [${piece}]로 [${cap}] 포획】"
            JungleLanguage.ES -> "⚡️【Intercambio Favorable: [${piece}] por [${cap}]】"
            JungleLanguage.FR -> "⚡️【Échange Favorable : [${piece}] contre [${cap}]】"
            JungleLanguage.DE -> "⚡️【Günstiger Abtausch: [${piece}] gegen [${cap}]】"
            }
        }
    }

    fun adviceProfitableTradeDetail(cap: String, netProfit: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "吃掉【${cap}】后虽会被反吃，但我方用小换大，净赚 +${netProfit} 子力优势，大赚！"
        JungleLanguage.ZH_HANT -> "吃掉【${cap}】後雖會被反吃，但我方用小換大，淨賺 +${netProfit} 子力優勢，大賺！"
        JungleLanguage.EN -> "Trading [${cap}] will invite counter-attack, but you gain +${netProfit} net army advantage!"
        JungleLanguage.JA -> "[${cap}]捕獲後に反撃を受けますが、戦力差 +${netProfit} の大幅黒字交換です！"
        JungleLanguage.KO -> "[${cap}]을 잡은 후 반격을 받지만, 전투력 +${netProfit} 이득인 훌륭한 교환입니다!"
        JungleLanguage.ES -> "¡Aunque contraataquen tras comer [${cap}], ganas +${netProfit} de ventaja táctica!"
        JungleLanguage.FR -> "Capturer [${cap}] provoquera une riposte, mais vous gagnez +${netProfit} en valeur !"
        JungleLanguage.DE -> "Das Schlagen von [${cap}] bringt Gegenangriff, aber +${netProfit} taktischen Vorteil!"
        }
    }

    fun adviceProfitableTradeBadge(netProfit: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🟣 净赚+${netProfit}"
        JungleLanguage.EN -> "🟣 Gain +${netProfit}"
        JungleLanguage.JA -> "🟣 得+${netProfit}"
        JungleLanguage.KO -> "🟣 이득+${netProfit}"
        JungleLanguage.ES -> "🟣 Gana +${netProfit}"
        JungleLanguage.FR -> "🟣 Gain +${netProfit}"
        JungleLanguage.DE -> "🟣 Gewinn+${netProfit}"
        }
    }

    val enemyStrongPiece: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "对方棋子"
        JungleLanguage.EN -> "Enemy"
        JungleLanguage.JA -> "相手の駒"
        JungleLanguage.KO -> "상대 말"
        JungleLanguage.ES -> "Fiera rival"
        JungleLanguage.FR -> "Fauve ennemi"
        JungleLanguage.DE -> "Gegner"
        }
    }

    fun adviceProfitableTradeForecast(piece: String, cap: String, capVal: Int, oppKiller: String, pieceVal: Int, netProfit: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】吃【${cap}】(+${capVal}) ➔ 2️⃣ 对方【${oppKiller}】吃【${piece}】(-${pieceVal}) ➔ 3️⃣ 我方净赚 +${netProfit} 子力优势！"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】吃【${cap}】(+${capVal}) ➔ 2️⃣ 對方【${oppKiller}】吃【${piece}】(-${pieceVal}) ➔ 3️⃣ 我方淨賺 +${netProfit} 子力優勢！"
        JungleLanguage.EN -> "1️⃣ [${piece}] eats [${cap}] (+${capVal}) ➔ 2️⃣ Opponent [${oppKiller}] eats [${piece}] (-${pieceVal}) ➔ 3️⃣ Net gain +${netProfit}!"
        JungleLanguage.JA -> "1️⃣ [${piece}]が[${cap}]を捕獲 (+${capVal}) ➔ 2️⃣ 敵[${oppKiller}]が反撃 (-${pieceVal}) ➔ 3️⃣ 差引 +${netProfit} の大勝利！"
        JungleLanguage.KO -> "1️⃣ [${piece}]로 [${cap}] 잡음 (+${capVal}) ➔ 2️⃣ 적 [${oppKiller}] 반격 (-${pieceVal}) ➔ 3️⃣ 순수 +${netProfit} 이득!"
        JungleLanguage.ES -> "1️⃣ [${piece}] come [${cap}] (+${capVal}) ➔ 2️⃣ Rival [${oppKiller}] contraataca (-${pieceVal}) ➔ 3️⃣ ¡Ganancia neta +${netProfit}!"
        JungleLanguage.FR -> "1️⃣ [${piece}] prend [${cap}] (+${capVal}) ➔ 2️⃣ L'adversaire riposte (-${pieceVal}) ➔ 3️⃣ Gain net +${netProfit} !"
        JungleLanguage.DE -> "1️⃣ [${piece}] schlägt [${cap}] (+${capVal}) ➔ 2️⃣ Gegnertausch [${oppKiller}] (-${pieceVal}) ➔ 3️⃣ Netto-Vorteil +${netProfit}!"
        }
    }

    val adviceBadTradeHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "⚠️【亏本兑子·不建议走】"
        JungleLanguage.ZH_HANT -> "⚠️【虧本兌子·不建議走】"
        JungleLanguage.EN -> "⚠️【Unfavorable Trade·Caution】"
        JungleLanguage.JA -> "⚠️【損な交換！非推奨】"
        JungleLanguage.KO -> "⚠️【손해 교환! 비추천】"
        JungleLanguage.ES -> "⚠️【Intercambio Desfavorable】"
        JungleLanguage.FR -> "⚠️【Échange Désavantageux】"
        JungleLanguage.DE -> "⚠️【Verlustreicher Abtausch】"
        }
    }

    fun adviceBadTradeDetail(cap: String, loss: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "吃掉【${cap}】后会被反吃，损失 ${loss} 点子力！"
        JungleLanguage.ZH_HANT -> "吃掉【${cap}】後會被反吃，損失 ${loss} 點子力！"
        JungleLanguage.EN -> "Capturing [${cap}] leads to losing valuable piece, net loss -${loss}!"
        JungleLanguage.JA -> "[${cap}]を捕獲すると反撃され、-${loss} の戦力損失になります！"
        JungleLanguage.KO -> "[${cap}]을 잡은 후 반격당해 -${loss} 손해가 발생합니다!"
        JungleLanguage.ES -> "¡Comer [${cap}] causará contraataque y pérdida de -${loss}!"
        JungleLanguage.FR -> "Prendre [${cap}] mènera à une riposte avec perte de -${loss} !"
        JungleLanguage.DE -> "Das Schlagen von [${cap}] führt zu Gegenangriff und Verlust von -${loss}!"
        }
    }

    val adviceBadTradeBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "⚠️ 亏损兑子"
        JungleLanguage.EN -> "⚠️ Loss Trade"
        JungleLanguage.JA -> "⚠️ 損失交換"
        JungleLanguage.KO -> "⚠️ 손해 교환"
        JungleLanguage.ES -> "⚠️ Mal Canje"
        JungleLanguage.FR -> "⚠️ Mauvais Troc"
        JungleLanguage.DE -> "⚠️ Verlust-Zug"
        }
    }

    fun adviceBadTradeForecast(cap: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 吃【${cap}】 ➔ 2️⃣ 我方被反吃 ➔ 3️⃣ 损失子力"
        JungleLanguage.ZH_HANT -> "1️⃣ 吃【${cap}】 ➔ 2️⃣ 我方被反吃 ➔ 3️⃣ 損失子力"
        JungleLanguage.EN -> "1️⃣ Eat [${cap}] ➔ 2️⃣ Countered ➔ 3️⃣ Net army loss"
        JungleLanguage.JA -> "1️⃣ [${cap}] 捕獲 ➔ 2️⃣ 反撃を受ける ➔ 3️⃣ 戦力低下"
        JungleLanguage.KO -> "1️⃣ [${cap}] 포획 ➔ 2️⃣ 반격당함 ➔ 3️⃣ 전력 손실"
        JungleLanguage.ES -> "1️⃣ Come [${cap}] ➔ 2️⃣ Contraataque ➔ 3️⃣ Pérdida táctica"
        JungleLanguage.FR -> "1️⃣ Prendre [${cap}] ➔ 2️⃣ Contre-attaqué ➔ 3️⃣ Perte nette"
        JungleLanguage.DE -> "1️⃣ [${cap}] schlagen ➔ 2️⃣ Ausgeschaltet ➔ 3️⃣ Punkteverlust"
        }
    }

    fun adviceFreeCaptureHeadline(cap: String, isJumpRiver: Boolean): String {
        return if (isJumpRiver) {
            return when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "🌊【飞河巧妙夺取·吃掉${cap}】"
            JungleLanguage.ZH_HANT -> "🌊【飛河巧妙奪取·吃掉${cap}】"
            JungleLanguage.EN -> "🌊【River Jump Strike: Capture ${cap}】"
            JungleLanguage.JA -> "🌊【渡河急襲！無傷で[${cap}]捕獲】"
            JungleLanguage.KO -> "🌊【도하 급습! [${cap}] 무손실 포획】"
            JungleLanguage.ES -> "🌊【Salto Fluvial: Captura a ${cap}】"
            JungleLanguage.FR -> "🌊【Frappe par Saut : Capture ${cap}】"
            JungleLanguage.DE -> "🌊【Flusssprung-Schlag: ${cap} schlagen】"
            }
        } else {
            return when (currentLanguage ) {
            JungleLanguage.ZH_HANS -> "👑【巧妙夺取·吃掉${cap}】"
            JungleLanguage.ZH_HANT -> "👑【巧妙奪取·吃掉${cap}】"
            JungleLanguage.EN -> "👑【Free Capture: Take [${cap}]】"
            JungleLanguage.JA -> "👑【無傷撃破！[${cap}]を捕獲】"
            JungleLanguage.KO -> "👑【무손실 포획: [${cap}] 잡기】"
            JungleLanguage.ES -> "👑【Captura Gratuita: Come [${cap}]】"
            JungleLanguage.FR -> "👑【Capture Sans Risque : Prendre [${cap}]】"
            JungleLanguage.DE -> "👑【Verlustfreier Schlag: ${cap} fangen】"
            }
        }
    }

    fun adviceFreeCaptureDetail(piece: String, cap: String, value: Int): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "用【${piece}】吃掉对方【${cap}】后我方绝对安全，净得 +${value} 点子力优势！"
        JungleLanguage.ZH_HANT -> "用【${piece}】吃掉對方【${cap}】後我方絕對安全，淨得 +${value} 點子力優勢！"
        JungleLanguage.EN -> "Use [${piece}] to take [${cap}] safely! You gain free +${value} army value!"
        JungleLanguage.JA -> "[${piece}]で[${cap}]を捕獲後も安全！無傷で +${value} の戦力獲得！"
        JungleLanguage.KO -> "[${piece}]로 적 [${cap}]을 잡은 후 완전히 안전합니다! +${value} 이득!"
        JungleLanguage.ES -> "¡Usa [${piece}] para comer [${cap}] sin peligro! Ganas +${value} gratis."
        JungleLanguage.FR -> "Capturez [${cap}] avec [${piece}] en toute sécurité ! +${value} net !"
        JungleLanguage.DE -> "Mit [${piece}] [${cap}] gefahrlos schlagen! Freier Gewinn von +${value}!"
        }
    }

    fun adviceFreeCaptureBadge(cap: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "👑 无损吃${cap}"
        JungleLanguage.EN -> "👑 Free ${cap}"
        JungleLanguage.JA -> "👑 ${cap}無傷捕獲"
        JungleLanguage.KO -> "👑 ${cap} 무료포획"
        JungleLanguage.ES -> "👑 Come ${cap}"
        JungleLanguage.FR -> "👑 Prendre ${cap}"
        JungleLanguage.DE -> "👑 ${cap} schlagen"
        }
    }

    fun adviceFreeCaptureForecast(piece: String, cap: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "1️⃣ 我方【${piece}】吃掉【${cap}】 ➔ 2️⃣ 对方防守受挫 ➔ 3️⃣ 扩大优势！"
        JungleLanguage.ZH_HANT -> "1️⃣ 我方【${piece}】吃掉【${cap}】 ➔ 2️⃣ 對方防守受挫 ➔ 3️⃣ 擴大優勢！"
        JungleLanguage.EN -> "1️⃣ [${piece}] captures [${cap}] safely ➔ 2️⃣ Enemy line breaks ➔ 3️⃣ Total control!"
        JungleLanguage.JA -> "1️⃣ [${piece}]が無傷で[${cap}]捕獲 ➔ 2️⃣ 相手の防衛線崩壊 ➔ 3️⃣ 圧倒的優勢！"
        JungleLanguage.KO -> "1️⃣ [${piece}] 무손실 포획 ➔ 2️⃣ 상대 방어선 붕괴 ➔ 3️⃣ 압도적 우위!"
        JungleLanguage.ES -> "1️⃣ [${piece}] captura [${cap}] seguro ➔ 2️⃣ Defensa rival rota ➔ 3️⃣ ¡Control total!"
        JungleLanguage.FR -> "1️⃣ [${piece}] prend [${cap}] sans danger ➔ 2️⃣ Ligne ennemie brisée ➔ 3️⃣ Domination !"
        JungleLanguage.DE -> "1️⃣ [${piece}] schlägt [${cap}] sicher ➔ 2️⃣ Gegnertruppe wankt ➔ 3️⃣ Totale Dominanz!"
        }
    }

    val adviceAssaultDenHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "🏰【直取兽穴·锁定胜局】"
        JungleLanguage.ZH_HANT -> "🏰【直取獸穴·鎖定勝局】"
        JungleLanguage.EN -> "🏰【Den Assault·Final Move】"
        JungleLanguage.JA -> "🏰【敵本陣到達！勝利の一手】"
        JungleLanguage.KO -> "🏰【진영 돌파! 최종 입수】"
        JungleLanguage.ES -> "🏰【Entrar a la Guarida·Victoria】"
        JungleLanguage.FR -> "🏰【Entrée dans le Repaire·Victoire】"
        JungleLanguage.DE -> "🏰【Höhlen-Besetzung·Siegzug】"
        }
    }

    val adviceAssaultDenDetail: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "对方大本营就在眼前！稳步走入大本营锁定胜局！"
        JungleLanguage.ZH_HANT -> "對方大本營就在眼前！穩步走入大本營鎖定勝局！"
        JungleLanguage.EN -> "Enemy Den is right ahead! Step in to secure immediate victory!"
        JungleLanguage.JA -> "相手の本陣は目の前です！堂々と突入して勝利を決めましょう！"
        JungleLanguage.KO -> "상대 진영이 바로 앞입니다! 진입하여 승리를 확정하세요!"
        JungleLanguage.ES -> "¡La guarida rival está a tiro! ¡Entra y sentencia la partida!"
        JungleLanguage.FR -> "La tanière adverse est à portée ! Entrez pour sceller la victoire !"
        JungleLanguage.DE -> "Gegnerische Höhle liegt direkt vor dir! Eintreten und Sieg sichern!"
        }
    }

    val adviceAssaultDenBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🏆 进驻兽穴"
        JungleLanguage.EN -> "🏆 Enter Den"
        JungleLanguage.JA -> "🏆 本陣到達"
        JungleLanguage.KO -> "🏆 진영 진입"
        JungleLanguage.ES -> "🏆 Entrar Guarida"
        JungleLanguage.FR -> "🏆 Entrer"
        JungleLanguage.DE -> "🏆 Höhle betreten"
        }
    }

    val adviceApproachDenHeadline: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "⚡️【步步为营·逼近兽穴】"
        JungleLanguage.ZH_HANT -> "⚡️【步步為營·逼近獸穴】"
        JungleLanguage.EN -> "⚡️【Strategic Advance·Approach Den】"
        JungleLanguage.JA -> "⚡️【陣形推進！本陣へ接近】"
        JungleLanguage.KO -> "⚡️【진형 전진! 진영 압박】"
        JungleLanguage.ES -> "⚡️【Avance Estratégico·Hacia Guarida】"
        JungleLanguage.FR -> "⚡️【Avancée Stratégique·Vers Repaire】"
        JungleLanguage.DE -> "⚡️【Strategischer Vorstoß·Höhle nah】"
        }
    }

    fun adviceApproachDenDetail(piece: String): String {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS -> "【${piece}】已深入对方区域！继续推进！"
        JungleLanguage.ZH_HANT -> "【${piece}】已深入對方區域！繼續推進！"
        JungleLanguage.EN -> "[${piece}] penetrated opponent zone! Keep pressing forward!"
        JungleLanguage.JA -> "[${piece}]が相手陣地へ切り込みました！このまま前進！"
        JungleLanguage.KO -> "[${piece}] 말이 상대 진영으로 진입했습니다! 전속 전진!"
        JungleLanguage.ES -> "¡[${piece}] ha penetrado en zona rival! ¡Sigue avanzando!"
        JungleLanguage.FR -> "[${piece}] a percé les lignes adverses ! Continuez d'avancer !"
        JungleLanguage.DE -> "[${piece}] ist im gegnerischen Bereich! Weiter vorrücken!"
        }
    }

    val adviceApproachDenBadge: String
        get() {
        return when (currentLanguage ) {
        JungleLanguage.ZH_HANS, JungleLanguage.ZH_HANT -> "🎯 逼近兽穴"
        JungleLanguage.EN -> "🎯 Approach Den"
        JungleLanguage.JA -> "🎯 本陣接近"
        JungleLanguage.KO -> "🎯 진영 접근"
        JungleLanguage.ES -> "🎯 Hacia Guarida"
        JungleLanguage.FR -> "🎯 Vers Repaire"
        JungleLanguage.DE -> "🎯 Höhle nah"
        }
    }

}
