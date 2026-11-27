package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val tabletDisplayBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchNativeToolTabs(packageMetadata.versionName)
        patchTabletThreadHeaderAdSpace(packageMetadata.versionName)
    }
}

@Suppress("unused")
val tabletDisplayPatch = resourcePatch(
    name = "タブレット表示まわり",
    description = "タブレットのツールタブ表示とスレヘッダの広告枠まわりを調整します。初期状態は未選択。Haiagaru本体とは別に選択できます。版別ブロック内のタブレット処理（191/226のスレURL転送・タブ選択）は本体側に残しています。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(tabletDisplayBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/tablet-display/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
