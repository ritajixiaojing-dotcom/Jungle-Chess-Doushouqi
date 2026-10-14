package com.ritastudio.junglechess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ritastudio.junglechess.localization.JungleLanguageManager
import com.ritastudio.junglechess.model.AnimalType

@Composable
fun JungleRuleGuideDialog(
    onDismiss: () -> Unit,
    lang: JungleLanguageManager = JungleLanguageManager.shared
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lang.ruleSheetTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(lang.closeBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Win Conditions
                    Text(
                        text = "🎯 ${lang.ruleWinConditionTitle}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = lang.ruleWinConditionDesc,
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        lineHeight = 15.sp
                    )

                    HorizontalDivider()

                    // Animal Hierarchy
                    Text(
                        text = "🦁 ${lang.ruleHierarchyTitle}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "🐘 ＞ 🦁 ＞ 🐯 ＞ 🐆 ＞ 🐺 ＞ 🐶 ＞ 🐱 ＞ 🐭",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )

                    AnimalType.entries.reversed().forEach { type ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${type.emoji} ${lang.animalName(type)}:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.width(68.dp)
                            )
                            Text(
                                text = lang.animalEatRule(type),
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f),
                                lineHeight = 13.sp
                            )
                        }
                    }

                    HorizontalDivider()

                    // Special Terrain & Superpowers
                    Text(
                        text = "⚡️ ${lang.ruleSpecialAbilitiesTitle}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "• 🌊: ${lang.animalName(AnimalType.RAT)} - ${lang.animalEatRule(AnimalType.RAT)}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "• 🦁 / 🐯: ${lang.animalEatRule(AnimalType.LION)}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "• ${lang.shieldSkillText(1)}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "• ${lang.frenzySkillText(1)}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "• ${lang.freezeSkillText(1)}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )

                    HorizontalDivider()

                    // Version Footer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "快乐斗兽棋 · Jungle Chess",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "v1.7.2 (Build 7)",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
