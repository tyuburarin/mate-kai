package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val readingPositionBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchReadingPosition(packageMetadata.versionName)
        val readManager = when (packageMetadata.versionName) {
            "0.8.10.241" -> "Lo/hLn11;"
            "0.8.10.242 dev" -> "Lo/VN21;"
            else -> null
        }
        readManager?.let { type ->
            mutableClassDefBy(type).methods.single {
                it.name == "<init>" && it.parameters.isEmpty()
            }.addBeforeEveryReturn(
                "invoke-static/range { p0 .. p0 }, $EXTENSION->captureReadCountManager(Ljava/lang/Object;)V"
            )
        }
    }
}

@Suppress("unused")
val readingPositionSplitPatch = resourcePatch(
    name = "読む位置・既読管理",
    description = "読む位置の保持と既読数カウントの取り込みを行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(readingPositionBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/reading-position/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
