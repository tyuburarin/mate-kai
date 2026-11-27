package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val advancedNgBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchReadingChallenge(packageMetadata.versionName)
        when (packageMetadata.versionName) {
            "0.8.10.191 dev" -> {
                patchProgrammableNg191()
                patchPreIoHissiMenu(
                    "Lo/setExtraParameter;", "d",
                    "Lo/processAdDisplayErrorPostbackForUserError;",
                    "Lo/setExtraParameter\$RemoteActionCompatParcelizer;",
                )
            }
            "0.8.10.226 dev" -> {
                patchProgrammableNg226()
                patchPreIoHissiMenu()
            }
            "0.8.10.241" -> {
                patchPreIoHissiMenu("Lo/lhA1;", "d", "Lo/setDislikeWidth;", "Lo/lhA1\$write;")
            }
            "0.8.10.242 dev" -> {
                patchProgrammableNgModern("Lo/emptyToNull;", "c", "c")
                patchPreIoHissiMenu("Lo/kUGNk2;", "c", "Lo/KeJ11;", "Lo/kUGNk2\$ComponentActivity;")
            }
            "0.8.10.243 dev" -> {
                patchProgrammableNgModern("Lo/zzdic;", "a", "c")
                patchPreIoHissiMenu("Lo/zzacz;", "c", "Lo/zzabv;", "Lo/zzacz\$write;")
            }
        }
        if (packageMetadata.versionName == "0.8.10.241") {
            patchProgrammableNgModern("Lo/RewardedInterstitialAdLoadCallback;", "a", "a")
        }
    }
}

@Suppress("unused")
val advancedNgPatch = resourcePatch(
    name = "高度NG・必死チェッカー",
    description = "JSによる高度なNGと必死チェッカー関連のフック、閲覧チャレンジまわりを有効にします。投稿者IDの連鎖NGもこの中の処理から呼ばれます。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(advancedNgBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/advanced-ng/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
