#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# テンプレートのデフォルト値
OLD_MOD_ID="yukulabtemplate"
OLD_MOD_NAME="YukulabTemplate"
OLD_MAVEN_GROUP="net.yukulab.template"
OLD_PACKAGE_PATH="net/yukulab/template"

# Portable sed -i (GNU/BSD対応)
if sed --version 2>&1 | grep -q GNU; then
    sed_i() { sed -i "$@"; }
else
    sed_i() { sed -i '' "$@"; }
fi

echo "=== Fabric Mod テンプレート初期化 ==="
echo ""

# 既に初期化済みかチェック
if ! grep -rq "$OLD_MOD_ID" --include='*.kt' --include='*.java' --include='*.json' src/ 2>/dev/null; then
    echo "エラー: テンプレートは既に初期化済みのようです"
    exit 1
fi

# 入力の受け取り
read -rp "Mod ID (小文字英数字、例: myawesomemod): " MOD_ID
read -rp "Mod名 PascalCase (例: MyAwesomeMod): " MOD_NAME
read -rp "Mavenグループ (例: com.example.mymod): " MAVEN_GROUP

# バリデーション
if [[ -z "$MOD_ID" || -z "$MOD_NAME" || -z "$MAVEN_GROUP" ]]; then
    echo "エラー: すべての項目を入力してください"
    exit 1
fi

if [[ ! "$MOD_ID" =~ ^[a-z][a-z0-9_-]*$ ]]; then
    echo "エラー: Mod IDは小文字英字で始まり、小文字英数字・ハイフン・アンダースコアのみ使用可能です"
    exit 1
fi

if [[ ! "$MOD_NAME" =~ ^[A-Z][a-zA-Z0-9]*$ ]]; then
    echo "エラー: Mod名は大文字英字で始まるPascalCaseで指定してください"
    exit 1
fi

if [[ ! "$MAVEN_GROUP" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$ ]]; then
    echo "エラー: Mavenグループはドット区切りの小文字英数字で指定してください (例: com.example.mymod)"
    exit 1
fi

PACKAGE_PATH="${MAVEN_GROUP//./\/}"

echo ""
echo "以下の設定で初期化します:"
echo "  Mod ID:         $MOD_ID"
echo "  Mod名:          $MOD_NAME"
echo "  Mavenグループ:   $MAVEN_GROUP"
echo "  パッケージパス:   $PACKAGE_PATH"
echo ""
read -rp "続行しますか？ [y/N]: " CONFIRM
if [[ "$CONFIRM" != "y" && "$CONFIRM" != "Y" ]]; then
    echo "キャンセルしました"
    exit 0
fi

echo ""

# ステップ 1: ファイル内容の置換
echo "[1/4] ファイル内容を置換中..."
while IFS= read -r file; do
    sed_i \
        -e "s|${OLD_MAVEN_GROUP}|${MAVEN_GROUP}|g" \
        -e "s|${OLD_PACKAGE_PATH}|${PACKAGE_PATH}|g" \
        -e "s|${OLD_MOD_NAME}|${MOD_NAME}|g" \
        -e "s|${OLD_MOD_ID}|${MOD_ID}|g" \
        "$file"
    echo "  置換: $file"
done < <(find . -type f \
    -not -path './.git/*' \
    -not -path './build/*' \
    -not -path './.gradle/*' \
    -not -name '*.png' \
    -not -name '*.jar' \
    -not -name '*.class' \
    -not -name "$(basename "$0")" \
    -print0 | xargs -0 grep -l "${OLD_MOD_ID}\|${OLD_MOD_NAME}\|${OLD_MAVEN_GROUP}" 2>/dev/null)

# ステップ 2: ファイル名の変更
echo "[2/4] ファイル名を変更中..."

# Mixin設定ファイル等 (mod IDを含むファイル名)
while IFS= read -r f; do
    dir="$(dirname "$f")"
    old_base="$(basename "$f")"
    new_base="${old_base//${OLD_MOD_ID}/${MOD_ID}}"
    if [[ "$old_base" != "$new_base" ]]; then
        mv "$f" "${dir}/${new_base}"
        echo "  リネーム: ${old_base} -> ${new_base}"
    fi
done < <(find . -type f -name "${OLD_MOD_ID}*" -not -path './.git/*')

# Kotlinソースファイル (クラス名を含むファイル名)
while IFS= read -r f; do
    dir="$(dirname "$f")"
    old_base="$(basename "$f")"
    new_base="${old_base//${OLD_MOD_NAME}/${MOD_NAME}}"
    if [[ "$old_base" != "$new_base" ]]; then
        mv "$f" "${dir}/${new_base}"
        echo "  リネーム: ${old_base} -> ${new_base}"
    fi
done < <(find . -type f -name "${OLD_MOD_NAME}*" -not -path './.git/*')

# ステップ 3: ディレクトリの移動
echo "[3/4] パッケージディレクトリを移動中..."

# アセットディレクトリ
if [[ -d "src/main/resources/assets/${OLD_MOD_ID}" ]]; then
    mv "src/main/resources/assets/${OLD_MOD_ID}" "src/main/resources/assets/${MOD_ID}"
    echo "  移動: assets/${OLD_MOD_ID} -> assets/${MOD_ID}"
fi

# ソースディレクトリ
SOURCE_ROOTS=(
    "src/main/kotlin"
    "src/main/java"
    "src/client/kotlin"
    "src/client/java"
    "src/test/kotlin"
    "src/gametest/kotlin"
)

for root in "${SOURCE_ROOTS[@]}"; do
    old_dir="${root}/${OLD_PACKAGE_PATH}"
    new_dir="${root}/${PACKAGE_PATH}"

    if [[ -d "$old_dir" ]]; then
        mkdir -p "$new_dir"
        # ディレクトリの中身をすべて移動
        for item in "$old_dir"/*; do
            [[ -e "$item" ]] && mv "$item" "$new_dir/"
        done
        for item in "$old_dir"/.[^.]*; do
            [[ -e "$item" ]] && mv "$item" "$new_dir/"
        done
        echo "  移動: ${old_dir} -> ${new_dir}"

        # 空になった旧ディレクトリを深い方から順に削除
        IFS='/' read -ra OLD_PARTS <<< "$OLD_PACKAGE_PATH"
        for ((i=${#OLD_PARTS[@]}; i>0; i--)); do
            dir_to_remove="${root}"
            for ((j=0; j<i; j++)); do
                dir_to_remove="${dir_to_remove}/${OLD_PARTS[$j]}"
            done
            if [[ -d "$dir_to_remove" ]] && [[ -z "$(ls -A "$dir_to_remove" 2>/dev/null)" ]]; then
                rmdir "$dir_to_remove"
            fi
        done
    fi
done

# ステップ 4: クリーンアップ
echo "[4/4] クリーンアップ..."

read -rp "初期化スクリプト (init.sh) を削除しますか？ [Y/n]: " DELETE_SELF
if [[ "$DELETE_SELF" != "n" && "$DELETE_SELF" != "N" ]]; then
    rm -- "$0"
    echo "  init.sh を削除しました"
fi

echo ""
echo "初期化が完了しました！"
echo ""
echo "次のステップ:"
echo "  1. README.md の内容をプロジェクトに合わせて更新してください"
echo "  2. CLAUDE.md の内容を確認・更新してください"
echo "  3. ./gradlew build でビルドが通ることを確認してください"
echo "  4. git add -A && git commit -m 'Initialize mod: ${MOD_NAME}'"
