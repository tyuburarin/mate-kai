package app.morphe.patches.chmate

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch

private val homeMultiSelectBytecodePatch = bytecodePatch {
    dependsOn(haiagaruPatch)
    execute {
        val profile = profileFor(packageMetadata.versionName)
        patchHomeBoardMultiSelect(packageMetadata.versionName, profile.homeFragmentClass)
        if (packageMetadata.versionName == "0.8.10.242 dev") {
            mutableClassDefBy(profile.homeFragmentClass).methods.single {
                it.name == "onViewCreated" && it.parameterTypes == listOf("Landroid/view/View;", "Landroid/os/Bundle;")
            }.addInstruction(0,"invoke-static {p0, p1}, Lapp/morphe/extension/chmate/HomeSelectionBar;->bind(Ljava/lang/Object;Landroid/view/View;)V")
        }
    }
}

@Suppress("unused")
val homeMultiSelectPatch = resourcePatch(
    name = "ホーム複数選択・選択バー",
    description = "ホームの板の複数選択と選択バーを有効にします。初期状態は未選択。Haiagaru本体とは別に選択できます。",
    default = false,
) {
    compatibleWith(chMateCompatibility)
    dependsOn(homeMultiSelectBytecodePatch)
    execute {
        get("assets").resolve("haiagaru/home-multi-select/installed").apply {
            parentFile.mkdirs()
            writeText("1")
        }
    }
}
