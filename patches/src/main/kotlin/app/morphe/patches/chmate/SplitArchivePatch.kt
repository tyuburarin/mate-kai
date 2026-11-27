package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val archiveBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        val profile = profileFor(packageMetadata.versionName)
        patchEdgeArchiveToolbar(profile, packageMetadata.versionName)
        patchRestoredDatRows()
        patchResponseArchive(packageMetadata.versionName)
        patchArchiveOrigins(packageMetadata.versionName)
        patchThreadArchiveOrder(packageMetadata.versionName)
    }
}

@Suppress("unused")
val archiveSplitPatch = resourcePatch(
    name = "DAT落ち取得・過去ログ",
    description = "DAT落ちスレの取得、保存レス・アーカイブ、復元したDAT行の表示を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。242のTalk経由DAT取得は通信まわり（本体側）に残しています。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(archiveBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/archive/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
