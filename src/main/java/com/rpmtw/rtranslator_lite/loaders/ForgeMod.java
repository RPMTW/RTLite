package com.rpmtw.rtranslator_lite.loaders;

import com.rpmtw.rtranslator_lite.RTranslatorLite;
import com.rpmtw.rtranslator_lite.core.RTLiteLogger;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

/**
 * Support for cpw.mods.modlauncher (Forge).
 * Forge 1.13.2 ~ Latest
 * NeoForge 1.20.2 ~ Latest
 */
public class ForgeMod implements ITransformationService {
    @Override
    public @NotNull String name() {
        return "RTLite";
    }

    @Override
    public void initialize(@NotNull IEnvironment environment) {
        RTLiteLogger.info("RTranslator Lite is loading (mod loader side)");

        Optional<Path> gameDir = environment.getProperty(IEnvironment.Keys.GAMEDIR.get());
        Optional<String> gameVersion = environment.getProperty(IEnvironment.Keys.VERSION.get());

        if (!gameDir.isPresent() || !gameVersion.isPresent()) {
            RTLiteLogger.error("Failed to get game directory or version");
            return;
        }

        String[] modFiles = gameDir.get().resolve("mods").toFile().list();
        boolean hasPlatformMod = modFiles != null && Arrays.stream(modFiles).anyMatch(name -> name.contains("rpmtw-platform-mod"));
        if (hasPlatformMod) {
            RTLiteLogger.info("Because you have installed RPMTW Platform Mod, RTLite will not be loaded.");
            return;
        }

        RTranslatorLite.init(gameDir.get(), gameVersion.get(), false);
    }

    @Override
    public void beginScanning(@NotNull IEnvironment environment) {
    }

    @Override
    public void onLoad(@NotNull IEnvironment environment, @NotNull Set<String> otherServices) {
    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return Collections.emptyList();
    }
}
