package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val stringReplacementXPreviewBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchStringReplacement()
        patchXPostPreviewBindings()
        patchXVideoAttachmentProjection()
    }
}

@Suppress("unused")
val stringReplacementXPreviewPatch = resourcePatch(
    name = "文字列置換・Xプレビュー",
    description = "表示本文への文字列置換ルール、X投稿の本文プレビュー、X動画添付の表示を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(stringReplacementXPreviewBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/string-replacement-x-preview/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
