package app.morphe.patches.chmate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val themeSettingsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        patchNativeDrawDates(packageMetadata.versionName)
        patchNativeAaClassifier(packageMetadata.versionName)
        patchNativeUiTheme(packageMetadata.versionName)
        if(packageMetadata.versionName=="0.8.10.242 dev")patchS2mSettingsStartup242()
        if (packageMetadata.versionName in setOf("0.8.10.191 dev", "0.8.10.226 dev", "0.8.10.241", "0.8.10.242 dev")) {
            val profile = profileFor(packageMetadata.versionName)
            patchPostVersionInfo(profile.applicationClass)
        }
        patchSettingsSearchAliases(packageMetadata.versionName)
        patchNativeUpdatePolicy(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.226 dev") {
            patchAboutLogoThemeColor226()
        }
    }
}

@Suppress("unused")
val themeSettingsPatch = resourcePatch(
    name = "テーマ・見た目・設定まわり",
    description = "自作テーマ、AA判定、描画日付、設定検索エイリアス、更新ポリシー、バージョン情報表示などを扱います。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(themeSettingsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/theme-settings/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
