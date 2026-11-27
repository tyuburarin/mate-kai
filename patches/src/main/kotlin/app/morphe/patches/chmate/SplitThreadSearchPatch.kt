package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val threadSearchBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchThreadSearchText(packageMetadata.versionName)
    }
}

@Suppress("unused")
val threadSearchPatch = resourcePatch(
    name = "スレ内検索テキスト",
    description = "スレ内検索のテキスト処理を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。全板検索はクイックフィルター側、板タイトル検索は外部板側の処理に含まれます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(threadSearchBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/thread-search/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
