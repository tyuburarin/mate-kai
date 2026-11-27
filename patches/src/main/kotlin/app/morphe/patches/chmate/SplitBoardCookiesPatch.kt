package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val boardCookiesBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchBoardCookies(packageMetadata.versionName)
        patchBeAutoLogout(packageMetadata.versionName)
    }
}

@Suppress("unused")
val boardCookiesPatch = resourcePatch(
    name = "掲示板クッキー・BE自動ログアウト",
    description = "掲示板クッキーの保持とBE自動ログアウトを行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(boardCookiesBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/board-cookies/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
