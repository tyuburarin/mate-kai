package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val talkUpliftBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        when (packageMetadata.versionName) {
            "0.8.10.191 dev" -> {
                patchLegacyTalkWriteUrlParsing()
                patchLegacyTalkDatLoading()
                patchLegacyTalkAuthIntegrity()
                patchLegacyCellularNetworkSelection()
                patchLegacyCellularSocketRefresh()
            }
            "0.8.10.226 dev" -> {
                patchUpliftIntegrityComparison("Lo/OpenJSSEPlatformCompanion;", "d", "Lo/OpenJSSEPlatformCompanion\$IconCompatParcelizer;")
                patchUpliftIntegrityComparison("Lo/OpenJSSEPlatformCompanion;", "j", "[Ljava/lang/Object;", returnType = "Ljava/lang/Object;")
                patchLiteralDiagnosticSubstring242(
                    "Lo/OpenJSSEPlatformCompanion;",
                    "1,30,null cannot be cast to non-null type " +
                        "jp.syoboi.a2chMate.bbs.a5ch.UpliftClient.LoginResult.Success",
                    5,
                )
                patchUpliftTokenExpiryArithmetic(
                    "Lo/OpenJSSEPlatformCompanion;", "d",
                    "Lo/OpenJSSEPlatformCompanion\$IconCompatParcelizer;", 653820,
                )
                patchPreIoCellularNetworkSelection()
                patchPreIoCellularSocketRefresh()
                patchPreIoTalkDatLoading()
                patchPreIoTalkPostIntegrity()
                patchPreIoImageUploadIntegrityTrap("Lo/fWG1;")
            }
            "0.8.10.241" -> {
                patchIoTalkDatLoading()
                patchIoTalkPostIntegrity()
                patchUpliftIntegrityComparison("Lo/VLj;", "c", "Lo/VLj\$ComponentActivity;")
                patchUpliftIntegrityComparison("Lo/VLj;", "c", "Lo/VLj\$ComponentActivity;", "Lo/getPaint;", "J")
                patchIoUpliftLoginCastMessage()
                patchIoUpliftTokenExpiryArithmetic()
            }
            "0.8.10.242 dev" -> {
                patchModernTalkDatLoading242()
                patchTalk242AuthenticationAndTokenCalls()
                patchModernTalkIntegrityPrimitives("Lo/GoogleApiSettingsBuilder;", "normalizeIntegrityState242")
                patchTalkPostDiagnostics242()
                patchUpliftPostingIntegrity242()
                patchUpliftLoginIntegrity242()
                patchImageUploadIntegrity242()
                patchLiteralDiagnosticSubstring242(
                    "Lo/getAdLogo;",
                    "3|18|null cannot be cast to non-null type " +
                        "jp.syoboi.a2chMate.bbs.a5ch.UpliftClient.LoginResult.Success",
                    5,
                )
                patchLiteralDiagnosticSubstring242(
                    "Ljp/syoboi/a2chMate/fragment/dialog/ThreadListSettingDialogViewModel;",
                    "20|21|31|22|6|threadQuickFilterManager",
                    14,
                )
            }
            "0.8.10.243 dev" -> {
                patchUpliftIntegrityComparison("Lo/zzaai;", "e", "Lo/zzaai\$ComponentActivity;")
                patchUpliftIntegrityComparison("Lo/zzaah;", "d", "Lo/zzaai\$ComponentActivity;", "Lo/zzbus;", "J")
                patchLiteralDiagnosticSubstring242(
                    "Lo/zzaah;",
                    "30/22/1/null cannot be cast to non-null type " +
                        "jp.syoboi.a2chMate.bbs.a5ch.UpliftClient.LoginResult.Success",
                    8,
                )
                patchModernTalkDatLoading()
                patchModernTalkPostIntegrity()
                patchModernTalkIntegrityPrimitives()
            }
        }
    }
}

@Suppress("unused")
val talkUpliftPatch = resourcePatch(
    name = "5ch Talk / UPLIFT 通信まわり",
    description = "Talk経由のDAT取得・認証・トークン、投稿とログインの整合性チェック、UPLIFTまわりの処理、携帯回線の接続選択を行います。書き込みとDAT取得の根幹に近い処理です。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(talkUpliftBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/talk-uplift/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
