# mate改

ChMate 用パッチ Haiagaru（areteruhiro/Haiagaru-Morphe）の分割版。
単体パッチから31機能を個別選択できるパッチに分離したもの（全てデフォルトOFF）。

- ベース: 上流タグ **1.56.1**（このリポジトリには上流との差分ファイルのみを収録）
- 収録物: 分割済み `HaiagaruPatch.kt`、`Split*.kt`（31ファイル）、表示名を変更した `patches/build.gradle.kts`
- ビルド方法: 上流 1.56.1 をチェックアウトし、このリポジトリのファイルを同パスに上書きしてから
  `.\gradlew.bat :patches:buildAndroid --no-daemon --max-workers=1`
  （詳細は BUILD_NOTES.md）
- Morphe パッチソースとしても利用可能（ルートの patches-bundle.json）。
  現在のリリース .mpp は 1.55.1 ベース。

Original: https://github.com/areteruhiro/Haiagaru-Morphe (GPL-3.0)
