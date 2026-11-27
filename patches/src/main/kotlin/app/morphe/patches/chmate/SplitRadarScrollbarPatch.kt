package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val radarScrollbarBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchRadarScrollbarTouch(packageMetadata.versionName)
    }
}

@Suppress("unused")
val radarScrollbarPatch = resourcePatch(
    name = "レーダースクロールバー",
    description = "スクロールバーのタッチ操作（レーダースクロール）を有効にします。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(radarScrollbarBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/radar-scrollbar/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
