package com.rpmtw.rtranslator_lite.loaders;

import com.rpmtw.rtranslator_lite.RTranslatorLite;
import com.rpmtw.rtranslator_lite.core.RTLiteLogger;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

/**
 * Support for net.minecraft.launchwrapper (<a href="https://github.com/Mojang/LegacyLauncher/tree/master">LegacyLauncher</a>).
 * FM/LiteLoader 1.6 ~ 1.12.2
 */
public class LegacyMinecraftMod implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        RTLiteLogger.info("RTranslator Lite is loading (mod loader side)");

        if (gameDir == null || profile == null) {
            RTLiteLogger.error("Failed to get game directory or version");
            return;
        }

        RTranslatorLite.init(gameDir.toPath(), profile, false);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
    }

    @Override
    public String getLaunchTarget() {
        return "";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
