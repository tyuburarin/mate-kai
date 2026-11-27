# Haiagaru 分割版 1.56.1 — ビルド指示（PC Codex 向け）

## ベース
- 上流 areteruhiro/Haiagaru-Morphe の **タグ 1.56.1** を新規チェックアウトすること（1.55.1 の作業ツリーへの上書きではなく、新規で取り直す）。
- 1.55.1 → 1.56.1 の上流変更は拡張機能（Java）側のみで、パッチ側 Kotlin コードは無変更。そのため分割作業は 1.55.1 版のものをそのまま移植済み。

## この ZIP の中身（ベースへの上書き適用）
- `patches/src/main/kotlin/app/morphe/patches/chmate/HaiagaruPatch.kt`（差し替え）
  - モノリスから31機能分の呼び出しを分離し、分割側から参照する関数・型の可視性を internal に変更したもの。
- `patches/src/main/kotlin/app/morphe/patches/chmate/Split*.kt`（31ファイル、新規追加）
  - 個別選択できる分割パッチ（全てデフォルトOFF）。
  - No.30「ローカルバックアップ/復元」だけは分割していない（パッケージ名変更パッチ内部に結線されているため）。
- `patches/build.gradle.kts`（差し替え）
  - バンドル表示名を `Haiagaru 切り分け版` に変更しただけ。

## ビルド手順（厳守）
認証・Android SDK の設定後に、プロジェクトルートで必ずこのコマンドを使うこと：

```
.\gradlew.bat :patches:buildAndroid --no-daemon --max-workers=1
```

- 生成物：`patches/build/libs/patches-1.56.1.mpp`
- **前回の失敗の再発防止**：前回は別タスクでビルドしたため、生成 .mpp に `classes.dex` が含まれず、Morphe Manager（Android）でパッチ0件になった。Android 用バンドルの dex 化は `buildAndroid` タスクの工程に含まれる。
- ビルド後、生成 .mpp の**ルートに `classes.dex` が存在すること**を必ず確認し、結果に明記すること。無ければ成功として扱わず、その旨を報告すること。

## ルール
- ソースコードは変更しないこと。コンパイルエラーが出たら、エラー文をそのまま報告すること（勝手に直さない）。
- 実機確認・動画確認は不要（ユーザーが自分でやる）。報告はコード変更点と生成物の確認結果まで。
