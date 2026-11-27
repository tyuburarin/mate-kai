package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val externalBoardsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchEdgeReporterHistory(packageMetadata.versionName)
        patchEdgeReporterDisplay(packageMetadata.versionName)
        patchEdgeReporterTitleCopy(packageMetadata.versionName)
        when (packageMetadata.versionName) {
            "0.8.10.191 dev" -> {
                EdgeSubjectUrl191Fingerprint.method.rewriteEdgeSubjectUrl()
                EdgeThreadMenu191Fingerprint.method.preserveEdgeReporterTitle("Lo/isReady;", "k")
                patchLegacyNextThreadTitleMatch191()
            }
            "0.8.10.226 dev" -> {
                EdgeSubjectUrl226Fingerprint.method.rewriteEdgeSubjectUrl()
                EdgeThreadMenu226Fingerprint.method.preserveEdgeReporterTitle("Lo/MessageInflater;", "m")
            }
            "0.8.10.241" -> EdgeSubjectUrl241Fingerprint.method.rewriteEdgeSubjectUrl()
            "0.8.10.242 dev" -> EdgeSubjectUrl242Fingerprint.method.rewriteEdgeSubjectUrl()
            "0.8.10.243 dev" -> EdgeSubjectUrlFingerprint.method.rewriteEdgeSubjectUrl()
        }
        patchExternalBoardAuth(packageMetadata.versionName)
        patchExternalBoardEditor(packageMetadata.versionName)
    }
}

@Suppress("unused")
val externalBoardsPatch = resourcePatch(
    name = "外部板",
    description = "外部板（エッヂ・kenmo・爆サイ・ふたば・あにまん等）の表示・認証・編集、記者ID表示、過去ログ検索、作成日時表示を行います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(externalBoardsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/external-boards/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
