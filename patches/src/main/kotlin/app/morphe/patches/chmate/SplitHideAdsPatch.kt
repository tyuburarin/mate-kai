package app.morphe.patches.chmate

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val hideAdsBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        val profile = profileFor(packageMetadata.versionName)
        if (packageMetadata.versionName == "0.8.10.191 dev") {
            patchLegacyFragmentBannerDiscovery()
            patchLegacyThreadListAd("Lo/m9ExternalSyntheticLambda1;")
        } else {
            // Inject while p1 is still guaranteed to contain the Fragment root; the
            // extension posts its scans to the view queue, so child views are inspected
            // after construction.
            mutableClassDefBy(profile.homeFragmentClass).methods.single { method ->
                method.name == "onViewCreated"
                    && method.returnType == "V"
                    && method.parameters.map(CharSequence::toString) ==
                    listOf("Landroid/view/View;", "Landroid/os/Bundle;")
            }.addInstruction(
                0,
                "invoke-static/range { p1 .. p1 }, $EXTENSION->hideHomeBanner(Landroid/view/View;)V"
            )
        }
        patchCommonAdSdkInitialization()
        buildList {
            add("Lcom/amazon/device/ads/DTBAdRequest;")
            if (profile.hasLevelPlayBanner) {
                add("Lcom/unity3d/mediation/banner/LevelPlayBannerAdView;")
            }
        }.forEach { classType ->
            mutableClassDefBy(classType).methods
                .filter { it.name == "loadAd" && it.returnType == "V" }
                .forEach { it.addHideAdsGuard() }
        }
        if (profile.hasLevelPlayBanner) {
            patchLevelPlayTrackerInitialization()

            mutableClassDefBy("Lcom/unity3d/mediation/banner/LevelPlayBannerAdView;")
                .methods
                .filter { it.name == "<init>" }
                .forEach {
                    it.addBeforeEveryReturn(
                        "invoke-static/range { p0 .. p0 }, $EXTENSION->hideAdView(Landroid/view/View;)V"
                    )
                }
        }
        // The exact class is version-specific, but each target was matched by the same
        // FrameLayout/ad-placement/load-method structure instead of by its obfuscated name.
        mutableClassDefBy(profile.homeAdClass).methods.forEach { method ->
            when {
                method.name == "<init>" -> method.addBeforeEveryReturn(
                    "invoke-static/range { p0 .. p0 }, $EXTENSION->hideAdView(Landroid/view/View;)V"
                )
                profile.homeAdLoadMethod != null
                    && method.name == profile.homeAdLoadMethod
                    && method.returnType == "V"
                    && method.parameters.isEmpty() ->
                    method.addHideAdsViewGuard()
            }
        }
        if (packageMetadata.versionName == "0.8.10.226 dev") {
            patchThreadBannerAdWrapper("Lo/TTVideoLandingPageLink2Activity1;")
            patchLegacyThreadListAd("Lo/listener;")
        }
        if (packageMetadata.versionName == "0.8.10.243 dev") {
            patchModernThreadListAd()
        }
    }
}

@Suppress("unused")
val hideAdsPatch = resourcePatch(
    name = "広告非表示",
    description = "ホーム・スレ一覧・スレ内のバナー／動画広告を非表示にし、広告SDKの初期化を抑止します。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(hideAdsBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/hide-ads/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
