package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val threadListDisplayBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        if (packageMetadata.versionName in listOf("0.8.10.191 dev", "0.8.10.226 dev", "0.8.10.241", "0.8.10.242 dev")) {
            patchReadThreadsFirst(packageMetadata.versionName)
            patchThreadTitleSpacing(packageMetadata.versionName)
        }
        patchShortThreadTopAlignment(packageMetadata.versionName)
    }
}

@Suppress("unused")
val threadListDisplayPatch = resourcePatch(
    name = "スレ一覧の表示調整",
    description = "既読スレを先頭に表示、スレタイトルの間隔調整、短いスレの上寄せを行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(threadListDisplayBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/thread-list-display/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
