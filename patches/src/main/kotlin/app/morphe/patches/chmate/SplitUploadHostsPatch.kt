package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val uploadHostsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchNativeUploadHistory(packageMetadata.versionName)
        patchPostingOverflowMenu(packageMetadata.versionName, "MediaUploadHosts", "upload_hosts")
        patchGigaFilePosting(packageMetadata.versionName)
        patchImgurWebSessionUpload(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.191 dev") {
            patchLegacyImageUploadTempName()
            patchLegacyImageUploadCall()
        }
    }
}

@Suppress("unused")
val uploadHostsPatch = resourcePatch(
    name = "アップロード先切替",
    description = "標準添付のアップロード先切替と履歴、Imgur／ギガファイル便への投稿、投稿オーバーフローメニューを追加します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(uploadHostsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/upload-hosts/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
