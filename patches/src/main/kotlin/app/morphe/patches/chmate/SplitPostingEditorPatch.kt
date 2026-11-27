package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val postingEditorBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        if (packageMetadata.versionName in listOf("0.8.10.191 dev", "0.8.10.226 dev", "0.8.10.241", "0.8.10.242 dev")) {
            patchPostEditorTextSize(packageMetadata.versionName)
        }
        patchPostPreflightValidation(packageMetadata.versionName)
        patchOutgoingReplacementBody(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.191 dev") {
            patchLegacyExternalEmojiPostBody()
        } else {
            patchExternalEmojiPostCopy(packageMetadata.versionName)
        }
    }
}

@Suppress("unused")
val postingEditorPatch = resourcePatch(
    name = "書き込み欄・投稿前チェック",
    description = "書き込み欄の文字サイズ、投稿前のバリデーション、投稿エラー時のコールバック、バックグラウンド投稿後の再読み込みを行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(postingEditorBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/posting-editor/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
