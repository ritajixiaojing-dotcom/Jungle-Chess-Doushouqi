package com.ritastudio.junglechess.localization

import java.util.Locale

enum class JungleLanguage(val code: String, val displayName: String, val flag: String) {
    ZH_HANS("zh-Hans", "🇨🇳 简体中文", "🇨🇳"),
    ZH_HANT("zh-Hant", "🇭🇰 繁體中文", "🇭🇰"),
    EN("en", "🇺🇸 English", "🇺🇸"),
    JA("ja", "🇯🇵 日本語", "🇯🇵"),
    KO("ko", "🇰🇷 한국어", "🇰🇷"),
    ES("es", "🇪🇸 Español", "🇪🇸"),
    FR("fr", "🇫🇷 Français", "🇫🇷"),
    DE("de", "🇩🇪 Deutsch", "🇩🇪");

    companion object {
        fun fromCode(code: String): JungleLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) }
                ?: if (code.startsWith("zh-Hant") || code.startsWith("zh-TW") || code.startsWith("zh-HK")) ZH_HANT
                else if (code.startsWith("zh")) ZH_HANS
                else if (code.startsWith("ja")) JA
                else if (code.startsWith("ko")) KO
                else if (code.startsWith("es")) ES
                else if (code.startsWith("fr")) FR
                else if (code.startsWith("de")) DE
                else EN
        }

        fun detectSystem(): JungleLanguage {
            val locale = Locale.getDefault()
            val tag = locale.toLanguageTag()
            return fromCode(tag)
        }
    }
}
