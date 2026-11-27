package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val toolbarIconsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchToolbarIcons(packageMetadata.versionName)
    }
}

@Suppress("unused")
val toolbarIconsPatch = resourcePatch(
    name = "ツールバーアイコン・ラベル置換",
    description = "ツールバーの文言・アイコンを差し替えます。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(toolbarIconsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/toolbar-icons/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
