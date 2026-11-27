package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val relatedThreadsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchRelatedThreads(packageMetadata.versionName)
        patchThreadKeywordOrder(packageMetadata.versionName)
        patchDisplayNames(packageMetadata.versionName)
    }
}

@Suppress("unused")
val relatedThreadsPatch = resourcePatch(
    name = "関連スレ・キーワード表示名",
    description = "関連スレの表示、スレキーワードの並び順、キーワード／スレ名の表示名を扱います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(relatedThreadsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/related-threads/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
