package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val patrolBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchThreadRefreshValidators(packageMetadata.versionName)
        patchPatrolImages(packageMetadata.versionName)
        patchPatrolInterval(packageMetadata.versionName)
        patchRefreshTargets(packageMetadata.versionName)
        patchLegacyAutoReload(packageMetadata.versionName)
        patchAutoScrollControlColors(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.241") {
            patchIoThreadRefreshCache()
        }
    }
}

@Suppress("unused")
val patrolPatch = resourcePatch(
    name = "巡回・通知・更新",
    description = "登録スレの新着通知、巡回時の画像保存、巡回間隔の選択肢拡張、更新対象、スレ更新の検証、自動リロード、自動スクロールの色を扱います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(patrolBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/patrol/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
        val patrolArrayNames = document("res/values/arrays.xml").use {
            PatrolIntervalOptions.expand(it)
        }
        get("res").listFiles().orEmpty().filter {
            it.isDirectory && it.name.startsWith("values-")
        }.forEach { directory ->
            directory.listFiles().orEmpty().filter { file ->
                file.extension == "xml" && patrolArrayNames.any { name ->
                    file.readText().contains("name=\"$name\"")
                }
            }.forEach { file ->
                document("res/${directory.name}/${file.name}").use {
                    PatrolIntervalOptions.expandOverrides(it, patrolArrayNames)
                }
            }
        }
    }
}
