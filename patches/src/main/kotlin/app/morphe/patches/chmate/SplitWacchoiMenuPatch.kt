package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val wacchoiMenuBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchWacchoiLongPressMenu(packageMetadata.versionName)
    }
}

@Suppress("unused")
val wacchoiMenuPatch = resourcePatch(
    name = "ワッチョイ検索・コピー",
    description = "ワッチョイの長押しメニュー（上4桁・下4桁のコピー等）を追加します。初期状態は未選択。Haiagaru本体とは別に選択できます。なおキャッシュ済みワッチョイ検索はクイックフィルター側の呼び出しに含まれるため、このパッチには含まれません。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(wacchoiMenuBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/wacchoi-menu/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
