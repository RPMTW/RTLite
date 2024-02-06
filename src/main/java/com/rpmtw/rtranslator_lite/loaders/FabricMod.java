package com.rpmtw.rtranslator_lite.loaders;

import com.rpmtw.rtranslator_lite.RTranslatorLite;
import com.rpmtw.rtranslator_lite.core.RTLiteLogger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class FabricMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RTLiteLogger.info("RTranslator Lite is loading (mod loader side)");

        boolean hasPlatformMod = FabricLoaderImpl.INSTANCE.isModLoaded("rpmtw_platform_mod");
        if (hasPlatformMod) {
            RTLiteLogger.info("Because you have installed the RPMTW Platform Mod, RTLite will not be loaded.");
            return;
        }

        Path gameDir = FabricLoader.getInstance().getGameDir();
        String version = getGameVersion();
        if (gameDir == null || version == null) {
            RTLiteLogger.error("Failed to get game directory or version");
            return;
        }

        RTranslatorLite.init(gameDir, version, hasFabricApi());
    }

    @Nullable
    private String getGameVersion() {
        try {
            return FabricLoaderImpl.INSTANCE.getGameProvider().getNormalizedGameVersion();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean hasFabricApi() {
        return FabricLoaderImpl.INSTANCE.isModLoaded("fabric-api-base") || FabricLoaderImpl.INSTANCE.isModLoaded("fabric");
    }
}
