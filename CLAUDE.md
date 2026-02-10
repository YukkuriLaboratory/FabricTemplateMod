# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Fabric Minecraft Mod (Minecraft 1.21.11) using Kotlin and Java 21. Mod ID: `yukulabtemplate`, package: `net.yukulab.template`.

## Build Commands

```bash
./gradlew build          # ビルド（成果物は build/libs/ に出力）
./gradlew runClient      # 開発用クライアント起動
./gradlew runServer      # 開発用サーバー起動
./gradlew runDatagen     # データジェネレーション実行
```

## Architecture

Fabric Loomの **split environment source sets** を使用し、サーバー/クライアントのコードを分離している。

- `src/main/` — サーバーサイド＆共有コード（エントリポイント: `YukulabTemplate.kt`）
- `src/client/` — クライアント専用コード（エントリポイント: `YukulabTemplateClient.kt`、DataGen: `YukulabTemplateDataGenerator.kt`）

エントリポイントは `fabric.mod.json` で定義され、全て `adapter: "kotlin"` を使用。

### Mixin

- `src/main/java/` — サーバーサイドMixin（設定: `yukulabtemplate.mixins.json`）
- `src/client/java/` — クライアントサイドMixin（設定: `yukulabtemplate.client.mixins.json`）

MixinはJavaで記述する（Kotlin非対応）。互換レベルはJava 21。

## Mod Config（設定システム）

**Cloth Config Auto Config** + **ModMenu** による設定管理を使用。

- `ModConfig.kt`（`src/main/`） — `@Config` アノテーション付き設定クラス。`ConfigData` を実装し、フィールドを `var` で定義するだけで設定項目になる。シリアライズは TOML（`Toml4jConfigSerializer`）。保存先は `config/yukulabtemplate.toml`。
- `YukulabTemplate.onInitialize()` で `AutoConfig.register(ModConfig::class.java, ::Toml4jConfigSerializer)` により登録。
- `ModMenuIntegration.kt`（`src/client/`） — `ModMenuApi` を実装し、ModMenu の設定ボタンから Cloth Config の設定画面を開く。`fabric.mod.json` の `modmenu` エントリポイントとして登録。

設定値の取得:
```kotlin
val config = AutoConfig.getConfigHolder(ModConfig::class.java).config
```

設定項目の追加は `ModConfig` クラスにフィールドを追加するだけでよい。`@ConfigEntry` アノテーションでGUIのカスタマイズも可能。

## Key Configuration Files

- `build.gradle.kts` — ビルドスクリプト（Kotlin DSL）
- `settings.gradle.kts` — Gradleプロジェクト設定（Kotlin DSL）
- `gradle/libs.versions.toml` — Version Catalog（Minecraft/Fabric/Kotlin の依存関係バージョン管理）
- `gradle.properties` — Gradle設定とModメタデータ
- `fabric.mod.json` — Modメタデータ、エントリポイント、依存関係定義（`${version}` はビルド時に `processResources` で展開）
- マッピング: `loom.officialMojangMappings()`（Mojang公式マッピング使用）

## CI

GitHub Actions（`.github/workflows/build.yml`）がpush/PRで `./gradlew build` を実行。Java 25（Microsoft distribution）使用。
