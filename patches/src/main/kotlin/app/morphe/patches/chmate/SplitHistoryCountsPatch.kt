package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val historyCountsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchPostedThreadDisplayCount(packageMetadata.versionName)
        patchPostHistoryRetentionCount(packageMetadata.versionName)
        patchClipboardHistory(packageMetadata.versionName)
    }
}

@Suppress("unused")
val historyCountsPatch = resourcePatch(
    name = "投稿履歴・クリップボード履歴",
    description = "投稿履歴の保持件数、投稿済みスレの表示件数、クリップボード履歴を扱います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(historyCountsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/history-counts/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
