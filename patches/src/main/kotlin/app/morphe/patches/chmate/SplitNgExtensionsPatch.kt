package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val ngExtensionsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        if (packageMetadata.versionName in listOf("0.8.10.191 dev", "0.8.10.226 dev", "0.8.10.241", "0.8.10.242 dev")) {
            patchNgThreadGroupVisibility(packageMetadata.versionName)
            patchNgBoardScopes()
        }
        patchNgRegistrationLimit()
        patchNgEditGesture(packageMetadata.versionName)
        patchHiddenNgMenu(packageMetadata.versionName)
    }
}

@Suppress("unused")
val ngExtensionsPatch = resourcePatch(
    name = "NG拡張",
    description = "NG登録上限の変更、IDなしレスの非表示、NG対象のスレグループ表示、NG編集ジェスチャー、隠しNGメニューを追加します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(ngExtensionsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/ng-extensions/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
