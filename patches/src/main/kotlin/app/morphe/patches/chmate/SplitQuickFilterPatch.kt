package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val quickFilterBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchQuickFilterToolbar(packageMetadata.versionName)
        patchPopularReplies(packageMetadata.versionName)
    }
}

@Suppress("unused")
val quickFilterSplitPatch = resourcePatch(
    name = "クイックフィルター・人気レス",
    description = "クイックフィルターツールバー、人気レス表示、追加クイックフィルターを有効にします。全板検索とキャッシュ済みワッチョイ検索もこの中の処理から呼ばれます。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(quickFilterBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/quick-filter/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
