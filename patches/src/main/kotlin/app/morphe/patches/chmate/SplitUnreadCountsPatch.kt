package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val unreadCountsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchUnreadToolbarCount(packageMetadata.versionName)
        patchHiddenNgUnread(packageMetadata.versionName)
        patchHiddenNgNewArrivalCount(packageMetadata.versionName)
    }
}

@Suppress("unused")
val unreadCountsPatch = resourcePatch(
    name = "未読数・新着数表示",
    description = "ツールバーの未読数、NG済みレスの未読・新着カウントを表示します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(unreadCountsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/unread-counts/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
