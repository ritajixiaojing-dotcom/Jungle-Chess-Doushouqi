# 🌲 快乐斗兽棋 (Jungle Chess / Doushouqi) 🦁🐭

[![iOS Platform](https://img.shields.io/badge/Platform-iOS%2016%2B%20%7C%20iPadOS%20%7C%20macOS-blue?logo=apple)](https://apps.apple.com)
[![Android Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026--36)-green?logo=android)](https://play.google.com)
[![iOS Status](https://img.shields.io/badge/App%20Store-Live%20v1.6.1%20%7C%20v1.7.2%20Ready-success?logo=apple)]()
[![Android Status](https://img.shields.io/badge/Google%20Play-Closed%20Testing%20(v1.7.2)-orange?logo=google-play)]()
[![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)

> 一款基于 **双原生架构 (iOS SwiftUI + Android Jetpack Compose)** 深度打造的现代化经典单机益智斗兽棋游戏。内置统一的 4-Ply / 6-Ply Alpha-Beta 剪枝博弈 AI 引擎、实时深度战术推演预判、三大神兽战略绝技（神兽天降、妙手越级、寒冰禁锢）、动态动物台词规则全显与纯净零广告体验。

---

## 📱 平台发布与测试状态 (Release & Testing Status)

| 平台 | 技术栈 | 当前版本 | 状态 / 渠道 | 说明 |
| :--- | :--- | :--- | :--- | :--- |
| 🍏 **iOS / iPadOS / macOS** | SwiftUI / Swift 6 | **v1.7.2 (Build 4)** | 🚀 **App Store 提审版本** | 支持 iPhone、iPad 及 Apple Silicon Mac |
| 🤖 **Android** | Jetpack Compose / Kotlin | **v1.7.2 (Build 7)** | 🧪 **Google Play 封闭测试 (Closed Testing)** | 覆盖全球 178 个国家/地区，火热测试中 |

### 🤖 Android 封闭测试 (Google Play Closed Testing) 参与方式
Android 版本目前已正式发布至 Google Play 封闭测试（Closed Testing - Alpha Track）阶段，采用 Google Groups 自动化分发机制：
1. **加入测试组**: 点击申请加入 [Jungle Chess Testers Google Group](https://groups.google.com/g/jungle-chess-testers) （群组邮箱：`jungle-chess-testers@googlegroups.com`）
2. **网页端接受测试邀请**: 访问 [Google Play Web Testing 计划](https://play.google.com/apps/testing/com.ritastudio.junglechess) 点击“Become a tester”
3. **移动端下载体验**: 通过 [Google Play Store 详情页](https://play.google.com/store/apps/details?id=com.ritastudio.junglechess) 下载安装并自动接收版本热更新

---

## 🌟 核心特色 (Key Features)

* **🧠 智能博弈引擎 (Multi-Ply Alpha-Beta Minimax Engine)**:
  * 🌱 **轻松入门 (Easy)**: 启发式走法，加入防守扰动，适合新手与少儿；
  * 🌿 **棋艺进阶 (Normal)**: 3-Ply 走一步看三步，攻守兼备，平衡对弈；
  * 👑 **智谋大师 (Master)**: 4-Ply / 6-Ply Minimax 深度推演 + 静态走子延伸 (Quiescence Search) + 陷阱控制权热力估值，极具挑战性！
* **🔮 深度战术推演 (Tactical Foresight Advice)**:
  * 实时评估候选着法性质：`👑 无损吃子`、`⚡️ 巧妙兑子 (+净得子力优势)`、`🛡️ 战术避险`；
  * 独创清障过河检测：智能预警敌鼠阻河，引导猛虎与雄狮腾空飞渡！
* **⚡️ 独创三大神兽绝技 (Symmetric Superpower Skills)**:
  * 🛡️ **神兽天降 (Guardian Teleport)**: 兽穴危急时刻瞬间召回主力猛兽守护中枢咽喉；
  * ✨ **妙手越级 (Master Leap)**: 2 回合内全员激活越级捕食特权，以弱克强逆转战局；
  * ❄️ **寒冰禁锢 (Frost Freeze)**: 战术级强控技能，直接冰封对方核心主力 1 回合无法移动！
  * 🤖 **双向技能博弈**: 玩家与 AI 均拥有各自独立技能槽，AI 会主动护穴、越级突袭与冰控反制，对弈更富智慧。
* **👥 丰富的对战交互**:
  * 包含 **人机单人博弈 (PvE)** 与 **双人同屏对战 (PvP)**；
  * 完整音效与震动反馈，支持 8 种语言动态即时切换（简体中文、繁体中文、English、日本語、한국어、Español、Français、Deutsch）。
* **🍃 纯净绿色**:
  * 100% 离线单机运行，零个人隐私收集，零第三方 SDK 追踪，零广告弹窗。

---

## 🦁 斗兽棋规则与食物链 (Rules & Hierarchy)

```text
象 (950) ＞ 狮 (850) ＞ 虎 (720) ＞ 豹 (500) ＞ 狼 (380) ＞ 狗 (280) ＞ 猫 (200) ＞ 鼠 (320)
```

1. **生克克制**: `🐭 鼠` 可以吃 `🐘 象`（鼠钻象鼻）；
2. **同级互吃**: 同级动物相遇，先下手者胜（先攻方捕食）；
3. **飞跃小河**: `🦁 狮` 和 `🐯 虎` 可以横向或纵向飞跃小河；若跳跃路线上河道中有 `🐭 鼠` 阻挡，则无法飞跃；
4. **潜水机制**: 只有 `🐭 鼠` 能够进入小河游泳，陆地猛兽无法下水；水中老鼠不能直接捕食岸上大象；
5. **陷阱机制**: 己方进入对方兽穴周边的陷阱时战斗力清零，任一敌兽均可捕食；
6. **胜负判定**: 任一方率先攻入对方大本营（兽穴 🏰）即直接判胜，或全歼对方所有动物。

---

## 🛠️ 技术架构 (Tech Stack & Architecture)

本项目采用**双端原生架构**，在保障各平台最佳原生交互与性能的同时，核心博弈算法保持严格数学对称与一致性。

### 🍏 iOS 客户端
* **语言**: Swift 5.10 / Swift 6 (Strict Concurrency Checking)
* **UI 框架**: SwiftUI + Combine
* **音效与触觉**: `AudioServices` + Core Haptics
* **支持目标**: iOS 16.0+, iPadOS 16.0+, macOS 13.0+ (Designed for iPad)

### 🤖 Android 客户端
* **语言**: Kotlin 2.0+ (JVM 17 Toolchain)
* **UI 框架**: Jetpack Compose + Material Design 3
* **架构设计**: 单向数据流 (Unidirectional Data Flow) + ViewModel + Kotlin Coroutines & StateFlow
* **音效系统**: `SoundPool` 音频池异步低延迟并发播放
* **构建系统**: Gradle 8.11 + Kotlin DSL (`build.gradle.kts`)，面向 Target SDK 36 (Android 16)，向下兼容至 Android 8.0 (API 26)

### 🧠 统一博弈算法体系 (Game Engine)
* **算法内核**: 4-Ply / 6-Ply Minimax 极小化极大算法 + Alpha-Beta 剪枝
* **静态走法延伸 (Quiescence Search)**: 搜索深度边界时自动延伸吃子序列，消除地平线效应 (Horizon Effect)
* **位置价值矩阵 (Piece-Square Tables)**: 结合兽穴距离、陷阱防守权重、中央河道压制动态调整估值
* **往复震荡惩罚机制 (Anti-Loop Oscillation Penalty)**: 记录局势 Zobrist/状态哈希，对来回重复无意义走子施以指数衰减惩罚，防止死循环

---

## 📂 项目结构 (Project Directory Structure)

```text
Jungle-Chess-Doushouqi/
├── Jungle Chess Doushouqi/            # 🍏 iOS 源码 (SwiftUI)
│   ├── App/                           # App 生命周期入口
│   ├── Engine/                        # Swift 极小化极大 AI 引擎
│   ├── Models/                        # 棋盘、棋子、走法数据模型
│   ├── Views/                         # SwiftUI 棋盘、技能栏、胜负结算界面
│   ├── Resources/                     # 音效资源与 Assets
│   └── Localization/                  # 多语言本地化文件
├── Jungle Chess Doushouqi.xcodeproj   # 🍏 Xcode 工程配置文件
│
├── android/                           # 🤖 Android 源码 (Jetpack Compose)
│   ├── app/
│   │   ├── src/main/java/com/ritastudio/junglechess/
│   │   │   ├── MainActivity.kt        # Compose 主入口 Activity
│   │   │   ├── engine/                # Kotlin AI 博弈引擎 (JungleGameEngine.kt)
│   │   │   ├── model/                 # 游戏状态与数据模型 (JungleModels.kt)
│   │   │   ├── ui/                    # Compose UI 组件 (棋盘、弹窗、顶栏、主题)
│   │   │   ├── sound/                 # SoundPool 声音管理器
│   │   │   └── localization/          # Android 多语言管理器
│   │   ├── src/main/res/              # Android 矢量图标、RAW 音效与资源
│   │   ├── src/test/                  # 引擎单元测试 (JungleChessTest.kt)
│   │   └── build.gradle.kts           # App 级 Gradle 依赖与构建配置
│   ├── google_play_assets/            # Google Play 商店视觉资产、横幅与元数据
│   ├── gradle/                        # Gradle Wrapper 与 Version Catalog
│   ├── build.gradle.kts               # 根项目构建脚本
│   └── settings.gradle.kts            # 模块设置
│
├── AppStore_Listing_Info.md           # iOS App Store 上架文案与元数据
├── PrivacyPolicy.html                 # 隐私政策页面
└── README.md                          # 项目综合文档
```

---

## 🚀 编译与构建 (Build & Run)

### 🍏 iOS 运行
1. 打开 `Jungle Chess Doushouqi.xcodeproj` 于 Xcode 15+ / 16+。
2. 选择目标设备 (iPhone / iPad 模拟器或真机)。
3. 按 `Cmd + R` 即可编译并启动应用。

### 🤖 Android 运行
1. 使用 **Android Studio Ladybug (2024.2+)** 打开 `android/` 目录。
2. 等待 Gradle 同步完成。
3. 连接 Android 设备或启动模拟器，点击 **Run** 即可运行。
4. 或者在命令行终端执行：
   ```bash
   cd android
   # 运行完整单元测试
   ./gradlew test
   # 编译 Debug APK
   ./gradlew assembleDebug
   ```

---

## 📄 开源与隐私 (License & Privacy)

本项目代码采用 [MIT License](LICENSE) 授权开源。  
我们尊重用户隐私，应用不上传、不收集任何用户隐私数据，详情请参阅 [PrivacyPolicy.html](PrivacyPolicy.html)。
