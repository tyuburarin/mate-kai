package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val imageCacheBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchHttpsTransport()
    }
}

@Suppress("unused")
val imageCachePatch = resourcePatch(
    name = "画像キャッシュ拡張・HTTPS化",
    description = "画像キャッシュ上限の選択肢を拡張（最大256GB）し、任意のHTTP→HTTPS切替を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(imageCacheBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/image-cache/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
        val cacheArrayNames = document("res/values/arrays.xml").use {
            ImageCacheSizeOptions.expand(it)
        }
        get("res").listFiles().orEmpty().filter {
            it.isDirectory && it.name.startsWith("values-")
        }.forEach { directory ->
            directory.listFiles().orEmpty().filter { file ->
                file.extension == "xml" && cacheArrayNames.any { name ->
                    file.readText().contains("name=\"$name\"")
                }
            }.forEach { file ->
                document("res/${directory.name}/${file.name}").use {
                    ImageCacheSizeOptions.expandOverrides(it, cacheArrayNames)
                }
            }
        }
    }
}
