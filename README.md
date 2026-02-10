# YukulabTemplate

Minecraft 1.21.11 向けの Fabric Mod テンプレートです。Kotlin で記述されており、Mod 開発に必要な基盤がセットアップ済みです。

## 特徴

- **Kotlin** — エントリポイント・Mod ロジックは全て Kotlin で記述（Mixin は Java）
- **Split Environment Source Sets** — サーバーサイドとクライアントサイドのコードを分離
- **Cloth Config + ModMenu** — TOML ベースの設定システムと ModMenu 統合
- **Fabric Networking API** — S2C / C2S カスタムペイロードのサンプル付き
- **テスト基盤** — ユニットテスト・サーバーゲームテスト・クライアントゲームテスト対応
- **CI** — GitHub Actions による自動ビルド

## 動作要件

- Java 21 以上
- Minecraft 1.21.11
- Fabric Loader 0.18.4 以上
- Fabric API

## セットアップ

1. GitHub で「Use this template」からリポジトリを作成
2. クローンして初期化スクリプトを実行

```bash
git clone <repository-url>
cd <your-repo-name>
./init.sh
```

対話的に Mod ID・Mod 名・Maven グループを入力すると、ファイル内容・ファイル名・パッケージ構造が一括で置換されます。

```
Mod ID (小文字英数字、例: myawesomemod): myawesomemod
Mod名 PascalCase (例: MyAwesomeMod): MyAwesomeMod
Mavenグループ (例: com.example.mymod): com.example.mymod
```

3. ビルドして動作確認

```bash
./gradlew build
```

## ビルド・実行

```bash
./gradlew build          # ビルド（成果物は build/libs/ に出力）
./gradlew runClient      # 開発用クライアント起動
./gradlew runServer      # 開発用サーバー起動
./gradlew runDatagen     # データジェネレーション実行
```

## テスト

```bash
./gradlew test               # ユニットテスト
./gradlew runGameTest        # サーバーサイドゲームテスト
./gradlew runClientGameTest  # クライアントサイドゲームテスト
```

## プロジェクト構成

```
src/
├── main/           # サーバーサイド & 共有コード
│   ├── kotlin/     #   Kotlin ソース（エントリポイント、設定、ネットワーク）
│   ├── java/       #   サーバーサイド Mixin
│   └── resources/  #   fabric.mod.json、Mixin 設定
├── client/         # クライアント専用コード
│   ├── kotlin/     #   クライアントエントリポイント、ModMenu 統合、DataGen
│   ├── java/       #   クライアントサイド Mixin
│   └── resources/
└── gametest/       # ゲームテスト
    └── resources/  #   テスト用 fabric.mod.json
```

## 主要な設定ファイル

| ファイル | 説明 |
|---|---|
| `build.gradle.kts` | ビルドスクリプト（Kotlin DSL） |
| `gradle/libs.versions.toml` | Version Catalog（依存関係バージョン管理） |
| `gradle.properties` | Mod メタデータ・Gradle 設定 |
| `src/main/resources/fabric.mod.json` | Mod メタデータ・エントリポイント定義 |

## ライセンス

[CC0 1.0 Universal](LICENSE)
