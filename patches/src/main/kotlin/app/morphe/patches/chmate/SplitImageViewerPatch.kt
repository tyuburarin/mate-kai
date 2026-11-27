package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val imageViewerBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchNativeImagePaging(packageMetadata.versionName)
        patchNativeImageCloseLifecycle(packageMetadata.versionName)
        patchImageViewerRotationButton(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.242 dev") patchImageViewerBottomActions242()
        patchLargeImageAlignment(packageMetadata.versionName)
    }
}

@Suppress("unused")
val imageViewerSplitPatch = resourcePatch(
    name = "画像ビューア操作",
    description = "画像ビューアのページ送り、閉じる挙動、回転ボタン、242の底部ボタン、大きい画像の配置を調整します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(imageViewerBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/image-viewer/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
