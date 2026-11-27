package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val posterHighlightBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchPosterHighlight(packageMetadata.versionName)
    }
}

@Suppress("unused")
val posterHighlightSplitPatch = resourcePatch(
    name = "投稿者強調",
    description = "投稿者の強調表示を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。連鎖あぼーん（投稿者ID連鎖NG）は高度NG側の処理に含まれます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(posterHighlightBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/poster-highlight/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
