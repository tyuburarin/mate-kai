package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val browsingHistoryBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchBrowsingAndSearchHistory(packageMetadata.versionName)
        patchGroupedBrowsingHistory(packageMetadata.versionName)
    }
}

@Suppress("unused")
val browsingHistorySplitPatch = resourcePatch(
    name = "閲覧履歴・検索履歴",
    description = "閲覧履歴と検索履歴の保持・グループ化を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(browsingHistoryBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/browsing-history/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
