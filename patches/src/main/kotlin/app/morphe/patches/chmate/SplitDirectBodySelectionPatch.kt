package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val directBodySelectionBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchDirectBodySelection(packageMetadata.versionName)
        patchSelectionMenuPriority()
    }
}

@Suppress("unused")
val directBodySelectionPatch = resourcePatch(
    name = "本文の直接選択コピー",
    description = "レス本文を直接選択してコピーできるようにします。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(directBodySelectionBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/direct-body-selection/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
