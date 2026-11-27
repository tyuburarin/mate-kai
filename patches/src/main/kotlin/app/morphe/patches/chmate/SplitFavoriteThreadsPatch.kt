package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val favoriteThreadsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchFavoriteThreads(packageMetadata.versionName)
        patchHomeRefreshKeywords(packageMetadata.versionName)
    }
}

@Suppress("unused")
val favoriteThreadsSplitPatch = resourcePatch(
    name = "お気に入りスレ・ホーム更新キーワード",
    description = "お気に入りスレ機能とホーム更新キーワードを追加します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(favoriteThreadsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/favorite-threads/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
