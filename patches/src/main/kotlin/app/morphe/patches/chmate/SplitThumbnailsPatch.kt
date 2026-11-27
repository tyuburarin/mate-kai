package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val thumbnailsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchImageListResolvedMedia(packageMetadata.versionName)
        patchThumbnailClickAttachments(packageMetadata.versionName)
        patchRegisteredImageLongPress(packageMetadata.versionName)
        patchThumbnailRebind242(packageMetadata.versionName)
        patchThumbnailAppearance(packageMetadata.versionName)
    }
}

@Suppress("unused")
val thumbnailsPatch = resourcePatch(
    name = "サムネイル・画像一覧",
    description = "サムネイルの再バインドと見た目、画像一覧のメディア解決、サムネイルタップの添付処理、登録画像の長押しを調整します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(thumbnailsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/thumbnails/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
